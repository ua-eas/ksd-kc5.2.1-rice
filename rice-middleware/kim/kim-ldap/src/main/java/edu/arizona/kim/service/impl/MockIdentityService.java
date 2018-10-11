package edu.arizona.kim.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
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
import org.kuali.rice.kim.service.LdapIdentityService;
import org.kuali.rice.krad.service.BusinessObjectService;



public class MockIdentityService implements LdapIdentityService {
    private static final Map<String, Principal> nameToPrincipalMap;
    static {
        nameToPrincipalMap = new HashMap<>();
        int dummyIdSequence = 1;
        for (UserNameFixture userNameFixture : UserNameFixture.values()) {
            dummyIdSequence += 1;
            String dummyId = String.format("999999%d", dummyIdSequence);
            Principal.Builder builder = Principal.Builder.create(userNameFixture.name());
            builder.setActive(true);
            builder.setEntityId(dummyId);
            builder.setEntityId(dummyId);
            builder.setObjectId(dummyId);
            builder.setVersionNumber(1L);
            Principal principal = builder.build();
            nameToPrincipalMap.put(userNameFixture.name(), principal);
        }
    }


    private BusinessObjectService businessObjectService;
    private LdapPrincipalDao ldapPrincipalDao;


    @Override
    public Principal getPrincipalByPrincipalName(String principalName) throws RiceIllegalArgumentException {
        Principal principal = nameToPrincipalMap.get(principalName);
        if (principal == null) {
            throw new RuntimeException("principalName not found in UserNameFixture: " + principalName);
        }

        return principal;
    }


    @Override
    public List<EntityDefault> findEntityDefaults(Map<String, String> map, boolean b) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<EntityDefault> getEntityDefaultsByPrincipalIds(Collection<String> collection) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<EntityDefault> getEntityDefaultsByCriteria(Map<String, List<String>> map) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityDefaultQueryResults findEntityDefaults(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityQueryResults findEntities(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Entity getEntity(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Entity getEntityByPrincipalId(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Entity getEntityByPrincipalName(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Entity getEntityByEmployeeId(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Entity createEntity(Entity entity) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Entity updateEntity(Entity entity) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Entity inactivateEntity(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityDefault getEntityDefault(String s) throws RiceIllegalArgumentException {
        //EntityDefault.Builder entityDefault = EntityDefault.Builder.create();
        System.out.println("getEntityDefault() called with arg: " + s);
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityDefault getEntityDefaultByPrincipalId(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityDefault getEntityDefaultByPrincipalName(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityDefault getEntityDefaultByEmployeeId(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Principal getPrincipal(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<Principal> getPrincipals(List<String> list) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<Principal> getPrincipalsByEntityId(String s) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<Principal> getPrincipalsByEmployeeId(String s) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Principal getPrincipalByPrincipalNameAndPassword(String s, String s1) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Principal addPrincipalToEntity(Principal principal) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Principal updatePrincipal(Principal principal) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Principal inactivatePrincipal(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Principal inactivatePrincipalByName(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityTypeContactInfo addEntityTypeContactInfoToEntity(EntityTypeContactInfo entityTypeContactInfo) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityTypeContactInfo updateEntityTypeContactInfo(EntityTypeContactInfo entityTypeContactInfo) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityTypeContactInfo inactivateEntityTypeContactInfo(String s, String s1) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityAddress addAddressToEntity(EntityAddress entityAddress) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityAddress updateAddress(EntityAddress entityAddress) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityAddress inactivateAddress(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEmail addEmailToEntity(EntityEmail entityEmail) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEmail updateEmail(EntityEmail entityEmail) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEmail inactivateEmail(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityPhone addPhoneToEntity(EntityPhone entityPhone) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityPhone updatePhone(EntityPhone entityPhone) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityPhone inactivatePhone(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityExternalIdentifier addExternalIdentifierToEntity(EntityExternalIdentifier entityExternalIdentifier) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityExternalIdentifier updateExternalIdentifier(EntityExternalIdentifier entityExternalIdentifier) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityAffiliation addAffiliationToEntity(EntityAffiliation entityAffiliation) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityAffiliation updateAffiliation(EntityAffiliation entityAffiliation) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityAffiliation inactivateAffiliation(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityNamePrincipalName getDefaultNamesForPrincipalId(String s) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public Map<String, EntityNamePrincipalName> getDefaultNamesForPrincipalIds(List<String> list) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityPrivacyPreferences getPrivacyPreferencesForPrincipalId(String s) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityName addNameToEntity(EntityName entityName) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityName updateName(EntityName entityName) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityName inactivateName(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEmployment addEmploymentToEntity(EntityEmployment entityEmployment) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEmployment updateEmployment(EntityEmployment entityEmployment) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEmployment inactivateEmployment(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityBioDemographics addBioDemographicsToEntity(EntityBioDemographics entityBioDemographics) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityBioDemographics updateBioDemographics(EntityBioDemographics entityBioDemographics) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityPrivacyPreferences getEntityPrivacyPreferences(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityPrivacyPreferences addPrivacyPreferencesToEntity(EntityPrivacyPreferences entityPrivacyPreferences) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityPrivacyPreferences updatePrivacyPreferences(EntityPrivacyPreferences entityPrivacyPreferences) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityCitizenship addCitizenshipToEntity(EntityCitizenship entityCitizenship) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityCitizenship updateCitizenship(EntityCitizenship entityCitizenship) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityCitizenship inactivateCitizenship(String s) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEthnicity addEthnicityToEntity(EntityEthnicity entityEthnicity) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityEthnicity updateEthnicity(EntityEthnicity entityEthnicity) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityResidency addResidencyToEntity(EntityResidency entityResidency) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityResidency updateResidency(EntityResidency entityResidency) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityVisa addVisaToEntity(EntityVisa entityVisa) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityVisa updateVisa(EntityVisa entityVisa) throws RiceIllegalArgumentException, RiceIllegalStateException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getEntityType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllEntityTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getAddressType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllAddressTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityAffiliationType getAffiliationType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<EntityAffiliationType> findAllAffiliationTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getCitizenshipStatus(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllCitizenshipStatuses() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getEmploymentType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllEmploymentTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getEmploymentStatus(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllEmploymentStatuses() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public EntityExternalIdentifierType getExternalIdentifierType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<EntityExternalIdentifierType> findAllExternalIdendtifierTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getNameType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllNameTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getPhoneType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllPhoneTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public CodedAttribute getEmailType(String s) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<CodedAttribute> findAllEmailTypes() {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public PrincipalQueryResults findPrincipals(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    @Override
    public List<EntityDefault> findEntityDefaultsByCriteriaMap(Map<String, String> map) {
        throw new NotImplementedException("This mock method has not yet been implemented.");
    }


    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }


    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    public LdapPrincipalDao getLdapPrincipalDao() {
        return ldapPrincipalDao;
    }


    public void setLdapPrincipalDao(LdapPrincipalDao ldapPrincipalDao) {
        this.ldapPrincipalDao = ldapPrincipalDao;
    }
}
