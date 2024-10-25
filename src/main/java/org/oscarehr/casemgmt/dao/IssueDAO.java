/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.casemgmt.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.AbstractDaoImpl;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.quatro.model.security.Secrole;

public interface IssueDAO {

    public Issue getIssue(Long id);

    public List<Issue> getIssues();

    public List<Issue> findIssueByCode(String[] codes);

    public Issue findIssueByCode(String code);

    public Issue findIssueByTypeAndCode(String type, String code);

    public void saveIssue(Issue issue);

    public void delete(Long issueId);

    public List<Issue> findIssueBySearch(String search);

    public List<Long> getIssueCodeListByRoles(List<Secrole> roles);

    public List<Issue> search(String search, List<Secrole> roles, final int startIndex, final int numToReturn);

    public Integer searchCount(String search, List<Secrole> roles);

    public List searchNoRolesConcerned(String search);

    public List<String> getLocalCodesByCommunityType(String type);

}
