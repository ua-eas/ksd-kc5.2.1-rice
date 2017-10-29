/**
 * Copyright 2005-2016 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.dao.impl;

import static org.kuali.rice.core.util.BufferedLogger.debug;
import static org.kuali.rice.core.util.BufferedLogger.info;
import static org.kuali.rice.kns.lookup.LookupUtils.getSearchResultsLimit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.directory.SearchControls;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.dao.LdapPrincipalDao;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.kim.util.Constants;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.ContextMapperCallbackHandler;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;

import edu.arizona.kim.eds.UaEdsConstants;

/**
 * Integrated Data Access via LDAP to EDS. Provides implementation to interface method
 * for using Spring-LDAP to communicate with EDS.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LdapPrincipalDaoImpl implements LdapPrincipalDao {
    protected Constants kimConstants;
    protected UaEdsConstants edsConstants;
    protected LdapTemplate template;
    protected ParameterService parameterService;

    
    protected Map<String, ContextMapper> contextMappers;
    
    public LdapPrincipalDaoImpl() {
    }
                            
    /**
     * In EDS, the principalId, principalName, and entityId will all be the same.
     */
    public Principal getPrincipal(String principalId) {
        if (principalId == null) {
            return null;
        }
        Map<String, Object> criteria = new HashMap();
        criteria.put(getKimConstants().getKimLdapIdProperty(), principalId);
        List<Principal> results = search(Principal.class, criteria);

        if (results.size() > 0) {
            return results.get(0);
        }

        return null;
    }

    /**
     * Assuming he principalId, principalName, and entityId will all be the same.
     */
    public Principal getPrincipalByName(String principalName) {
        if (principalName == null) {
            return null;
        }
        Map<String, Object> criteria = new HashMap();
        criteria.put(getKimConstants().getKimLdapNameProperty(), principalName);
        List<Principal> results = search(Principal.class, criteria);

        if (results.size() > 0) {
            return results.get(0);
        }
        
        return null;
    }

    public Principal getPrincipalByEmployeeId(String employeeId) {
        if (StringUtils.isBlank(employeeId)) {
            return null;
        }

        Map<String, Object> criteria = new HashMap();
        String key = getEdsConstants().getEmplIdContextKey();
        criteria.put(key, employeeId);
        List<Principal> results = search(Principal.class, criteria);

        if (results.size() > 0) {
            return results.get(0);
        }

        return null;
    }

    public <T> List<T> search(Class<T> type, Map<String, Object> criteria) {
        AndFilter filter = new AndFilter();
        
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            //attempting to handle null values to prevent NPEs in this code.
            if (entry.getValue() == null) {
                entry.setValue("null");
            }
            if (entry.getValue() instanceof Iterable) {
                OrFilter orFilter = new OrFilter();
                for (String value : (Iterable<String>) entry.getValue()) {
                    if (value.startsWith("!")) {
                        orFilter.or(new NotFilter(new LikeFilter(entry.getKey(), value.substring(1))));
                    } else {
                        orFilter.or(new LikeFilter(entry.getKey(), value));
                    }
                }
                filter.and(orFilter);
            }
            else {
                if (((String)entry.getValue()).startsWith("!")) {
                    filter.and(new NotFilter(new LikeFilter(entry.getKey(), ((String)entry.getValue()).substring(1))));
                } else {
                    filter.and(new LikeFilter(entry.getKey(), (String) entry.getValue()));
                }
            }
        };
        
        info("Using filter ", filter.encode());
        
        List retval = new ArrayList();
        
        // UAF-6 defensice programming - no search with empty filter
        if (StringUtils.isNotBlank(filter.encode())) {
            debug("Looking up mapper for ", type.getSimpleName());
            final ContextMapper customMapper = contextMappers.get(type.getSimpleName());

            ContextMapperCallbackHandler callbackHandler = new CustomContextMapperCallbackHandler(customMapper);

            getLdapTemplate().search(DistinguishedName.EMPTY_PATH, 
                                         filter.encode(), 
                                         getSearchControls(), callbackHandler);

            retval = callbackHandler.getList();
        }
        
        return retval;
    }

    protected SearchControls getSearchControls() {
        SearchControls retval = new SearchControls();
        retval.setCountLimit(getSearchResultsLimit(PersonImpl.class).longValue());
        retval.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return retval;
    }

	/**
     * FIND entity objects based on the given criteria. 
     * 
     * @param entityId of user/person to grab entity information for
     * @return {@link Entity}
     */
	public Entity getEntity(String entityId) {
	    if (entityId == null) {
	        return null;
	    }
        Map<String, Object> criteria = new HashMap();
        criteria.put(getKimConstants().getKimLdapIdProperty(), entityId);

        List<Entity> results = search(Entity.class, criteria);

        debug("Got results from info lookup ", results, " with size ", results.size());

        if (results.size() > 0) {
            return results.get(0);
        }
        
        return null;
    }
	
	/**
	 * Fetches full entity info, populated from EDS, based on the Entity's principal id
	 * @param principalId the principal id to look the entity up for
	 * @return the corresponding entity info
	 */
	public Entity getEntityByPrincipalId(String principalId) {
	    if (principalId == null) {
	        return null;
	    }
	   final Principal principal = getPrincipal(principalId);
	   if (principal != null && !StringUtils.isBlank(principal.getEntityId())) {
	       return getEntity(principal.getEntityId());
	   }
	   return null;
	}

	public EntityDefault getEntityDefault(String entityId) {
	    if (entityId == null) {
	        return null;
	    }
        Map<String, Object> criteria = new HashMap();
        criteria.put(getKimConstants().getKimLdapIdProperty(), entityId);

        List<EntityDefault> results = search(EntityDefault.class, criteria);

        debug("Got results from info lookup ", results, " with size ", results.size());

        if (results.size() > 0) {
            return results.get(0);
        }
        
        return null;
    }

    /**
     * entityid and principalId are treated as the same.
     * 
     * @see #getEntityDefaultInfo(String)
     */
	public EntityDefault getEntityDefaultByPrincipalId(String principalId) {
        return getEntityDefault(principalId);
    }

	public EntityDefault getEntityDefaultByPrincipalName(String principalName) {
        Map<String, Object> criteria = new HashMap();
        criteria.put(getKimConstants().getKimLdapNameProperty(), principalName);

        List<EntityDefault> results = search(EntityDefault.class, criteria);
        if (results.size() > 0) {
            return results.get(0);
        }
        
        return null;
    }

    public EntityDefault getEntityDefaultByEmployeeId(String employeeId) {
        Map<String, Object> criteria = new HashMap();
        String key = getEdsConstants().getEmplIdContextKey();
        criteria.put(key, employeeId);

        List<EntityDefault> results = search(EntityDefault.class, criteria);
        if (results.size() > 0) {
            return results.get(0);
        }

        return null;
    }

    @Override
    public List<EntityDefault> getEntityDefaultsByCriteria(Map<String, List<String>> criteria) {
	    Map<String, Object> convertedMap = new HashMap<>();
        for (String key : criteria.keySet()) {
            Object value = criteria.get(key);

            String ldapKey = getLdapAttribute(key);
            if (StringUtils.isBlank(ldapKey)) {
                throw new RuntimeException("KIM value not mapped!: " + key);
            }

            convertedMap.put(ldapKey, value);
        }

        List<EntityDefault> results = search(EntityDefault.class, convertedMap);
        if (results == null || results.size() == 0) {
            results = Collections.emptyList();
        }

        return results;
    }

	public Entity getEntityByPrincipalName(String principalName) {
        Map<String, Object> criteria = new HashMap();
        criteria.put(getKimConstants().getKimLdapNameProperty(), principalName);

        List<Entity> results = search(Entity.class, criteria);
        if (results.size() > 0) {
            return results.get(0);
        }
        
        return null;
    }

    public Entity getEntityByEmployeeId(String employeeId) {
        Map<String, Object> criteria = new HashMap();
        String key = getEdsConstants().getEmplIdContextKey();
        criteria.put(key, employeeId);

        List<Entity> results = search(Entity.class, criteria);
        if (results.size() > 0) {
            return results.get(0);
        }

        return null;
    }

    public List<Principal> getPrincipalsByPrincipalIds(List<String> principalIds) {
        if (principalIds == null) {
            return null;
        }

        Map<String, Object> criteria = new HashMap();
        String key = getKimConstants().getKimLdapIdProperty();
        criteria.put(key, principalIds);
        return search(Principal.class, criteria);
    }


    public List<Principal> getPrincipalsByEmployeeId(String employeeId) {
        if (StringUtils.isBlank(employeeId)) {
            return null;
        }

        Map<String, Object> criteria = new HashMap<String, Object>();
        String key = getEdsConstants().getEmplIdContextKey();
        criteria.put(key, employeeId);

        return  search(Principal.class, criteria);
    }


	public List<EntityDefault> lookupEntityDefault(Map<String,String> searchCriteria, boolean unbounded) {
        Map<String, Object> criteria = getLdapLookupCriteria(searchCriteria);
        return search(EntityDefault.class, criteria);
    }

	public List<String> lookupEntityIds(Map<String,String> searchCriteria) {
        final List<String> results = new ArrayList<String>();
        final Map<String, Object> criteria = getLdapLookupCriteria(searchCriteria);
        
        for (final Entity entity : search(Entity.class, criteria)) {
            results.add(entity.getId());
        }
        
        return results;
    }
    
    /**
     * Converts Kuali Lookup parameters into LDAP query parameters
     * @param searchCriteria kuali lookup info
     * @return {@link Map} of LDAP query info
     */
    protected Map<String, Object> getLdapLookupCriteria(Map<String, String> searchCriteria) {
        Map<String, Object> criteria = new HashMap();
        
        for (Map.Entry<String, String> criteriaEntry : searchCriteria.entrySet()) {
            debug(String.format("Searching with criteria %s = %s", criteriaEntry.getKey(), criteriaEntry.getValue()));
            String valueName = criteriaEntry.getKey();            
            Object value = criteriaEntry.getValue();
            
            // UAF-6 - defensive programming - handle null
            if (value != null) {
                if (!value.equals("*")) {
                    valueName = String.format("%s.%s", criteriaEntry.getKey(), value);
                }

                if (!value.equals("*") && isMapped(valueName)) {
                    value = getLdapValue(valueName);
                    debug(value, " mapped to valueName ", valueName);
                }

                if (isMapped(criteriaEntry.getKey())) {
                    debug(String.format("Setting attribute to (%s, %s)", 
                                        getLdapAttribute(criteriaEntry.getKey()), 
                                        value));
                    final String key = getLdapAttribute(criteriaEntry.getKey());
                    if (!criteria.containsKey(key)) {
                        criteria.put(key, value);
                    }
                }
                else if (criteriaEntry.getKey().equalsIgnoreCase(getKimConstants().getExternalIdProperty())) {
                    criteria.put(getKimConstants().getKimLdapIdProperty(), value);
                }

            }
        }
        return criteria;
    }

	public EntityPrivacyPreferences getEntityPrivacyPreferences(String entityId) {
	    if (entityId == null) {
	        return null;
	    }
        Map<String, Object> criteria = new HashMap();
        criteria.put(getKimConstants().getKimLdapIdProperty(), entityId);

        List<EntityPrivacyPreferences> results = search(EntityPrivacyPreferences.class, criteria);
        if (results.size() > 0) {
            return results.get(0);
        }
        
        return null;
    }
	
    public Map<String, EntityNamePrincipalName> getDefaultNamesForPrincipalIds(List<String> principalIds) {
        Map<String, Object> criteria = new HashMap();
        Map<String, EntityNamePrincipalName> retval = new HashMap();
        criteria.put(getKimConstants().getKimLdapIdProperty(), principalIds);

        List<EntityNamePrincipalName> results = search(EntityNamePrincipalName.class, criteria);

        for (EntityNamePrincipalName nameInfo : results) {
            retval.put(nameInfo.getPrincipalName(), nameInfo);
        }
        return retval;
    }

    public Map<String, EntityNamePrincipalName> getDefaultNamesForEntityIds(List<String> entityIds) {
        return getDefaultNamesForPrincipalIds(entityIds);
    }

    protected Matcher getKimAttributeMatcher(String kimAttribute) {
        String mappedParamValue = getParameterService().getParameterValueAsString(getKimConstants().getParameterNamespaceCode(),
                                                                                  getKimConstants().getParameterDetailTypeCode(),
                                                                                  getKimConstants().getMappedParameterName());

        String regexStr = String.format("(%s|.*;%s)=([^=;]*).*", kimAttribute, kimAttribute);
        debug("Matching KIM attribute with regex ", regexStr);
        Matcher retval = Pattern.compile(regexStr).matcher(mappedParamValue);
        
        if (!retval.matches()) {
            mappedParamValue = getParameterService().getParameterValueAsString(getKimConstants().getParameterNamespaceCode(),
                                                                          getKimConstants().getParameterDetailTypeCode(),
                                                                          getKimConstants().getMappedValuesName());
            retval = Pattern.compile(regexStr).matcher(mappedParamValue);
        }

        return retval;
    }

    protected boolean isMapped(String kimAttribute) {
        debug("Matching " + kimAttribute);
        debug("Does ", kimAttribute, " match? ", getKimAttributeMatcher(kimAttribute).matches());
        return getKimAttributeMatcher(kimAttribute).matches();
    }

    protected String getLdapAttribute(String kimAttribute) {
        Matcher matcher = getKimAttributeMatcher(kimAttribute);
        debug("Does ", kimAttribute, " match? ", matcher.matches());
        if (matcher.matches()) { 
            return matcher.group(2);
        } else {
            return null;
        }
    }

    protected Object getLdapValue(String kimAttribute) {
        Matcher matcher = getKimAttributeMatcher(kimAttribute);
        debug("Does ", kimAttribute, " match? ", matcher.matches());
        if (!matcher.matches()) {
            return null;
        }
        String value = matcher.group(2);

        // If it's actually a list. It can only be a list if there are commas
        if (value.contains(",")) {
            return Arrays.asList(value.split(","));
        }

        return value;
    }

    public void setKimConstants(Constants constants) {
        this.kimConstants = constants;
    }

    public Constants getKimConstants() {
        return kimConstants;
    }


    /*
     * Note: When pulling a constant from this EDS class, there is no need to use the
     *       getLdapAttribute(...) method for conversion, since UaEdsConstants already
     *       has the correct mappings configured via spring, not parms.
     */
    public UaEdsConstants getEdsConstants() {
        return edsConstants;
    }

    public void setEdsConstants(UaEdsConstants edsConstants) {
        this.edsConstants = edsConstants;
    }

    public ParameterService getParameterService() {
        return this.parameterService;
    }

    public void setParameterService(ParameterService service) {
        this.parameterService = service;
    }

    public LdapTemplate getLdapTemplate() {
        return template;
    }

    public void setLdapTemplate(LdapTemplate template) {
        this.template = template;
    }
    
    public Map<String, ContextMapper> getContextMappers() {
        return this.contextMappers;
    }

    public void setContextMappers(final Map<String, ContextMapper> contextMappers) {
        this.contextMappers = contextMappers;
    }

    /**
     * Overrides the existing {@link ContextMapperCallbackHandler} because we want to 
     * intercede when there is invalid results from EDS.
     * 
     * @author Leo Przybylski (przybyls@arizona.edu)
     */
    private static final class CustomContextMapperCallbackHandler extends ContextMapperCallbackHandler {
        public CustomContextMapperCallbackHandler(ContextMapper mapper) {
            super(mapper);
        }
    }
}
