package edu.arizona.kim.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.springframework.ldap.core.DirContextOperations;

import edu.arizona.kim.eds.UaEdsAffiliation;
import edu.arizona.kim.eds.UaEdsConstants;
import edu.arizona.kim.eds.UaEdsRecord;
import edu.arizona.kim.eds.UaEdsRecordFactory;



public class UaEntityEmploymentMapper extends UaBaseMapper<List<EntityEmployment>> {

    private static final Logger LOG = Logger.getLogger(UaEntityEmploymentMapper.class);

    private ParameterService parameterService;
    private UaEntityAffiliationMapper affiliationMapper;


    @Override
    protected List<EntityEmployment> mapDtoFromContext(DirContextOperations context) {
        List<EntityEmployment.Builder> builders = mapBuilderFromContext(context);
        List<EntityEmployment> employments = new ArrayList<>();
        if (builders != null) {
            for (EntityEmployment.Builder builder : builders) {
                employments.add(builder.build());
            }
        }

        return employments;
    }


    protected List<EntityEmployment.Builder> mapBuilderFromContext(DirContextOperations context) {
        UaEdsRecord edsRecord = UaEdsRecordFactory.getEdsRecord(context);
        if (edsRecord == null) {
            LOG.warn("No active and valid EDS eduPersonAffiliation found for netId: " + getNetIdForLogWarning(context));
            return null;
        }

        // Setting up for loop below
        int affIndex = 0; // Used for syncing ordered lists of UaEdsAffiliation and EntityAffiliation
        int affId = 1; // The affiliation id is just its position in an ordered set
        List<EntityAffiliation.Builder> kualiAffs = getAffiliationMapper().mapBuilderFromContext(context);
        Set<String> nonEmployeeAffs = getValueSetForParameter(getEdsConstants().getEdsNonEmployeeAffsParamKey());
        List<EntityEmployment.Builder> employments = new ArrayList<>();

        for (UaEdsAffiliation edsAff : edsRecord.getOrderedAffiliations()) {
            if (nonEmployeeAffs.contains(edsAff.getAffiliatonString())) {
                // Non-employees don't have employment info, like student or retiree
                affIndex++;
                continue;
            }

            EntityEmployment.Builder employmentInfo = EntityEmployment.Builder.create();
            employmentInfo.setPrimary(affId == 1);// Primary aff always has id == 1
            employmentInfo.setEntityAffiliation(getAffForEmploymentInfo(affIndex, kualiAffs));
            employmentInfo.setEmploymentRecordId(Integer.toString(affId));
            employmentInfo.setEmployeeId(edsRecord.getEmplId());
            employmentInfo.setActive(edsAff.isActive());
            employmentInfo.setEmployeeStatus(CodedAttribute.Builder.create(edsAff.getStatusCode()));
            employmentInfo.setPrimaryDepartmentCode(getConstants().getDefaultChartCode() + "-" + edsAff.getDeptCode());
            employmentInfo.setEmployeeType(CodedAttribute.Builder.create(edsAff.getEmployeeType()));
            employmentInfo.setBaseSalaryAmount(KualiDecimal.ZERO);//UAF-1030: baseSalaryAmount should always be $0.00

            employments.add(employmentInfo);
            affIndex++;
            affId++;
        }

        return employments;
    }


    /*
     * Note:
     * Fact 1: The for loop this method is called from loops on EdsRecord#getOrderedAffs(context)
     * Fact 2: Given the same context, EdsRecord#getOrderedAffs(context) will always return the
     *         same-ordered collection
     * Fact 3: The passed-in kualiAffs are actually built from EdsRecord.getOrderedAffs(context), with the same
     *         context as the loop that called this method
     *
     * Point: That kualiAffs.get(n) will be informationally equivalent to
     *        EdsRecord#getOrderedAffiliations().get(n), so each employmentInfo in the calling loop
     *        will line up correctly with its corresponding aff (was bugged under FIN-294)
     */
    private EntityAffiliation.Builder getAffForEmploymentInfo(int affIndex, List<EntityAffiliation.Builder> kualiAffs) {
        if (kualiAffs != null && affIndex < kualiAffs.size()) {
            return kualiAffs.get(affIndex);
        }
        return null;
    }


    private String getNetIdForLogWarning(DirContextOperations context) {
        if (context == null) {
            return "LDAP context was null!";
        }

        UaEdsConstants edsConstants = new UaEdsConstants();
        String netIdContextKey = edsConstants.getUidContextKey();
        String netId = context.getStringAttribute(netIdContextKey);
        if (StringUtils.isBlank(netId)) {
            return "Could not find uid in LDAP context";
        }

        return netId;
    }


    private Set<String> getValueSetForParameter(String parameterKey) {
        String listAsCommaString = getStringForParameter(parameterKey);
        String[] listAsArray = listAsCommaString.split(getEdsConstants().getKfsParamDelimiter());
        return new HashSet<>(Arrays.asList(listAsArray));
    }


    private String getStringForParameter(String parameterKey) {
        String namespaceCode = getEdsConstants().getParameterNamespaceCode();
        String detailTypeCode = getEdsConstants().getParameterDetailTypeCode();
        Parameter parameter = getParameterService().getParameter(namespaceCode, detailTypeCode, parameterKey);
        if (parameter == null) {
            String message = String.format("ParameterService returned null for parameterKey: '%s', namespaceCode: '%s', detailTypeCode: '%s'", parameterKey, namespaceCode, detailTypeCode);
            throw new RuntimeException(message);
        }
        return parameter.getValue();
    }


    private ParameterService getParameterService() {
        return parameterService;
    }


    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }


    public final UaEntityAffiliationMapper getAffiliationMapper() {
        return this.affiliationMapper;
    }


    public void setAffiliationMapper(UaEntityAffiliationMapper entityAffiliationMapper) {
        this.affiliationMapper = entityAffiliationMapper;
    }

}
