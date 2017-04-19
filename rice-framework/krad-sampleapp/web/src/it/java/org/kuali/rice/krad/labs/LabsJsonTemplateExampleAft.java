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
package org.kuali.rice.krad.labs;

import org.junit.Test;
import org.kuali.rice.testtools.selenium.WebDriverLegacyITBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LabsJsonTemplateExampleAft extends WebDriverLegacyITBase {

    /**
     * /kr-krad/labs?methodToCall=jsonExample
     */
    public static final String BOOKMARK_URL = "/kr-krad/labs?methodToCall=jsonExample";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
    	waitAndClickByLinkText("JSON Template Example");
    }

    protected void testDemoJsonTemplateExample() throws InterruptedException {
    	assertTextPresent(new String[] {"dataStoreConnUsageLimit", "maxUrlLength"});
    }

    @Test
    public void testDemoJsonTemplateExampleBookmark() throws Exception {
    	testDemoJsonTemplateExample();
        passed();
    }

    @Test
    public void testDemoJsonTemplateExampleNav() throws Exception {
    	testDemoJsonTemplateExample();
        passed();
    }
}
