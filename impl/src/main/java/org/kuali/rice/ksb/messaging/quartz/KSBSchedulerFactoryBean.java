/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.kuali.rice.ksb.messaging.quartz;

import javax.sql.DataSource;

import org.kuali.rice.core.Core;
import org.kuali.rice.core.config.ConfigurationException;
import org.kuali.rice.ksb.services.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * An implementation of the Quartz SchedulerFactoryBean which uses a database-backed quartz if the useQuartzDatabase property
 * is set.
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class KSBSchedulerFactoryBean extends SchedulerFactoryBean {

    private PlatformTransactionManager jtaTransactionManager;
    private boolean transactionManagerSet = false;
    private boolean nonTransactionalDataSourceIsNull = true;

    @Override
    protected Scheduler createScheduler(SchedulerFactory schedulerFactory, String schedulerName) throws SchedulerException {
        if (Core.getCurrentContextConfig().getObject(KSBConstants.INJECTED_EXCEPTION_MESSAGE_SCHEDULER_KEY) != null) {
            try {
                Scheduler scheduler = (Scheduler) Core.getCurrentContextConfig().getObject(KSBConstants.INJECTED_EXCEPTION_MESSAGE_SCHEDULER_KEY);
                scheduler.addJobListener(new MessageServiceExecutorJobListener());
                return scheduler;
            } catch (Exception e) {
                throw new ConfigurationException(e);
            }
        }
        return super.createScheduler(schedulerFactory, schedulerName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        boolean useQuartzDatabase = new Boolean(Core.getCurrentContextConfig().getProperty(KSBConstants.USE_QUARTZ_DATABASE));
        if (useQuartzDatabase) {
            // require a transaction manager
            if (jtaTransactionManager == null) {
                throw new ConfigurationException("No jta transaction manager was configured for the KSB Quartz Scheduler");
            }
            // since transaction manager is required... require a non transactional datasource
            DataSource nonTransDataSource = KSBServiceLocator.getMessageNonTransactionalDataSource();
            if (nonTransDataSource == null) {
                throw new ConfigurationException("No non-transactional data source was found but is required for the KSB Quartz Scheduler");
            }
            setTransactionManager(jtaTransactionManager);
            setDataSource(KSBServiceLocator.getMessageDataSource());
            setNonTransactionalDataSource(nonTransDataSource);
        }
        if (transactionManagerSet && nonTransactionalDataSourceIsNull) {
            throw new ConfigurationException("A valid transaction manager was set but no non-transactional data source was found");
        }
        super.afterPropertiesSet();
    }

    /**
     * This is to work around an issue with the GRL when you've got more than one module with a bean named
     * "transactionManager".
     * 
     * @param jtaTransactionManager
     */
    public void setJtaTransactionManager(PlatformTransactionManager jtaTransactionManager) {
        this.jtaTransactionManager = jtaTransactionManager;
    }
    
    /**
     * This overridden method is used simply to keep track of whether the transactionManager property has been set
     * 
     * @see org.springframework.scheduling.quartz.SchedulerFactoryBean#setTransactionManager(org.springframework.transaction.PlatformTransactionManager)
     */
    @Override
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        transactionManagerSet = transactionManager != null;
        super.setTransactionManager(transactionManager);
    }
    
    /**
     * This overridden method is used to keep track of whether the non transactional data source is null
     * 
     * @see org.springframework.scheduling.quartz.SchedulerFactoryBean#setNonTransactionalDataSource(javax.sql.DataSource)
     */
    @Override
    public void setNonTransactionalDataSource(DataSource nonTransactionalDataSource) {
        nonTransactionalDataSourceIsNull = nonTransactionalDataSource == null;
        super.setNonTransactionalDataSource(nonTransactionalDataSource);
    }
}