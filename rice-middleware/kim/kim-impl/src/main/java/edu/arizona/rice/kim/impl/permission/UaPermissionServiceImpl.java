/*
 *
 * Copyright 2009 State of Arizona Board of Regents
 *
 */
package edu.arizona.rice.kim.impl.permission;

import java.util.Map;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.Entity;

// UAF-7.0 upgrade
public class UaPermissionServiceImpl extends org.kuali.rice.kim.impl.permission.PermissionServiceImpl {
    protected IdentityService identityService;
    
    @Override
    public boolean isAuthorized(String principalId, String namespaceCode, String permissionName, Map<String, String> qualification) throws RiceIllegalArgumentException {
        boolean retval = false;
        // UA UPGRADE - if logging in and user is active employee the let them in
        if (KimConstants.PermissionNames.LOG_IN.equals(permissionName)) {
            retval = isAuthorizedToLogin(principalId);
        } else {
            retval = super.isAuthorized(principalId, namespaceCode, permissionName, qualification);
        }

        return retval;
    }

    private boolean isAuthorizedToLogin(String principalId) {
        Entity e = getIdentityService().getEntityByPrincipalId(principalId);
        return (e != null);
    }
    
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }
    
    public IdentityService getIdentityService() {
		return this.identityService;
	}
}    
