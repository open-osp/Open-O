//CHECKSTYLE:OFF
/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 * <p>
 * Modifications made by Magenta Health in 2024.
 */
package ca.openosp.openo.commn.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Query;

import ca.openosp.openo.commn.model.CustomFilter;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.commn.model.Tickler;
import ca.openosp.openo.tickler.dto.TicklerCommentDTO;
import ca.openosp.openo.tickler.dto.TicklerLinkDTO;
import ca.openosp.openo.tickler.dto.TicklerListDTO;
import org.springframework.stereotype.Repository;

@Repository
public class TicklerDaoImpl extends AbstractDaoImpl<Tickler> implements TicklerDao {

    public TicklerDaoImpl() {
        super(Tickler.class);
    }

    @Override
    public Tickler find(Integer id) {
        Tickler tickler = super.find(id);
        tickler.getUpdates().size();
        return tickler;
    }

    @Override
    public List<Tickler> findActiveByMessageForPatients(List<Integer> demographicNos, String remString) {

        //weird logic here, beware.
        if (demographicNos.isEmpty())
            demographicNos.add(0);

        Query query = entityManager.createQuery("select t from Tickler t where t.demographicNo in (?1) and t.status = 'A' and t.message like ?2");
        query.setParameter(1, demographicNos);
        query.setParameter(2, "%" + remString + "%");

        @SuppressWarnings("unchecked")
        List<Tickler> results = query.getResultList();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> findActiveByDemographicNoAndMessage(Integer demoNo, String message) {

        Query query = entityManager.createQuery("select t from Tickler t where t.demographicNo = ?1 and t.message = ?2 and t.status = 'A'");
        query.setParameter(1, demoNo);
        query.setParameter(2, message);

        List<Tickler> results = query.getResultList();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> findActiveByDemographicNo(Integer demoNo) {

        Query query = entityManager.createQuery("select t from Tickler t where t.demographicNo = ?1 and t.status = 'A' order by t.serviceDate desc");
        query.setParameter(1, demoNo);

        List<Tickler> results = query.getResultList();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> findByTicklerNoDemo(Integer ticklerNo, Integer demoNo) {

        Query query = entityManager.createQuery("select t from Tickler t where t.id = ?1 AND t.demographicNo = ?2 AND t.status != 'D'");
        query.setParameter(1, ticklerNo);
        query.setParameter(2, demoNo);

        List<Tickler> results = query.getResultList();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> findByTicklerNoAssignedTo(Integer ticklerNo, String assignedTo, Integer demoNo) {

        Query query = entityManager.createQuery("select t from Tickler t where t.id = ?1 AND t.taskAssignedTo = ?2 AND t.demographicNo = ?3 AND t.status != 'D'");
        query.setParameter(1, ticklerNo);
        query.setParameter(2, assignedTo);
        query.setParameter(3, demoNo);

        List<Tickler> results = query.getResultList();

        return results;
    }

    /**
     * Finds all ticklers for the specified demographic, assigned to and message fields.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> findByDemographicIdTaskAssignedToAndMessage(Integer demographicNo, String taskAssignedTo, String message) {
        Query query = entityManager.createQuery("select t from Tickler t where t.demographicNo = ?1 AND t.taskAssignedTo = ?2 and t.message = ?3");
        query.setParameter(1, demographicNo);
        query.setParameter(2, taskAssignedTo);
        query.setParameter(3, message);

        List<Tickler> results = query.getResultList();

        return results;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> search_tickler_bydemo(Integer demographicNo, String status, Date beginDate, Date endDate) {
        Query query = entityManager.createQuery("SELECT t FROM Tickler t WHERE t.demographicNo = ?1 and t.status = ?2 and t.serviceDate >= ?3 and t.serviceDate <= ?4 order by t.serviceDate desc");
        query.setParameter(1, demographicNo);
        query.setParameter(2, this.convertStatus(status));
        query.setParameter(3, beginDate);
        query.setParameter(4, endDate);

        List<Tickler> results = query.getResultList();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> search_tickler(Integer demographicNo, Date endDate) {
        Query query = entityManager.createQuery("SELECT t FROM Tickler t WHERE t.demographicNo = ?1 and t.status = 'A' and t.serviceDate <= ?2 order by t.serviceDate desc");
        query.setParameter(1, demographicNo);
        query.setParameter(2, endDate);


        List<Tickler> results = query.getResultList();

        return results;
    }

    /**
     * Finds all ticklers for the specified demographic, and date range.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> listTicklers(Integer demographicNo, Date beginDate, Date endDate) {
        Query query = entityManager.createQuery("select t FROM Tickler t where t.status = 'A' and (t.serviceDate >= ?1 and t.serviceDate <= ?2) and t.demographicNo = ?3 order by t.serviceDate desc");
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, demographicNo);

        List<Tickler> results = query.getResultList();

        return results;
    }

    @Override
    public int getActiveTicklerCount(String providerNo) {
        Query query = entityManager.createQuery("select count(t) FROM Tickler t where t.status = 'A' and t.serviceDate <= ?1 and (t.taskAssignedTo  = ?2 or t.taskAssignedTo='All Providers')");
        query.setParameter(1, new Date());
        query.setParameter(2, providerNo);

        Long result = (Long) query.getSingleResult();

        return result.intValue();
    }

    @Override
    public int getActiveTicklerByDemoCount(Integer demographicNo) {
        Query query = entityManager.createQuery("select count(t) FROM Tickler t where t.status = 'A' and t.serviceDate <= ?1 and t.demographicNo  = ?2 ");
        query.setParameter(1, new Date());
        query.setParameter(2, demographicNo);

        Long result = (Long) query.getSingleResult();

        return result.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tickler> getTicklers(CustomFilter filter, int offset, int limit) {
        String sql = "select t";
        ArrayList<Object> paramList = new ArrayList<Object>();
        sql = getTicklerQueryString(sql, paramList, filter);

        Query query = entityManager.createQuery(sql);
        for (int x = 0; x < paramList.size(); x++) {
            query.setParameter(x + 1, paramList.get(x));
        }
        query.setFirstResult(offset);
        setLimit(query, limit);
        return query.getResultList();
    }


    /**
     * @param filter
     * @return
     * @Deprecated Get Ticklers.
     * <p>
     * Warning..this will limit you to TicklerDao.MAX_LIST_RETURN_SIZE
     */
    @Override
    public List<Tickler> getTicklers(CustomFilter filter) {
        return getTicklers(filter, 0, TicklerDao.MAX_LIST_RETURN_SIZE);
    }


    @Override
    public int getNumTicklers(CustomFilter filter) {
        List<Object> paramList = new ArrayList<Object>();
        String sql = "select count(t)";
        sql = getTicklerQueryString(sql, paramList, filter);

        Query query = entityManager.createQuery(sql);
        for (int x = 0; x < paramList.size(); x++) {
            query.setParameter(x + 1, paramList.get(x));
        }

        Long result = (Long) query.getSingleResult();

        return result.intValue();
    }

    /**
     * selectQuery is in the form of "SELECT t"
     *
     * @param selectQuery
     * @param paramList
     * @param filter
     * @return
     */
    private String getTicklerQueryString(String selectQuery, List<Object> paramList, CustomFilter filter) {
//		String tickler_date_order = filter.getSort_order();

        String query = selectQuery + " FROM Tickler t WHERE 1=1 ";
        int paramIndex = 1;
        boolean includeMRPClause = true;
        boolean includeProviderClause = true;
        boolean includeAssigneeClause = true;
        boolean includeStatusClause = true;
        boolean includeClientClause = true;
        boolean includeDemographicClause = true;
        boolean includeProgramClause = true;
        boolean includeMessage = true;
        boolean includePriorityClause = true;
        boolean includeServiceStartDateClause = false;
        boolean includeServiceEndDateClause = false;

        if (filter.getStartDate() != null) {
            includeServiceStartDateClause = true;
        }
        if (filter.getEndDate() != null) {
            includeServiceEndDateClause = true;
        }

        if (filter.getProgramId() == null || "".equals(filter.getProgramId()) || filter.getProgramId().equals("All Programs")) {
            includeProgramClause = false;
        }
        if (filter.getProvider() == null || filter.getProvider().equals("All Providers") || filter.getProvider().equals("")) {
            includeProviderClause = false;
        }
        if (filter.getAssignee() == null || filter.getAssignee().equals("All Providers") || filter.getAssignee().equals("")) {
            includeAssigneeClause = false;
        }
        if (filter.getClient() == null || filter.getClient().equals("All Clients")) {
            includeClientClause = false;
        }
        if (filter.getDemographicNo() == null || filter.getDemographicNo().equals("") || filter.getDemographicNo().equalsIgnoreCase("All Clients")) {
            includeDemographicClause = false;
        }
        if (filter.getStatus().equals("") || filter.getStatus().equals("Z")) {
            includeStatusClause = false;
        }
        if (filter.getPriority() == null || "".equals(filter.getPriority())) {
            includePriorityClause = false;
        }
        if (filter.getMrp() == null || filter.getMrp().equals("All Providers") || filter.getMrp().equals("")) {
            includeMRPClause = false;
        }
        if (filter.getMessage() == null || filter.getMessage().trim().isEmpty()) {
            includeMessage = false;
        }

        if (includeMRPClause) {
            query = selectQuery + " FROM Tickler t, Demographic d where d.DemographicNo = t.demographicNo and d.ProviderNo = ?" + paramIndex++;
            paramList.add(filter.getMrp());
        }

        if (includeServiceStartDateClause) {
            query = query + " and t.serviceDate >= ?" + paramIndex++;
            paramList.add(filter.getStartDate());
        }

        if (includeServiceEndDateClause) {
            query = query + " and t.serviceDate <= ?" + paramIndex++;

            Calendar cal = Calendar.getInstance();
            cal.setTime(filter.getEndDate());

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);

            paramList.add(new Date(cal.getTime().getTime()));
        }

        //TODO: IN clause
        if (includeProviderClause) {
            query = query + " and t.creator IN (";
            Set<Provider> pset = filter.getProviders();
            Provider[] providers = pset.toArray(new Provider[pset.size()]);
            for (int x = 0; x < providers.length; x++) {
                if (x > 0) {
                    query += ",";
                }
                query += "?" + paramIndex++;
                paramList.add(providers[x].getProviderNo());
            }
            query += ")";
        }

        //TODO: IN clause
        if (includeAssigneeClause) {
            query = query + " and t.taskAssignedTo IN (";
            Set<Provider> pset = filter.getAssignees();
            Provider[] providers = pset.toArray(new Provider[pset.size()]);
            for (int x = 0; x < providers.length; x++) {
                if (x > 0) {
                    query += ",";
                }
                query += "?" + paramIndex++;
                paramList.add(providers[x].getProviderNo());
            }
            query += ")";
        }

        if (includeProgramClause) {
            query = query + " and t.programId = ?" + paramIndex++;
            paramList.add(Integer.valueOf(filter.getProgramId()));
        }
        if (includeStatusClause) {
            query = query + " and t.status = ?" + paramIndex++;
            paramList.add(convertStatus(filter.getStatus()));
        }

        if (includePriorityClause) {
            query = query + " and t.priority = ?" + paramIndex++;
            paramList.add(convertPriority(filter.getPriority()));
        }

        if (includeClientClause) {
            query = query + " and t.demographicNo = ?" + paramIndex++;
            paramList.add(Integer.parseInt(filter.getClient()));
        }
        if (includeDemographicClause) {
            query = query + " and t.demographicNo = ?" + paramIndex++;
            paramList.add(Integer.parseInt(filter.getDemographicNo()));
        }
        if (includeMessage) {
            query = query + " and t.message = ?" + paramIndex++;
            paramList.add(filter.getMessage());
        }

        return query;
    }

    private Tickler.STATUS convertStatus(String status) {
        Tickler.STATUS result = Tickler.STATUS.A;
        if (status != null && status.startsWith("C"))
            result = Tickler.STATUS.C;
        if (status != null && status.startsWith("D"))
            result = Tickler.STATUS.D;
        return result;
    }

    private Tickler.PRIORITY convertPriority(String priority) {
        Tickler.PRIORITY result = Tickler.PRIORITY.Normal;
        if (priority != null && priority.equals("High"))
            result = Tickler.PRIORITY.High;
        if (priority != null && priority.equals("Low"))
            result = Tickler.PRIORITY.Low;
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses JPQL constructor expression for direct DTO projection,
     * then batch-loads comments and links in separate queries.
     * </p>
     *
     * @since 2026-01-30
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<TicklerListDTO> getTicklerDTOs(CustomFilter filter, int offset, int limit) {
        ArrayList<Object> paramList = new ArrayList<Object>();
        String sql = getTicklerDTOQueryString(paramList, filter);

        Query query = entityManager.createQuery(sql, TicklerListDTO.class);
        for (int x = 0; x < paramList.size(); x++) {
            query.setParameter(x + 1, paramList.get(x));
        }
        query.setFirstResult(offset);
        setLimit(query, limit);

        List<TicklerListDTO> ticklerDTOs = query.getResultList();

        loadCommentsForTicklerDTOs(ticklerDTOs);
        loadLinksForTicklerDTOs(ticklerDTOs);

        return ticklerDTOs;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2026-01-30
     */
    @Override
    public List<TicklerListDTO> getTicklerDTOs(CustomFilter filter) {
        return getTicklerDTOs(filter, 0, TicklerDao.MAX_LIST_RETURN_SIZE);
    }

    /**
     * Builds the JPQL query string for TicklerListDTO projection.
     *
     * @param paramList List to populate with query parameters
     * @param filter CustomFilter the filter criteria
     * @return String the complete JPQL query
     */
    private String getTicklerDTOQueryString(List<Object> paramList, CustomFilter filter) {
        int paramIndex = 1;

        boolean includeMRPClause = filter.getMrp() != null
                && !filter.getMrp().isEmpty()
                && !"All Providers".equals(filter.getMrp());
        boolean includeProviderClause = filter.getProvider() != null
                && !filter.getProvider().isEmpty()
                && !"All Providers".equals(filter.getProvider());
        boolean includeAssigneeClause = filter.getAssignee() != null
                && !filter.getAssignee().isEmpty()
                && !"All Providers".equals(filter.getAssignee());
        boolean includeStatusClause = filter.getStatus() != null
                && !filter.getStatus().isEmpty()
                && !"Z".equals(filter.getStatus());
        boolean includePriorityClause = filter.getPriority() != null
                && !filter.getPriority().isEmpty();
        boolean includeClientClause = isValidIntegerFilter(filter.getClient())
                && !"All Clients".equals(filter.getClient());
        boolean includeDemographicClause = isValidIntegerFilter(filter.getDemographicNo())
                && !"All Clients".equalsIgnoreCase(filter.getDemographicNo());
        boolean includeProgramClause = isValidIntegerFilter(filter.getProgramId())
                && !"All Programs".equals(filter.getProgramId());
        boolean includeMessage = filter.getMessage() != null
                && !filter.getMessage().trim().isEmpty();

        StringBuilder query = new StringBuilder();
        query.append("SELECT NEW ca.openosp.openo.tickler.dto.TicklerListDTO(");
        query.append("t.id, t.message, t.serviceDate, t.createDate, t.status, t.priority, ");
        query.append("t.demographicNo, d.LastName, d.FirstName, ");
        query.append("creator.LastName, creator.FirstName, ");
        query.append("assignee.LastName, assignee.FirstName) ");
        query.append("FROM Tickler t ");
        query.append("LEFT JOIN Demographic d ON d.DemographicNo = t.demographicNo ");
        query.append("LEFT JOIN Provider creator ON creator.ProviderNo = t.creator ");
        query.append("LEFT JOIN Provider assignee ON assignee.ProviderNo = t.taskAssignedTo ");

        if (includeMRPClause) {
            query.append("WHERE d.ProviderNo = ?").append(paramIndex++).append(" ");
            paramList.add(filter.getMrp());
        } else {
            query.append("WHERE 1=1 ");
        }

        if (filter.getStartDate() != null) {
            query.append("AND t.serviceDate >= ?").append(paramIndex++).append(" ");
            paramList.add(filter.getStartDate());
        }

        if (filter.getEndDate() != null) {
            query.append("AND t.serviceDate <= ?").append(paramIndex++).append(" ");
            Calendar cal = Calendar.getInstance();
            cal.setTime(filter.getEndDate());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            paramList.add(new Date(cal.getTime().getTime()));
        }

        if (includeProviderClause) {
            appendInClause(query, paramList, "t.creator", filter.getProviders(), paramIndex);
            paramIndex += filter.getProviders().size();
        }

        if (includeAssigneeClause) {
            appendInClause(query, paramList, "t.taskAssignedTo", filter.getAssignees(), paramIndex);
            paramIndex += filter.getAssignees().size();
        }

        if (includeProgramClause) {
            query.append("AND t.programId = ?").append(paramIndex++).append(" ");
            paramList.add(Integer.valueOf(filter.getProgramId()));
        }

        if (includeStatusClause) {
            query.append("AND t.status = ?").append(paramIndex++).append(" ");
            paramList.add(convertStatus(filter.getStatus()));
        }

        if (includePriorityClause) {
            query.append("AND t.priority = ?").append(paramIndex++).append(" ");
            paramList.add(convertPriority(filter.getPriority()));
        }

        if (includeClientClause) {
            query.append("AND t.demographicNo = ?").append(paramIndex++).append(" ");
            paramList.add(Integer.parseInt(filter.getClient()));
        }

        if (includeDemographicClause) {
            query.append("AND t.demographicNo = ?").append(paramIndex++).append(" ");
            paramList.add(Integer.parseInt(filter.getDemographicNo()));
        }

        if (includeMessage) {
            query.append("AND t.message = ?").append(paramIndex++).append(" ");
            paramList.add(filter.getMessage());
        }

        // ORDER BY clause for deterministic results and stable pagination
        // Secondary sort by id ensures consistent ordering when serviceDates are equal
        String orderBy = "ORDER BY t.serviceDate ";
        orderBy += "desc".equalsIgnoreCase(filter.getSort_order()) ? "DESC" : "ASC";
        orderBy += ", t.id DESC ";
        query.append(orderBy);

        return query.toString();
    }

    /**
     * Checks if a string value is a valid integer for use in queries.
     *
     * @param value the string to check
     * @return true if the value is non-null, non-empty, and parseable as an integer
     */
    private boolean isValidIntegerFilter(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Appends an IN clause for a set of providers to the query.
     *
     * @param query the query builder
     * @param paramList the parameter list to populate
     * @param fieldName the field name (e.g., "t.creator")
     * @param providers the set of providers
     * @param paramIndex the starting parameter index
     */
    private void appendInClause(StringBuilder query, List<Object> paramList, String fieldName,
                                Set<Provider> providers, int paramIndex) {
        query.append("AND ").append(fieldName).append(" IN (");
        int i = 0;
        for (Provider provider : providers) {
            if (i > 0) {
                query.append(",");
            }
            query.append("?").append(paramIndex + i);
            paramList.add(provider.getProviderNo());
            i++;
        }
        query.append(") ");
    }

    /**
     * Batch loads comments for a list of TicklerListDTOs.
     * Uses a single query to fetch all comments and then maps them to their parent ticklers.
     *
     * @param ticklerDTOs List of TicklerListDTO to populate with comments
     */
    @SuppressWarnings("unchecked")
    private void loadCommentsForTicklerDTOs(List<TicklerListDTO> ticklerDTOs) {
        if (ticklerDTOs == null || ticklerDTOs.isEmpty()) {
            return;
        }

        List<Integer> ticklerIds = ticklerDTOs.stream()
                .map(TicklerListDTO::getId)
                .collect(Collectors.toList());

        String commentSql = "SELECT NEW ca.openosp.openo.tickler.dto.TicklerCommentDTO(" +
                "c.id, c.ticklerNo, c.message, c.updateDate, " +
                "c.provider.LastName, c.provider.FirstName) " +
                "FROM TicklerComment c " +
                "LEFT JOIN c.provider " +
                "WHERE c.ticklerNo IN (:ticklerIds) " +
                "ORDER BY c.updateDate ASC";

        List<TicklerCommentDTO> allComments = entityManager
                .createQuery(commentSql, TicklerCommentDTO.class)
                .setParameter("ticklerIds", ticklerIds)
                .getResultList();

        Map<Integer, List<TicklerCommentDTO>> commentsByTickler = allComments.stream()
                .collect(Collectors.groupingBy(TicklerCommentDTO::getTicklerNo));

        for (TicklerListDTO tickler : ticklerDTOs) {
            tickler.setComments(
                    commentsByTickler.getOrDefault(tickler.getId(), Collections.emptyList())
            );
        }
    }

    /**
     * Batch loads links for a list of TicklerListDTOs.
     * Uses a single query to fetch all links and then maps them to their parent ticklers.
     *
     * @param ticklerDTOs List of TicklerListDTO to populate with links
     */
    @SuppressWarnings("unchecked")
    private void loadLinksForTicklerDTOs(List<TicklerListDTO> ticklerDTOs) {
        if (ticklerDTOs == null || ticklerDTOs.isEmpty()) {
            return;
        }

        List<Integer> ticklerIds = new ArrayList<>();
        Map<Integer, TicklerListDTO> ticklerMap = new HashMap<>();

        for (TicklerListDTO dto : ticklerDTOs) {
            ticklerIds.add(dto.getId());
            ticklerMap.put(dto.getId(), dto);
            dto.setLinks(new ArrayList<>());
        }

        String linkSql = "SELECT NEW ca.openosp.openo.tickler.dto.TicklerLinkDTO(" +
                "l.id, l.ticklerNo, l.tableName, l.tableId) " +
                "FROM TicklerLink l " +
                "WHERE l.ticklerNo IN (:ticklerIds) " +
                "ORDER BY l.id ASC";

        Query linkQuery = entityManager.createQuery(linkSql);
        linkQuery.setParameter("ticklerIds", ticklerIds);

        List<TicklerLinkDTO> links = linkQuery.getResultList();

        for (TicklerLinkDTO link : links) {
            TicklerListDTO tickler = ticklerMap.get(link.getTicklerNo());
            if (tickler != null) {
                tickler.getLinks().add(link);
            }
        }
    }
}
