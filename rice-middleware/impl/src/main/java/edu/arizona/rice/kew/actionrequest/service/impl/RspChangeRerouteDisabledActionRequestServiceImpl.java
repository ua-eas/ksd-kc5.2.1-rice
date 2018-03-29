package edu.arizona.rice.kew.actionrequest.service.impl;

import java.util.Set;

import org.kuali.rice.kew.actionrequest.service.impl.ActionRequestServiceImpl;
import org.apache.log4j.Logger;

/**
 * This class overrides ActionRequestServiceImpl to disable edoc reroute by
 * re-implementing updateActionRequestsForResponsibilityChange
 */
public class RspChangeRerouteDisabledActionRequestServiceImpl extends ActionRequestServiceImpl {

	private static final Logger log = Logger.getLogger(RspChangeRerouteDisabledActionRequestServiceImpl.class);

	@Override
	public void updateActionRequestsForResponsibilityChange(Set<String> responsibilityIds) {

		log.info("UA Implementation to skip rerouting/requeing edoc for Responsibility change processing.");

	}

}
