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
package edu.arizona.kim.ldap;

import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;

import edu.arizona.kim.eds.UaEdsRecord;
import edu.arizona.kim.eds.UaEdsRecordFactory;

/**
 * Created by shaloo & kosta on 8/20/15.
 */
public class UaEntityNamePrincipalNameMapper extends UaBaseMapper<EntityNamePrincipalName> {

	private static final Logger LOG = LogManager.getLogger(UaEntityNamePrincipalNameMapper.class);

	private ContextMapper defaultNameMapper;

	@Override
	EntityNamePrincipalName mapDtoFromContext(DirContextOperations context) {
		EntityNamePrincipalName.Builder builder = mapBuilderFromContext(context);
		return builder != null ? builder.build() : null;
	}

	EntityNamePrincipalName.Builder mapBuilderFromContext(DirContextOperations context) {
		UaEdsRecord edsRecord = UaEdsRecordFactory.getEdsRecord(context);
		if (edsRecord == null) {
			LOG.debug("No active and valid EDS eduPersonAffiliation found for context: " + context.getAttributes());
			return null;
		}

		EntityName.Builder defaultName = null;
		try {
			defaultName = (EntityName.Builder) getDefaultNameMapper().mapFromContext(context);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		String principalName = edsRecord.getUid();
		if (principalName == null) {
			LOG.debug("No principal name for context: " + context.getAttributes());
			principalName = "";
		}

		EntityNamePrincipalName.Builder person = EntityNamePrincipalName.Builder.create();
		person.setDefaultName(defaultName);
		person.setPrincipalName(principalName);
		return person;
	}

	public final ContextMapper getDefaultNameMapper() {
		return this.defaultNameMapper;
	}

	public final void setDefaultNameMapper(final ContextMapper argDefaultNameMapper) {
		this.defaultNameMapper = argDefaultNameMapper;
	}

}