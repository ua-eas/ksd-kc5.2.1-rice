/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package edu.arizona.kim.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.kim.impl.identity.PersonServiceImpl;
import org.kuali.rice.kim.service.LdapIdentityService;

// UAF-6.0 upgrade
public class UaPersonService extends PersonServiceImpl {
	private static final Logger LOG = LogManager.getLogger(UaPersonService.class);
	private LdapIdentityService ldapIdentityService;

	protected static final String EDS_ACTIVE_STATUS_KEY = "principals.active";

	@Override
	protected List<Person> findPeopleInternal(Map<String, String> criteria, boolean unbounded) {
		if (criteria.containsKey(KIMPropertyConstants.Person.ACTIVE)) {
			String value = criteria.get(KIMPropertyConstants.Person.ACTIVE);
			criteria.remove(KIMPropertyConstants.Person.ACTIVE);
			criteria.put(EDS_ACTIVE_STATUS_KEY, value);
		}

		List<EntityDefault> entities = ldapIdentityService.findEntityDefaults(criteria, unbounded);

        return convertEntityDefaultsToPersons(entities);
	}

	public void setLdapIdentityService(LdapIdentityService ldapIdentityService) {
		this.ldapIdentityService = ldapIdentityService;
	}

	// UAF-6 - Performance improvements to improve user experience for AWS
	// deployment
	@Override
	public List<Person> getPeople(Collection<String> principalIds) {
		List<Person> retval = new ArrayList<Person>();
		List<EntityDefault> entityDefaults = ldapIdentityService.getEntityDefaultsByPrincipalIds(principalIds);
		for (EntityDefault ed : entityDefaults) {
			if ((ed.getPrincipals() != null) && !ed.getPrincipals().isEmpty()) {
				retval.add(convertEntityToPerson(ed, ed.getPrincipals().get(0)));
			}
		}

		return retval;
	}

	@Override
	public Person getPersonByPrincipalName(String principalName) {
		Person retval = super.getPersonByPrincipalName(principalName);

		if (retval == null) {
			EntityDefault ed = ldapIdentityService.getEntityDefaultByPrincipalName(principalName);
			if (ed != null) {
				retval = convertEntityToPerson(ed, ed.getPrincipals().get(0));
			}
		}

		return retval;
	}

    @Override
    public List<Person> getPersonsByCriteria(Map<String, List<String>> criteria) {
        List<EntityDefault> entities = ldapIdentityService.getEntityDefaultsByCriteria(criteria);

        return convertEntityDefaultsToPersons(entities);
    }


    private List<Person> convertEntityDefaultsToPersons(List<EntityDefault> entityDefaults) {
        List<Person> personList = new ArrayList<>();

        if (!entityDefaults.isEmpty()) {
            for (EntityDefault e : entityDefaults) {
                if (e != null) {
                    // get to get all principals for the identity as well
                    for (Principal p : e.getPrincipals()) {
                        PersonImpl person = convertEntityToPerson(e, p);
                        person.setActive(e.isActive());
                        personList.add(person);
                    }
                }
            }
        }

        return personList;
    }

}
