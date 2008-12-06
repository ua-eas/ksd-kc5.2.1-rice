<%--
 Copyright 2005-2006 The Kuali Foundation.

 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.opensource.org/licenses/ecl1.php

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>


<kul:documentPage
	showDocumentInfo="false"
	htmlFormAction="identityManagementPersonDocument"
	documentTypeName="IdentityManagementPersonDocument"
	renderMultipart="false"
	showTabButtons="true"
	auditCount="0">

 	<kul:hiddenDocumentFields />
	<kul:documentOverview editingMode="${KualiForm.editingMode}" />
	<kim:personOverview />
	<kim:personContact />
	<kim:personPrivacy />
	<kim:personMembership />
		
	<kul:panelFooter />
	<kul:documentControls transactionalDocument="false" />

</kul:documentPage>
