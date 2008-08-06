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
package org.kuali.rice.kcb.services.impl;

import java.util.Collection;

import org.junit.Test;
import org.kuali.rice.kcb.GlobalKCBServiceLocator;
import org.kuali.rice.kcb.bo.RecipientDelivererConfig;
import org.kuali.rice.kcb.service.RecipientPreferenceService;
import org.kuali.rice.kcb.test.KCBTestCase;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.AssertThrows;

/**
 * Tests persisting RecipientDelivererConfig 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
@BaselineMode(Mode.ROLLBACK)
public class RecipientDelivererConfigTest extends KCBTestCase {
    //private RecipientDelivererConfig CFG;

    private RecipientPreferenceService prefsvc;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    
        prefsvc = GlobalKCBServiceLocator.getInstance().getRecipientPreferenceService();

//        CFG = new RecipientDelivererConfig();
//        CFG.setRecipientId("user1");
//        CFG.setDelivererName("mock");
//        CFG.setChannel("channel1");

        prefsvc.saveRecipientDelivererConfig("user1", "mock", new String[] { "channel1" });
    }

    @Test
    public void testCreate() throws Exception {
        prefsvc.saveRecipientDelivererConfig("user1", "mock", new String[] { "channel2" });
        
        Collection<RecipientDelivererConfig> deliverers = prefsvc.getDeliverersForRecipient("user1");
        assertEquals(2, deliverers.size());
    }

    @Test
    public void testDelete() throws Exception {
        prefsvc.removeRecipientDelivererConfigs("user1");
        
        assertEquals(0, prefsvc.getDeliverersForRecipient("user1").size());
    }

    @Test
    public void testDuplicateCreate() throws Exception {
        // duplicate channel/deliverer entry
        new AssertThrows(DataIntegrityViolationException.class) {
            public void test() throws Exception {
                prefsvc.saveRecipientDelivererConfig("user1", "mock", new String[] { "channel1" });
            }
        }.runTest();
    }

    @Test
    public void testInvalidUpdate() throws Exception {
        // null channel
        new AssertThrows(DataIntegrityViolationException.class) {
            public void test() throws Exception {
                prefsvc.saveRecipientDelivererConfig("user1", null, new String[] { "channel2" });
            }
        }.runTest();
    }
    
    @Test
    public void testGetDeliverersForRecipientAndChannel() {
        Collection<RecipientDelivererConfig> cfgs = prefsvc.getDeliverersForRecipientAndChannel("user1", "channel1");
        assertEquals(1, cfgs.size());
        assertEquals("mock", cfgs.iterator().next().getDelivererName());
    }
}
