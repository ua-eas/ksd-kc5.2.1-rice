package edu.arizona.kim.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.entity.EntityDefaultQueryResults;
import org.kuali.rice.kim.api.identity.entity.EntityQueryResults;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierType;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographics;
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalQueryResults;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.identity.residency.EntityResidency;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.identity.visa.EntityVisa;
import org.kuali.rice.kim.dao.LdapPrincipalDao;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.EntityTypeBo;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo;
import org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipStatusBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierTypeBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo;
import org.kuali.rice.kim.service.LdapIdentityService;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.*;

/**
 * BURLAP: Add thorough descritpion of why this impl is necessary above what is present in rice already, also touch on EDS flavor of LDAP.
 */
public class UaLdapIdentityServiceImpl implements LdapIdentityService {

    // Class variables
    private static final String UNSUPPORTED_OPERATION_MSG = "This method is not supported under the UA EDS MOD, please see UAF-2303 for further information.";

    // Instance variables
    private LdapPrincipalDao ldapPrincipalDao;
    private BusinessObjectService businessObjectService;


    public UaLdapIdentityServiceImpl() {
        // Spring might complain w/out constructor present
    }



    @Override
    public CodedAttribute getAddressType(String addressTypeCode) {
        incomingParamCheck(addressTypeCode);
        EntityAddressTypeBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityAddressTypeBo.class, addressTypeCode);
        return bo == null ? null : EntityAddressTypeBo.to(bo);
    }


    @Override
    public List<CodedAttribute> findAllAddressTypes() {
        Map<String, Boolean> activeInicatorMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityAddressTypeBo> bos = getBusinessObjectService().findMatching(EntityAddressTypeBo.class, activeInicatorMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for(EntityAddressTypeBo bo : bos) {
            codedAttributes.add(EntityAddressTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public EntityAffiliationType getAffiliationType(String code) {
        incomingParamCheck(code);
        EntityAffiliationTypeBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityAffiliationTypeBo.class, code);
        return bo == null ? null : EntityAffiliationTypeBo.to(bo);
    }


    @Override
    public List<EntityAffiliationType> findAllAffiliationTypes() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityAffiliationTypeBo> bos = getBusinessObjectService().findMatching(EntityAffiliationTypeBo.class, queryMap);

        List<EntityAffiliationType> codedAttributes = new ArrayList<EntityAffiliationType>();
        for (EntityAffiliationTypeBo bo : bos) {
            codedAttributes.add(EntityAffiliationTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public CodedAttribute getCitizenshipStatus(String code) {
        incomingParamCheck(code);
        EntityCitizenshipStatusBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityCitizenshipStatusBo.class, code);
        return bo == null ? null : EntityCitizenshipStatusBo.to(bo);
    }


    @Override
    public List<CodedAttribute> findAllCitizenshipStatuses() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityCitizenshipStatusBo> bos = getBusinessObjectService().findMatching(EntityCitizenshipStatusBo.class, queryMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for(EntityCitizenshipStatusBo bo : bos) {
            codedAttributes.add(EntityCitizenshipStatusBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public EntityNamePrincipalName getDefaultNamesForPrincipalId(String principalId) {
        EntityDefault entityDefault = getEntityDefaultByPrincipalId(principalId);
        if(entityDefault == null) {
            return null;
        }

        EntityNamePrincipalName.Builder nameBuilder = EntityNamePrincipalName.Builder.create();
        for(Principal principal : entityDefault.getPrincipals()) {
            // This should only loop once w/ EDS style records anyway, keeping with legacy behavior just in case
            nameBuilder.setPrincipalName(principal.getPrincipalName());
        }

        nameBuilder.setDefaultName(EntityName.Builder.create(entityDefault.getName()));
        if(StringUtils.isBlank(entityDefault.getName().getCompositeName())) {
            String compositeName = getCompositeName(entityDefault);
            nameBuilder.getDefaultName().setCompositeName(compositeName);
        }

        return nameBuilder.build();
    }


    /*
     * Default behavior ported from IdentityServiceImpl
     */
    private String getCompositeName(EntityDefault defaultEntity) {
        String lastName = defaultEntity.getName().getLastName() + ", ";
        String firstName = defaultEntity.getName().getFirstName();
        String middleName = defaultEntity.getName().getMiddleName() == null ? "" : " " + defaultEntity.getName().getMiddleName().trim();
        return lastName + firstName + middleName;
    }


    @Override
    public CodedAttribute getEmailType(String code) {
        incomingParamCheck(code);
        EntityEmailTypeBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityEmailTypeBo.class, code);
        return bo == null ? null : EntityEmailTypeBo.to(bo);
    }


    @Override
    public List<CodedAttribute> findAllEmailTypes() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityEmailTypeBo> bos = getBusinessObjectService().findMatching(EntityEmailTypeBo.class, queryMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityEmailTypeBo bo : bos) {
            codedAttributes.add(EntityEmailTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public CodedAttribute getEmploymentStatus(String code) {
        incomingParamCheck(code);
        EntityEmploymentStatusBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityEmploymentStatusBo.class, code);
        return bo == null ? null : EntityEmploymentStatusBo.to(bo);
    }


    @Override
    public List<CodedAttribute> findAllEmploymentStatuses() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityEmploymentStatusBo> bos = getBusinessObjectService().findMatching(EntityEmploymentStatusBo.class, queryMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityEmploymentStatusBo bo : bos) {
            codedAttributes.add(EntityEmploymentStatusBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public CodedAttribute getEmploymentType(String code) {
        incomingParamCheck(code);
        EntityEmploymentStatusBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityEmploymentStatusBo.class, code);
        return bo == null ? null : EntityEmploymentStatusBo.to(bo);
    }


    @Override
    public List<CodedAttribute> findAllEmploymentTypes() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityEmploymentTypeBo> bos = getBusinessObjectService().findMatching(EntityEmploymentTypeBo.class, queryMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityEmploymentTypeBo bo : bos) {
            codedAttributes.add(EntityEmploymentTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public EntityDefault getEntityDefault(String entityId) {
        incomingParamCheck(entityId);
        return getLdapPrincipalDao().getEntityDefault(entityId);
    }


    @Override
    public EntityDefault getEntityDefaultByPrincipalId(String principalId) {
        incomingParamCheck(principalId);
        return getLdapPrincipalDao().getEntityDefaultByPrincipalId(principalId);
    }


    @Override
    public EntityDefault getEntityDefaultByPrincipalName(String principalName) {
        incomingParamCheck(principalName);
        return getLdapPrincipalDao().getEntityDefaultByPrincipalName(principalName);
    }


    @Override
    public EntityDefault getEntityDefaultByEmployeeId(String employeeId) {
        incomingParamCheck(employeeId);
        return getLdapPrincipalDao().getEntityDefaultByEmployeeId(employeeId);
    }


    @Override
    public Entity getEntity(String entityId) {
        incomingParamCheck(entityId);
        return getLdapPrincipalDao().getEntity(entityId);
    }


    @Override
    public Entity getEntityByPrincipalId(String principalId) {
        incomingParamCheck(principalId);
        return getLdapPrincipalDao().getEntityByPrincipalId(principalId);
    }


    @Override
    public Entity getEntityByPrincipalName(String principalName) {
        incomingParamCheck(principalName);
        return getLdapPrincipalDao().getEntityByPrincipalName(principalName);
    }


    @Override
    public Entity getEntityByEmployeeId(String employeeId) {
        incomingParamCheck(employeeId);
        return getLdapPrincipalDao().getEntityByEmployeeId(employeeId);
    }


    @Override
    public CodedAttribute getNameType(String code) throws RiceIllegalArgumentException {
        incomingParamCheck(code);
        EntityNameTypeBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityNameTypeBo.class, code);
        return bo == null ? null : EntityNameTypeBo.to(bo);

    }


    @Override
    public List<CodedAttribute> findAllNameTypes() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityNameTypeBo> bos = getBusinessObjectService().findMatching(EntityNameTypeBo.class, queryMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityNameTypeBo bo : bos) {
            codedAttributes.add(EntityNameTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public CodedAttribute getEntityType(String code) {
        incomingParamCheck(code);
        EntityTypeBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityTypeBo.class, code);
        return bo == null ? null : EntityTypeBo.to(bo);
    }


    @Override
    public List<CodedAttribute> findAllEntityTypes() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityTypeBo> bos = getBusinessObjectService().findMatching(EntityTypeBo.class, queryMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityTypeBo bo : bos) {
            codedAttributes.add(EntityTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public EntityExternalIdentifierType getExternalIdentifierType(String code) {
        incomingParamCheck(code);
        EntityExternalIdentifierTypeBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityExternalIdentifierTypeBo.class, code);
        return bo == null ? null : EntityExternalIdentifierTypeBo.to(bo);
    }


    @Override
    public List<EntityExternalIdentifierType> findAllExternalIdendtifierTypes() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityExternalIdentifierTypeBo> bos = businessObjectService.findMatching(EntityExternalIdentifierTypeBo.class, queryMap);

        List<EntityExternalIdentifierType> codedAttributes = new ArrayList<EntityExternalIdentifierType>();
        for (EntityExternalIdentifierTypeBo bo : bos) {
            codedAttributes.add(EntityExternalIdentifierTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public CodedAttribute getPhoneType(String code) {
        incomingParamCheck(code);
        EntityPhoneTypeBo bo = getBusinessObjectService().findBySinglePrimaryKey(EntityPhoneTypeBo.class, code);
        return bo == null ? null : EntityPhoneTypeBo.to(bo);
    }


    @Override
    public List<CodedAttribute> findAllPhoneTypes() {
        Map<String, Boolean> queryMap = Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        Collection<EntityPhoneTypeBo> bos = getBusinessObjectService ().findMatching(EntityPhoneTypeBo.class, queryMap);

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityPhoneTypeBo bo : bos) {
            codedAttributes.add(EntityPhoneTypeBo.to(bo));
        }

        return Collections.unmodifiableList(codedAttributes);
    }


    @Override
    public Principal getPrincipal(String principalId) {
        return getLdapPrincipalDao().getPrincipal(principalId);
    }


    @Override
    public List<Principal> getPrincipals(List<String> principalIds) {
        return getLdapPrincipalDao().getPrincipalsByPrincipalIds(principalIds);
    }


    @Override
    public Principal getPrincipalByPrincipalName(String principalName) {
        return getLdapPrincipalDao().getPrincipalByName(principalName);
    }


    @Override
    public List<Principal> getPrincipalsByEntityId(String entityId) {
        incomingParamCheck(entityId);
        List<String> ids = Collections.singletonList(entityId);
        // Entity ID and principal ID are one and the same.
        return getLdapPrincipalDao().getPrincipalsByPrincipalIds(ids);
    }


    @Override
    public List<Principal> getPrincipalsByEmployeeId(String employeeId) {
        incomingParamCheck(employeeId);
        return getLdapPrincipalDao().getPrincipalsByEmployeeId(employeeId);
    }


    @Override
    public List<EntityDefault> findEntityDefaults(Map<String, String> criteria, boolean unbounded) {
        return getLdapPrincipalDao().lookupEntityDefault(criteria, unbounded);
    }


    @Override
    public List<EntityDefault> getEntityDefaultsByPrincipalIds(Collection<String> principalIds) {
        Map <String, Object> criteria = new HashMap<String, Object>();
        criteria.put(getLdapPrincipalDao().getKimConstants().getKimLdapIdProperty(), principalIds);
        return getLdapPrincipalDao().search(EntityDefault.class, criteria);
    }


    /*
    * Helper to ensure that objectToCheck is not null, and that if objectToCheck
    * is a String, to also ensure it's not blank.
    */
    private void incomingParamCheck(Object objectToCheck) {
        if (objectToCheck == null) {
            throw new RiceIllegalArgumentException("Incoming param is null!");
        } else if (objectToCheck instanceof String && StringUtils.isBlank((String) objectToCheck)) {
            throw new RiceIllegalArgumentException("Incoming param is blank!");
        }
    }

    public LdapPrincipalDao getLdapPrincipalDao() {
        return ldapPrincipalDao;
    }


    public void setLdapPrincipalDao(LdapPrincipalDao ldapPrincipalDao) {
        this.ldapPrincipalDao = ldapPrincipalDao;
    }


    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }


    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


/*
    * BURLAP: ADD IN DOCUMENTATION -- Add bullet list for each reason, then refer to numbered reason on each method.
    * The following section of this class contains all of the overridden
    * methods from the IdentityService interface. Rice was originally written
    * without LDAP in mind at all[0], and since so few institutions had any
    * requirement to use LDAP in their financials, IdentityService
    */


    @Override
    public EntityAddress addAddressToEntity(EntityAddress address) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal getPrincipalByPrincipalNameAndPassword(String principalName, String password) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal addPrincipalToEntity(Principal principal) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal updatePrincipal(Principal principal) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal inactivatePrincipal(String principalId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal inactivatePrincipalByName(String principalName) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityTypeContactInfo addEntityTypeContactInfoToEntity(EntityTypeContactInfo entityTypeContactInfo) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityTypeContactInfo updateEntityTypeContactInfo(EntityTypeContactInfo entityTypeContactInfo) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityTypeContactInfo inactivateEntityTypeContactInfo(String entityId, String entityTypeCode) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAddress updateAddress(EntityAddress address) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAddress inactivateAddress(String addressId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmail addEmailToEntity(EntityEmail email) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmail updateEmail(EntityEmail email) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmail inactivateEmail(String emailId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPhone addPhoneToEntity(EntityPhone phone) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPhone updatePhone(EntityPhone phone) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPhone inactivatePhone(String phoneId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityExternalIdentifier addExternalIdentifierToEntity(EntityExternalIdentifier externalId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityExternalIdentifier updateExternalIdentifier(EntityExternalIdentifier externalId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAffiliation addAffiliationToEntity(EntityAffiliation affiliation) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAffiliation updateAffiliation(EntityAffiliation affiliation) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAffiliation inactivateAffiliation(String id) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPrivacyPreferences getEntityPrivacyPreferences(String entityId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPrivacyPreferences addPrivacyPreferencesToEntity(EntityPrivacyPreferences privacyPreferences) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPrivacyPreferences updatePrivacyPreferences(EntityPrivacyPreferences privacyPreferences) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityCitizenship addCitizenshipToEntity(EntityCitizenship citizenship) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityCitizenship updateCitizenship(EntityCitizenship citizenship) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityCitizenship inactivateCitizenship(String id) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEthnicity addEthnicityToEntity(EntityEthnicity ethnicity) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEthnicity updateEthnicity(EntityEthnicity ethnicity) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityResidency addResidencyToEntity(EntityResidency residency) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityResidency updateResidency(EntityResidency residency) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityVisa addVisaToEntity(EntityVisa visa) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityVisa updateVisa(EntityVisa visa) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Entity createEntity(Entity entity) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Entity updateEntity(Entity entity) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Entity inactivateEntity(String entityId) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityDefaultQueryResults findEntityDefaults( QueryByCriteria queryByCriteria) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityQueryResults findEntities(QueryByCriteria queryByCriteria) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public PrincipalQueryResults findPrincipals(QueryByCriteria query) throws RiceIllegalArgumentException {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityName addNameToEntity(EntityName name) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityName updateName(EntityName name) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityName inactivateName(String id) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmployment addEmploymentToEntity(EntityEmployment employment) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmployment updateEmployment(EntityEmployment employment) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmployment inactivateEmployment(String id) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityBioDemographics addBioDemographicsToEntity(EntityBioDemographics bioDemographics) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityBioDemographics updateBioDemographics(EntityBioDemographics bioDemographics) {
        //BURLAP: Add ref to specific reason above for overide
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

}
