/*
 * Copyright 2005-2007 The Kuali Foundation
 * 
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
package org.kuali.rice.kew.routing;

import org.junit.Test;
import org.kuali.rice.kew.dto.NetworkIdDTO;
import org.kuali.rice.kew.exception.InvalidActionTakenException;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kew.service.WorkflowDocument;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;


public class RoutingToInactiveWorkgroupTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("RoutingConfig.xml");
    }
    
    @Test public void testRoutingToInactiveWorkgroup() throws Exception {
        WorkflowDocument doc = new WorkflowDocument(new NetworkIdDTO("rkirkend"), "InactiveWorkgroupDocType");
        try {
            doc.routeDocument("");
            fail("document should have thrown routing exception");
        } catch (Exception e) {
            
        }
        TestUtilities.getExceptionThreader().join();//wait for doc to go into exception routing
        doc = new WorkflowDocument(new NetworkIdDTO("rkirkend"), doc.getRouteHeaderId());
        assertTrue("Document should be in exception routing because workgroup is inactive", doc.stateIsException());

        try {
            doc.routeDocument("routing a document that is in exception routing");
            fail("Succeeded in routing document that is in exception routing");
        } catch (InvalidActionTakenException iate) {
            log.info("Expected exception occurred: " + iate);
        } catch (WorkflowException we) {
            fail("Attempt at routing document in exception routing succeeded, when it should have been an InvalidActionTakenException");
        }
    }
}