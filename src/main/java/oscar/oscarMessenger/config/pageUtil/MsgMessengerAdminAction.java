/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarMessenger.config.pageUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.GroupMembersDao;
import org.oscarehr.common.dao.GroupsDao;
import org.oscarehr.common.model.GroupMembers;
import org.oscarehr.common.model.Groups;
import org.oscarehr.managers.MessengerGroupManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.oscarMessenger.data.ContactIdentifier;
import oscar.oscarMessenger.data.MsgAddressBookMaker;
import oscar.oscarMessenger.data.MsgProviderData;
import oscar.util.ConversionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MsgMessengerAdminAction extends DispatchAction {

	private MessengerGroupManager messengerGroupManager = SpringUtils.getBean(MessengerGroupManager.class);	
	private GroupsDao groupsDao = SpringUtils.getBean(GroupsDao.class);
	private GroupMembersDao groupMembersDao = SpringUtils.getBean(GroupMembersDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@SuppressWarnings("unused")
	public ActionForward fetch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);		
		Map<Groups, List<MsgProviderData>> groups = messengerGroupManager.getAllGroupsWithMembers(loggedInInfo);
		List<MsgProviderData> localContacts = messengerGroupManager.getAllLocalMessengerContactList(loggedInInfo);
		Map<String, List<MsgProviderData>> remoteContacts = messengerGroupManager.getAllRemoteMessengerContactList(loggedInInfo);
		
		request.setAttribute("groups", groups);
		request.setAttribute("localContacts", localContacts);
		request.setAttribute("remoteContacts", remoteContacts);
		return mapping.findForward("success");
	}
	
	@SuppressWarnings("unused")
	public void add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);	
		String memberId = request.getParameter("member");
		String groupId = request.getParameter("group");
		if(groupId == null) {
			groupId = "0";
		}
		if(memberId != null && ! memberId.isEmpty()) {
			//incoming id is expected to be a composite id 
			ContactIdentifier contactIdentifier = new ContactIdentifier(memberId);
			messengerGroupManager.addMember(loggedInInfo, contactIdentifier, Integer.parseInt(groupId));
		}
		request.setAttribute("success", true);
	}
	
	@SuppressWarnings("unused")
	public void remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);	
		String memberId = request.getParameter("member");
		String groupId = request.getParameter("group");
		if(groupId == null || groupId.isEmpty()) {
			groupId = "0";
		}
		
		if(memberId != null && !memberId.isEmpty()) {
			//incoming id is expected to be a composite id
			ContactIdentifier contactIdentifier = new ContactIdentifier(memberId);

			if ("0".equals(groupId)) {
				messengerGroupManager.removeMember(loggedInInfo, contactIdentifier);
			} else {
				contactIdentifier.setGroupId(Integer.parseInt(groupId));
				messengerGroupManager.removeGroupMember(loggedInInfo, contactIdentifier);
			}
		} else if(! "0".equals(groupId)){
			messengerGroupManager.removeGroup(loggedInInfo, Integer.parseInt(groupId));
		}
	}
	
	public void create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String groupName = request.getParameter("groupName");
		String parentId = request.getParameter("parentId");
		if(parentId == null) {
			parentId = "0";
		}
		
		messengerGroupManager.addGroup(loggedInInfo, groupName, Integer.parseInt(parentId));
		fetch(mapping, form, request, response);
	}

	/**
	 * @deprecated
	 * Use remove method
	 */
	@Deprecated
	@SuppressWarnings("unused")
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String parent = new String();
		
		GroupsDao dao = SpringUtils.getBean(GroupsDao.class);
		String grpNo = ((MsgMessengerAdminForm) form).getGrpNo();
		
		Groups gg = dao.find(ConversionUtils.fromIntString(grpNo));
		if (gg != null) {
			parent = "" + gg.getParentId();
		}

		if (dao.findByParentId(ConversionUtils.fromIntString(parent)).size() > 1) {
			request.setAttribute("groupNo", grpNo);
			request.setAttribute("fail", "This Group has Children, you must delete the children groups first");
			return (mapping.findForward("failure"));
		}

		for (GroupMembers g : groupMembersDao.findByGroupId(Integer.parseInt(grpNo))) {
			groupMembersDao.remove(g.getId());
		}

		Groups g = groupsDao.find(Integer.parseInt(grpNo));
		if (g != null) {
			groupsDao.remove(g.getId());
		}

		MsgAddressBookMaker addMake = new MsgAddressBookMaker();
		addMake.updateAddressBook();
		request.setAttribute("groupNo", parent);
		
		return mapping.findForward("success");
	}
	
	@Deprecated
	@SuppressWarnings("unused")
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
			throw new SecurityException("missing required security object (_admin)");
		}
		
		String[] providers = ((MsgMessengerAdminForm) form).getProviders();
		String grpNo = ((MsgMessengerAdminForm) form).getGrpNo();
		String update = ((MsgMessengerAdminForm) form).getUpdate();
		String delete = ((MsgMessengerAdminForm) form).getDelete();

		String parent = new String();

		ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources", request.getLocale());

		if (update.equals(oscarR.getString("oscarMessenger.config.MessengerAdmin.btnUpdateGroupMembers"))) {

			for (GroupMembers g : groupMembersDao.findByGroupId(Integer.parseInt(grpNo))) {
				groupMembersDao.remove(g.getId());
			}

			for (int i = 0; i < providers.length; i++) {
				GroupMembers gm = new GroupMembers();
				gm.setGroupId(Integer.parseInt(grpNo));
				gm.setProviderNo(providers[i]);
				groupMembersDao.persist(gm);

			}

			MsgAddressBookMaker addMake = new MsgAddressBookMaker();
			addMake.updateAddressBook();
			request.setAttribute("groupNo", grpNo);
		} else if (delete.equals(oscarR.getString("oscarMessenger.config.MessengerAdmin.btnDeleteThisGroup"))) {
			GroupsDao dao = SpringUtils.getBean(GroupsDao.class);
			Groups gg = dao.find(ConversionUtils.fromIntString(grpNo));
			if (gg != null) {
				parent = "" + gg.getParentId();
			}

			if (dao.findByParentId(ConversionUtils.fromIntString(parent)).size() > 1) {
				request.setAttribute("groupNo", grpNo);
				request.setAttribute("fail", "This Group has Children, you must delete the children groups first");
				return (mapping.findForward("failure"));
			}

			for (GroupMembers g : groupMembersDao.findByGroupId(Integer.parseInt(grpNo))) {
				groupMembersDao.remove(g.getId());
			}

			Groups g = groupsDao.find(Integer.parseInt(grpNo));
			if (g != null) {
				groupsDao.remove(g.getId());
			}

			MsgAddressBookMaker addMake = new MsgAddressBookMaker();
			addMake.updateAddressBook();
			request.setAttribute("groupNo", parent);
		}

		return (mapping.findForward("success"));
	}

}
