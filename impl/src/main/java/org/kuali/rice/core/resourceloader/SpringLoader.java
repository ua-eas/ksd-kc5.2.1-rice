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
package org.kuali.rice.core.resourceloader;

import org.apache.log4j.Logger;
import org.kuali.rice.core.lifecycle.BaseLifecycle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.namespace.QName;
import java.util.Set;


/**
 * ServiceLocator that starts and wraps the primary workflow Spring Application Context.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class SpringLoader extends BaseLifecycle implements ServiceLocator {

    private static final Logger LOG = Logger.getLogger(SpringLoader.class);

    /**
     * The default top-level Spring configuration resource name
     */
    public static final String DEFAULT_SPRING_FILE = "Spring.xml";

    /* Conditions are used to ensure state integrity.  Various methods that modify state
     * are already synchronized so in some places these are redundant.  At some point we
     * should switch to java.util.concurrent Lock and Condition classes and implement this
     * correctly.
     */

    private static final SpringLoader INSTANCE = new SpringLoader();
    
    /**
     * Separator character for list of Spring files.
     * <p>Value is ','
     */
    public static final String SPRING_SEPARATOR_CHARACTER = ",";

    /**
     * Condition that indicates whether Spring has completed initialization
     */
    private final ContextualConfigLock SPRING_INIT_COMPLETE= new ContextualConfigLock("Spring has completed initialization");
    /**
     * Condition that indicates whether Spring has started initialization
     */
    private final ContextualConfigLock SPRING_INIT_STARTED = new ContextualConfigLock("Spring has started initialization");

    private String contextFiles = DEFAULT_SPRING_FILE;
    private static Set<String> suppressedServices = null;

    /**
     * The Spring context
     */
    private ConfigurableApplicationContext appContext;

    public static SpringLoader getInstance() {
    	return INSTANCE;
    }

    public synchronized void start() throws Exception {
    	initializeAppContexts();
    	super.start();
    }

    public synchronized void stop() throws Exception {
    	close();
    	super.stop();
    }

    /**
     * Initializes Spring with the specified context resource name
     * @param rootContextFile the Spring context resource name
     */
    protected void initializeAppContexts() {
        if (SPRING_INIT_STARTED.hasFired() || SPRING_INIT_COMPLETE.hasFired()) {
        	return;
        }
        SPRING_INIT_STARTED.fire();

        LOG.info("Initializing Spring from resources: ");
        try {
            if (!getContextFiles().contains(SPRING_SEPARATOR_CHARACTER)) {
                LOG.info( getContextFiles() );
                appContext = new ClassPathXmlApplicationContext(getContextFiles());
            } else {
                String[] contextFileLocations = getContextFiles().split( SPRING_SEPARATOR_CHARACTER );
                LOG.info( contextFileLocations );
                appContext = new ClassPathXmlApplicationContext( contextFileLocations );
            }
        	appContext.getBeanFactory().preInstantiateSingletons();
        } finally {
            // if an exception occurs we need to signal that init is complete
            // even though it is in error, so that close will be allowed
            SPRING_INIT_COMPLETE.fire();
        }
    }

	public Object getService(QName qname) {
		return getBean(qname.toString());
	}

    /**
     * Obtains a bean from Spring
     *
     * @param serviceName the name of the bean
     * @return a bean from Spring
     */
    public Object getBean(String serviceName) {
        initializeAppContexts();

        if (appContext == null) {
            throw new RuntimeException("Spring not initialized properly.  Initialization has completed and the application context is null.");
        }
        if (appContext.containsBean(serviceName)) {
        	return appContext.getBean(serviceName);
        }
        return null;
    }

    public Class getType(String beanName) {
    	initializeAppContexts();
        if (appContext == null) {
            throw new RuntimeException("Spring not initialized properly.  Initialization has completed and the application context is null.");
        }
        return appContext.getType(beanName);
    }

    public boolean isSingleton(String beanName) {
    	initializeAppContexts();
        if (appContext == null) {
            throw new RuntimeException("Spring not initialized properly.  Initialization has completed and the application context is null.");
        }
        return appContext.isSingleton(beanName);
    }

    /**
     * Closes the Spring context if initialization has at least started.
     * If initialization has NOT started, this method returns immediately.
     * If initialization HAS started, then it awaits completion before closing
     */
    public synchronized void close() {
        if (!SPRING_INIT_STARTED.hasFired()) {
            return;
        }
        // this is not technically necessary at this time because both close and initialize* methods
        // are synchronized so initialization should always be complete if it has been started;
        // NOTE: although - let's think about error cases...if exception is thrown from init, then
        // SPRING_INIT_COMPLETE may never have fired - that is a good thing probably, but maybe it warrants
        // a separate SPRING_INIT_FAILED condition; don't want to get too complicated though
        SPRING_INIT_COMPLETE.await();
        if (appContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext)appContext).close();
        }
        SPRING_INIT_STARTED.reset();
        SPRING_INIT_COMPLETE.reset();
        this.suppressedServices = null;
    }

    /**
     * Returns the Spring context, waiting for initialization to start and complete
     * if it hasn't already
     * @return the intiailized Spring context
     */
    public ApplicationContext getApplicationContext() {
        SPRING_INIT_COMPLETE.await();
        return appContext;
    }

	public String getContents(String indent, boolean servicePerLine) {
		String contents = indent + "SpringLoader " + this + " services =";

		for (String beanName : appContext.getBeanDefinitionNames()) {
			if (servicePerLine) {
				contents += indent + "+++" + beanName + "\n";
			} else {
				contents += beanName + ", ";
			}
		}

		return contents;
	}

	public String getContextFiles() {
		return contextFiles;
	}

	public void setContextFiles(String contextFiles) {
		this.contextFiles = contextFiles;
	}

}