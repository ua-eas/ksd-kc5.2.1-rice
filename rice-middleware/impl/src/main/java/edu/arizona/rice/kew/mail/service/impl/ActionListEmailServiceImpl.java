package edu.arizona.rice.kew.mail.service.impl;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;

/*
 * Class to make jobs durable in order to be compatible with Spring version
 * bump to 4.3.7 during UA upgrade from KualiCo tags 2017-03-23 to target
 * 2017-05-11 (ua-release-50)
 */
public class ActionListEmailServiceImpl extends org.kuali.rice.kew.mail.service.impl.ActionListEmailServiceImpl {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionListEmailServiceImpl.class);

    @Override
    protected void addJobToScheduler(JobDetail jobDetail) throws SchedulerException {
        if (jobDetail instanceof JobDetailImpl) {
            // This is the whole reason for extending the class
            ((JobDetailImpl)jobDetail).setDurability(true);

            // Delegate to super in case implementation changes
            super.addJobToScheduler(jobDetail);

        } else {
            // If implementation changed or we get null, make some noise
            LOG.error("JobDetail was null or not the expected JobDetailImpl: " + (jobDetail == null ? "null" : jobDetail.getClass().toString()));
        }
    }
}
