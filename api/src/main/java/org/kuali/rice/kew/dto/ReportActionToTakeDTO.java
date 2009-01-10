/*
 * Copyright 2005-2006 The Kuali Foundation.
 * 
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
package org.kuali.rice.kew.dto;

import java.io.Serializable;

/**
 * A transport object representing an action a user might take
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class ReportActionToTakeDTO implements Serializable {
	private static final long serialVersionUID = 5212455086079117671L;

	private String actionToPerform;
    private String principalId;
    private String nodeName;

    public ReportActionToTakeDTO() {
    }

    public ReportActionToTakeDTO(String actionToPerform, String principalId, String nodeName) {
        this.actionToPerform = actionToPerform;
        this.principalId = principalId;
        this.nodeName = nodeName;
    }

	public String getActionToPerform() {
		return actionToPerform;
	}

	public void setActionToPerform(String actionToPerform) {
		this.actionToPerform = actionToPerform;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getPrincipalId() {
		return this.principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}	

}