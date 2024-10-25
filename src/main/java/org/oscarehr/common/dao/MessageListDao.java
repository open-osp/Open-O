/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
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
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.common.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.OscarMsgType;
import org.springframework.stereotype.Repository;

public interface MessageListDao extends AbstractDao<MessageList> {

	public List<MessageList> findByProviderNoAndMessageNo(String providerNo, Long messageNo);

	public List<MessageList> findByProviderNoAndLocationNo(String providerNo, Integer locationNo);

	public List<MessageList> findAllByMessageNoAndLocationNo(Long messageNo, Integer locationNo);

	public List<MessageList> findByMessageNoAndLocationNo(Long messageNo, Integer locationNo);

	public List<MessageList> findByMessage(Long messageNo);

	public List<MessageList> findByProviderAndStatus(String providerNo, String status);

	public List<MessageList> findUnreadByProvider(String providerNo);

	public int findUnreadByProviderAndAttachedCount(String providerNo);

	public int countUnreadByProviderAndFromIntegratedFacility(String providerNo);

	public int countUnreadByProvider(String providerNo);

	public List<MessageList> search(String providerNo, String status, int start, int max);

	public Integer searchAndReturnTotal(String providerNo, String status);

	public Integer messagesTotal(int type, String providerNo, Integer remoteLocation, String searchFilter);

	public List<MessageList> findByIntegratedFacility(int facilityId, String status);

	public List<MessageList> findByMessageAndIntegratedFacility(Long messageNo, int facilityId);

}
