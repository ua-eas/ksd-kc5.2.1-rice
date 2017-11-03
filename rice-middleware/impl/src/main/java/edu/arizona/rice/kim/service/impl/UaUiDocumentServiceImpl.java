/*
 *
 * Copyright 2009 State of Arizona Board of Regents
 *
 */
package edu.arizona.rice.kim.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupMember;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.service.impl.UiDocumentServiceImpl;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.KRADUtils;

import edu.arizona.rice.kim.document.UaIdentityManagementPersonDocument;

//import org.kuali.rice.kim.service.KIMServiceLocator;

/**
 *
 * @author Leo Przybylski (przybyls@arizona.edu)
 */
public class UaUiDocumentServiceImpl extends UiDocumentServiceImpl {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UiDocumentServiceImpl.class);
	
	/**
	 * Looks up GroupInfo objects for each group id passed in
	 * 
	 * @param groupIds
	 *            the List of group ids to look up GroupInfo records on
	 * @return a List of GroupInfo records
	 */
	protected List<? extends Group> getGroupsByIds(List<String> groupIds) {
		List<Group> groups = new ArrayList<Group>();
		for (String groupId : groupIds) {
			final Group group = getGroupService().getGroup(groupId);
			groups.add(group);
		}
		return groups;
	}

	protected List<UaIdentityManagementPersonDocument> getPrincipalFromPrincipals(List<PrincipalContract> principals) {
		List<UaIdentityManagementPersonDocument> retval = new ArrayList<UaIdentityManagementPersonDocument>();

		for (PrincipalContract principal : principals) {
			PrincipalBo impl = getPrincipalImpl(principal.getPrincipalId());
			impl.setPrincipalId(principal.getPrincipalId());
			impl.setPrincipalName(principal.getPrincipalName());
			impl.setEntityId(principal.getEntityId());
			impl.setActive(principal.isActive());
		}

		return retval;
	}

	protected PrincipalBo getPrincipalImpl(String principalId) {
		return (PrincipalBo) getDataObjectService().find(PrincipalBo.class, principalId);
	}

	/**
	 * Determines if the pending member is a current or future member of the
	 * group
	 * 
	 * @param pendingMember
	 *            the member to check
	 * @param now
	 *            the current date
	 * @return true if the member is a future or current member of the group,
	 *         false otherwise
	 */
	protected boolean isCurrentOrFutureGroupMember(GroupMember member, DateTime today) {
		return member.getActiveToDate() == null || today.compareTo(member.getActiveToDate()) < 0;
	}

	/** 
	 * UA KFS7 upgrade. (UAF-5246) Overriding loadRoleMembers since editing role runs into 'java.lang.UnsupportedOperationException: This method is not supported under the UA EDS MOD" per UAF-5246
	 * Changed to use new API added to IdentityService to allow loading Person information from EDS
	 */
	@Override
	protected List<KimDocumentRoleMember> loadRoleMembers(
            IdentityManagementRoleDocument identityManagementRoleDocument, List<RoleMemberBo> members){
        List<KimDocumentRoleMember> pndMembers = new ArrayList<KimDocumentRoleMember>();

        if(KRADUtils.isNull(members) || members.isEmpty() ){
            return pndMembers;
        }

        // extract all the principal role member IDs
        List<String> roleMemberPrincipalIds = new ArrayList<String>();
        for(RoleMemberBo roleMember : members) {
            if (roleMember.getType().getCode().equals(KimConstants.KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode())) {
                if (!roleMemberPrincipalIds.contains(roleMember.getMemberId())) {
                    roleMemberPrincipalIds.add(roleMember.getMemberId());
                }
            }
        }

        // pull in all the names for the principals for later use
        Map<String, EntityDefault> principalIdToEntityMap = new HashMap<String,EntityDefault>();
        /*KULRICE-12538: If roleMemberPrincipalIds is empty, skip populating list of pricipals*/
        if(!roleMemberPrincipalIds.isEmpty()){
        	// UA KFS7 upgrade. (UAF-5246) Use new API to load Person information from EDS
            Map<String, String> criteria = new HashMap<String, String>();
            
            StringBuffer sb = new StringBuffer();
            for (String principalId : roleMemberPrincipalIds) {
				sb.append( principalId + "|");
			}
            
            String principalIds = sb.toString();
            if (StringUtils.endsWith(principalIds, "|")) {
            	principalIds = StringUtils.substringBeforeLast(principalIds, "|");
            }
            
			// add the list of principal IDs to the lookup so that only matching Person objects are returned
			criteria.put( KIMPropertyConstants.Person.PRINCIPAL_ID, principalIds );
			criteria.put("principals." + KRADPropertyConstants.ACTIVE, "Y");

    		List<EntityDefault> entities = getIdentityService().findEntityDefaultsByCriteriaMap(criteria);
            principalIdToEntityMap = new HashMap<String,EntityDefault>( entities.size() );
            for ( EntityDefault entity : entities ) {
                // yes, I'm missing null checks, but since I searched on principal ID - there needs to be
                // at least one record
                principalIdToEntityMap.put( entity.getPrincipals().get(0).getPrincipalId(), entity );
            }
            // END UA KFS7 upgrade changes
        }
        // pull in all the group members of this role
        Map<String, Group> roleGroupMembers = findGroupsForRole(identityManagementRoleDocument.getRoleId());

        for(RoleMemberBo member: members){
            KimDocumentRoleMember pndMember = new KimDocumentRoleMember();
            pndMember.setActiveFromDate(member.getActiveFromDateValue());
            pndMember.setActiveToDate(member.getActiveToDateValue());
            pndMember.setActive(member.isActive( getDateTimeService().getCurrentTimestamp()));
            if(pndMember.isActive()){
                pndMember.setRoleMemberId(member.getId());
                pndMember.setRoleId(member.getRoleId());
                pndMember.setMemberTypeCode(member.getType().getCode());
                pndMember.setMemberId(member.getMemberId());
                pndMember.setMemberNamespaceCode(getMemberNamespaceCode(member.getType(), member.getMemberId()));

                if ( StringUtils.equals( pndMember.getMemberTypeCode(), MemberType.PRINCIPAL.getCode() ) ) {
                    EntityDefault entity = principalIdToEntityMap.get(member.getMemberId());
                    if ( entity != null ) {
                        pndMember.setMemberName(entity.getPrincipals().get(0).getPrincipalName());

                        if ( entity.getName() != null ) {
                            pndMember.setMemberFullName(entity.getName().getFirstName() + " " + entity.getName().getLastName());
                        }
                    }
                } else if ( StringUtils.equals( pndMember.getMemberTypeCode(), MemberType.GROUP.getCode() ) ) {
                    Group group =  roleGroupMembers.get(member.getMemberId());
                    if (group != null) {
                        pndMember.setMemberName(group.getName());
                        pndMember.setMemberNamespaceCode(group.getNamespaceCode());
                        pndMember.setMemberFullName(group.getName());
                    }
                } else if ( StringUtils.equals( pndMember.getMemberTypeCode(), MemberType.ROLE.getCode() ) ) {
                    pndMember.setMemberName(getMemberName(member.getType(), member.getMemberId()));
                    pndMember.setMemberFullName(getMemberFullName(member.getType(), member.getMemberId()));
                }

                pndMember.setQualifiers(loadRoleMemberQualifiers(identityManagementRoleDocument, member.getAttributeDetails()));
                pndMember.setEdit(true);
                pndMembers.add(pndMember);
            }
        }
        Collections.sort(pndMembers, identityManagementRoleDocument.getMemberMetaDataType());
        return pndMembers;
    }


    @Override
    protected void updateRoleMembers( String roleId, String kimTypeId, List<KimDocumentRoleMember> modifiedRoleMembers, List<RoleMemberBo> roleMembers){
        super.updateRoleMembers(roleId, kimTypeId, modifiedRoleMembers, roleMembers);

        // If this is not cleared, they are later assumed by super.setMembersInDocument() as inactivations,
        // will then get removed from the parent document, and subsequently deleted from the DB during the next
        // save of the parent document (this only happens w/ IdentityManagementRoleDocument.save() and then
        // directly after hitting reload button of same doc).
        modifiedRoleMembers.clear();
    }

}
