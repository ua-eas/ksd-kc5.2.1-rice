package edu.arizona.kim.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.dao.LdapPrincipalDao;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.EntityTypeBo;
import org.kuali.rice.kim.impl.identity.IdentityServiceImpl;
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

/**
 * This class is a simplified implementation of the IdentityService and
 * LdapIdentityService interfaces. The vanilla IdentityService impls are more
 * complex in their inheritance and composition. Alternatively, this class
 * provides a clear and concise hierarchy which then serves to reduce bugs.
 * This implementation also cuts out inheritance to classes that still contain
 * termination points to KIM tables -- i.e., this class is gaurunteed to
 * *always* fetch its entity records directly from the EDS implementation of LDAP.
 *
 * Any methods that do not line up with this LDAP paradigm, have all been implemented
 * to throw an UnsupportedOperationException. This works since all of the screens
 * in the UI that can mutate Person data, are all turned off here at the UA. In such
 * a way, we ensure non-LDAP operations are never executed.
 */
public class UaLdapIdentityServiceImpl extends IdentityServiceImpl implements LdapIdentityService {

    // Class variables
    private static final String UNSUPPORTED_OPERATION_MSG = "This method is not supported under the UA EDS MOD, please see UAF-2303 for further information.";

    // Instance variables
    private LdapPrincipalDao ldapPrincipalDao;
    private BusinessObjectService businessObjectService;

    public static final String SYSTEM_USER_PRINCIPAL_ID = "1";

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
    public List<EntityDefault> getEntityDefaultsByCriteria(Map<String, List<String>> criteria) {
        incomingParamCheck(criteria);
        return getLdapPrincipalDao().getEntityDefaultsByCriteria(criteria);
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

        // Need to account for system user, since this can't be an LDAP call
        if (StringUtils.isNotBlank(principalId) && principalId.equals(SYSTEM_USER_PRINCIPAL_ID)) {
            return KimApiServiceLocator.getPersonService().getSystemUserPrincipalFromDb();
        }

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

    private LdapPrincipalDao getLdapPrincipalDao() {
        return ldapPrincipalDao;
    }


    // Note: This method name is wired in the spring file: KimEmbeddedSpringBeans.xml
    public void setLdapPrincipalDao(LdapPrincipalDao ldapPrincipalDao) {
        this.ldapPrincipalDao = ldapPrincipalDao;
    }


    private BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }


    // Note: This method name is wired in the spring file: KimEmbeddedSpringBeans.xml
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    /*
    * UAF-2303: Refactor EDS IdentityService kerfuffle
    *
    * The following section of this class contains all of the overridden
    * methods from the IdentityService interface that have no place in our LDAP
    * system. Rice was originally written without LDAP in mind at all. Since so few
    * institutions had any requirement to use LDAP in their financials, any later
    * contribs were only half-baked into the identity core. This being the case,
    * many methods try to access/mutate data in a non-LDAP way. We put the smack down
    * on these methods, and have overridden them for any of the following reasons:
    *
    * REASONS:
    * 1. Trying to mutate(or add) LDAP values (43 methods)
    * 2. Return type is a table row object, i.e., *QueryResults (3 methods)
    * 3. Special cases: are already deprecated in the other IdentityService
    *    impls (2 methods)
    */


    @Override
    public EntityAddress addAddressToEntity(EntityAddress address) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal addPrincipalToEntity(Principal principal) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal updatePrincipal(Principal principal) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal inactivatePrincipal(String principalId) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal inactivatePrincipalByName(String principalName) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityTypeContactInfo addEntityTypeContactInfoToEntity(EntityTypeContactInfo entityTypeContactInfo) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityTypeContactInfo updateEntityTypeContactInfo(EntityTypeContactInfo entityTypeContactInfo) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityTypeContactInfo inactivateEntityTypeContactInfo(String entityId, String entityTypeCode) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAddress updateAddress(EntityAddress address) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAddress inactivateAddress(String addressId) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmail addEmailToEntity(EntityEmail email) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmail updateEmail(EntityEmail email) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmail inactivateEmail(String emailId) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPhone addPhoneToEntity(EntityPhone phone) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPhone updatePhone(EntityPhone phone) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPhone inactivatePhone(String phoneId) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityExternalIdentifier addExternalIdentifierToEntity(EntityExternalIdentifier externalId) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityExternalIdentifier updateExternalIdentifier(EntityExternalIdentifier externalId) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAffiliation addAffiliationToEntity(EntityAffiliation affiliation) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAffiliation updateAffiliation(EntityAffiliation affiliation) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityAffiliation inactivateAffiliation(String id) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPrivacyPreferences addPrivacyPreferencesToEntity(EntityPrivacyPreferences privacyPreferences) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPrivacyPreferences updatePrivacyPreferences(EntityPrivacyPreferences privacyPreferences) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityCitizenship addCitizenshipToEntity(EntityCitizenship citizenship) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityCitizenship updateCitizenship(EntityCitizenship citizenship) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityCitizenship inactivateCitizenship(String id) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEthnicity addEthnicityToEntity(EntityEthnicity ethnicity) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEthnicity updateEthnicity(EntityEthnicity ethnicity) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityResidency addResidencyToEntity(EntityResidency residency) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityResidency updateResidency(EntityResidency residency) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityVisa addVisaToEntity(EntityVisa visa) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityVisa updateVisa(EntityVisa visa) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Entity createEntity(Entity entity) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Entity updateEntity(Entity entity) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Entity inactivateEntity(String entityId) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityName addNameToEntity(EntityName name) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityName updateName(EntityName name) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityName inactivateName(String id) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmployment addEmploymentToEntity(EntityEmployment employment) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmployment updateEmployment(EntityEmployment employment) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityEmployment inactivateEmployment(String id) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityBioDemographics addBioDemographicsToEntity(EntityBioDemographics bioDemographics) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityBioDemographics updateBioDemographics(EntityBioDemographics bioDemographics) {
        // Reject for reason #1
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    /**
     * UA KFS7 upgrade. (UAF-5246) new API for loading person information from EDS
     */
    @Override
    public List<EntityDefault> findEntityDefaultsByCriteriaMap( Map<String, String> criteria) {
    	return findEntityDefaults(criteria, false);
    }

    @Override
    public EntityDefaultQueryResults findEntityDefaults( QueryByCriteria queryByCriteria) {
        // Reject for reason #2
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

	@Override
    public EntityQueryResults findEntities(QueryByCriteria queryByCriteria) {
        // Reject for reason #2
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public PrincipalQueryResults findPrincipals(QueryByCriteria query) throws RiceIllegalArgumentException {
        // Reject for reason #2
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public EntityPrivacyPreferences getEntityPrivacyPreferences(String entityId) {
        // Reject for reason #3
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

    @Override
    public Principal getPrincipalByPrincipalNameAndPassword(String principalName, String password) {
        // Reject for reason #3
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);
    }

}
