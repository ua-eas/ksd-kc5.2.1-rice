/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.service;

import java.util.List;
import java.util.Map;

import org.kuali.rice.kim.bo.entity.impl.KimEntityImpl;
import org.kuali.rice.kim.bo.group.KimGroup;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kns.datadictionary.AttributeDefinition;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface UiDocumentService {
    public void saveEntityPerson(IdentityManagementPersonDocument identityManagementPersonDocument);
	public void setAttributeEntry(PersonDocumentRole personDocRole);
	public void loadEntityToPersonDoc(IdentityManagementPersonDocument identityManagementPersonDocument, KimEntityImpl kimEntity);
	public void loadGroupToPersonDoc(IdentityManagementPersonDocument identityManagementPersonDocument, List<? extends KimGroup> groups);
}
