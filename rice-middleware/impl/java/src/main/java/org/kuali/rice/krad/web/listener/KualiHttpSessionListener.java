/**
 * Copyright 2005-2015 The Kuali Foundation
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
package org.kuali.rice.krad.web.listener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.document.authorization.PessimisticLock;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

/**
 * This class is used to handle session timeouts where {@link PessimisticLock} objects should
 * be removed from a document 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiHttpSessionListener implements HttpSessionListener {

    private static final Logger LOG = Logger.getLogger(KualiHttpSessionListener.class);

    /**
     *  HttpSession hook for additional setup method when sessions are created
     *
     * @param se - the HttpSessionEvent containing the session
     *
     *  @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // no operation required at this time
    }

    /**
     * HttpSession hook for additional cleanup when sessions are destroyed.
     *
     * UAF-2097: Ensure already destroyed sessions can't cause STE's to fill up logs during
     *           session clearing for memory events. This means return immediately when we see
     *           that we don't have enough info to clear doc locks.
     *
     * @param se - the HttpSessionEvent containing the session
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        UserSession userSession = (UserSession) sessionEvent.getSession().getAttribute(KRADConstants.USER_SESSION_KEY);
        if(ObjectUtils.isNull(userSession)){
            LOG.warn("UserSession already cleared, could not release any open document locks!");
            return;
        }

        String sessionId = userSession.getKualiSessionId();
        if(StringUtils.isBlank(sessionId)){
            LOG.warn("Could not realease document locks for because sessionId is null!");
            return;
        }

        List<PessimisticLock> locks = KRADServiceLocatorWeb.getPessimisticLockService().getPessimisticLocksForSession(sessionId);
        if(ObjectUtils.isNull(locks) || locks.size() <  1){
            // Don't make more work if we don't need to, but not necessarily a warn event since the user
            // might not have any doc locks out
            return;
        }

        Person user = userSession.getActualPerson();
        if(ObjectUtils.isNull(user)){
            // Really paranoid about nulls causing stack traces that fill up logs
            LOG.warn("Person object returned null for sessionId: " + sessionId);
            return;
        }

        // Ok, clear out any document locks
        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, user);

    }

}

