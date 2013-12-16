/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.uif.lifecycle;

import java.util.Map.Entry;
import java.util.Queue;

import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecycle.LifecycleEvent;
import org.kuali.rice.krad.uif.lifecycle.initialize.AssignIdsTask;
import org.kuali.rice.krad.uif.lifecycle.initialize.HelperCustomInitializeTask;
import org.kuali.rice.krad.uif.util.LifecycleElement;

/**
 * Lifecycle phase processing task for initializing a component.
 * 
 * <p>
 * During the initialize phase each component of the tree is invoked to setup state based on the
 * configuration and request options.
 * </p>
 * 
 * <p>
 * The initialize phase is only called once per <code>View</code> lifecycle
 * </p>
 * 
 * <p>
 * Note the <code>View</code> instance also contains the context Map that was created based on the
 * parameters sent to the view service
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class InitializeComponentPhase extends ViewLifecyclePhaseBase {
    
    /**
     * {@inheritDoc}
     * @return UifConstants.ViewPhases.INITIALIZE
     */
    @Override
    public String getViewPhase() {
        return UifConstants.ViewPhases.INITIALIZE;
    }

    /**
     * {@inheritDoc}
     * return UifConstants.ViewStatus.CREATED
     */
    @Override
    public String getStartViewStatus() {
        return UifConstants.ViewStatus.CREATED;
    }

    /**
     * {@inheritDoc}
     * @return UifConstants.ViewStatus.INITIALIZED
     */
    @Override
    public String getEndViewStatus() {
        return UifConstants.ViewStatus.INITIALIZED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LifecycleEvent getEventToNotify() {
        return null;
    }

    /**
     * Queues initialization phase tasks.
     * {@inheritDoc}
     */
    @Override
    protected void initializePendingTasks(Queue<ViewLifecycleTask> tasks) {
        tasks.offer(LifecycleTaskFactory.getTask(AssignIdsTask.class, this));
        getElement().initializePendingTasks(this, tasks);
        tasks.offer(LifecycleTaskFactory.getTask(HelperCustomInitializeTask.class, this));
    }

    /**
     * Define all nested lifecycle components, and component prototypes, as successors.
     * {@inheritDoc}
     */
    @Override
    protected void initializeSuccessors(Queue<ViewLifecyclePhase> successors) {
        LifecycleElement element = getElement();
        Object model = getModel();

        // initialize nested components
        int index = 0;

        for (Entry<String, LifecycleElement> nestedElementEntry :
                ViewLifecycleUtils.getElementsForLifecycle(element, getViewPhase()).entrySet()) {
            String path = getPath();
            String nestedPath = (path == null ? "" : path + ".") + nestedElementEntry.getKey();
            LifecycleElement nestedElement = nestedElementEntry.getValue();

            if (nestedElement != null && !nestedElement.isInitialized()) {
                successors.offer(LifecyclePhaseFactory.initialize(
                        nestedElement, model, index++, nestedPath, null, null));
            }
        }
    }

}
