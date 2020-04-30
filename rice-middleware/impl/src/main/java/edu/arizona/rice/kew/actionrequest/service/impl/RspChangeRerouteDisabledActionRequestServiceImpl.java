package edu.arizona.rice.kew.actionrequest.service.impl;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kuali.rice.kew.actionrequest.service.impl.ActionRequestServiceImpl;


/**
 * This class overrides ActionRequestServiceImpl to disable edoc reroute by
 * re-implementing updateActionRequestsForResponsibilityChange
 */
public class RspChangeRerouteDisabledActionRequestServiceImpl extends ActionRequestServiceImpl {

	private static final Logger LOG = LogManager.getLogger(RspChangeRerouteDisabledActionRequestServiceImpl.class);

	@Override
	public void updateActionRequestsForResponsibilityChange(Set<String> responsibilityIds) {

		LOG.info("UA Implementation to skip rerouting/requeing edoc for Responsibility change processing.");

	}

}
