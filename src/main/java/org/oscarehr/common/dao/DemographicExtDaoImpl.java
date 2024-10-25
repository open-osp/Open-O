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

import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.enumerator.DemographicExtKey;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.*;

@Repository
public class DemographicExtDaoImpl extends AbstractDaoImpl<DemographicExt> implements DemographicExtDao {

    public DemographicExtDaoImpl() {
        super(DemographicExt.class);
    }

    @Override
    public DemographicExt getDemographicExt(Integer id) {
        return find(id);
    }

    @Override
    public List<DemographicExt> getDemographicExtByDemographicNo(Integer demographicNo) {

        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        Query query = entityManager
                .createQuery("SELECT d from DemographicExt d where d.demographicNo=? order by d.dateCreated DESC");
        query.setParameter(0, demographicNo);

        @SuppressWarnings("unchecked")
        List<DemographicExt> results = query.getResultList();

        return results;
    }

    @Override
    public DemographicExt getDemographicExt(Integer demographicNo, DemographicExtKey demographicExtKey) {
        return getDemographicExt(demographicNo, demographicExtKey.getKey());
    }

    /**
     * @Deprecated: use alternate method with DemographicExtKey parameter
     */
    @Deprecated
    @Override
    public DemographicExt getDemographicExt(Integer demographicNo, String key) {

        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        if (key == null || key.length() <= 0) {
            throw new IllegalArgumentException();
        }

        Query query = entityManager.createQuery(
                "SELECT d from DemographicExt d where d.demographicNo=? and d.key = ? order by d.dateCreated DESC");
        query.setParameter(0, demographicNo);
        query.setParameter(1, key);

        @SuppressWarnings("unchecked")
        List<DemographicExt> results = query.getResultList();

        if (results.isEmpty())
            return null;
        DemographicExt result = results.get(0);

        return result;
    }

    @Override
    public List<DemographicExt> getDemographicExtByKeyAndValue(DemographicExtKey demographicExtKey, String value) {
        return getDemographicExtByKeyAndValue(demographicExtKey.getKey(), value);
    }

    /**
     * @Deprecated: use alternate method with DemographicExtKey parameter
     */
    @Deprecated
    @Override
    public List<DemographicExt> getDemographicExtByKeyAndValue(String key, String value) {

        Query query = entityManager.createQuery(
                "SELECT d from DemographicExt d where d.key = ? and d.value=? order by d.dateCreated DESC");
        query.setParameter(0, key);
        query.setParameter(1, value);
        return query.getResultList();
    }

    @Override
    public DemographicExt getLatestDemographicExt(Integer demographicNo, String key) {

        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        if (key == null || key.length() <= 0) {
            throw new IllegalArgumentException();
        }

        Query query = entityManager.createQuery(
                "SELECT d from DemographicExt d where d.demographicNo=? and d.key = ? order by d.dateCreated DESC, d.id DESC");
        query.setParameter(0, demographicNo);
        query.setParameter(1, key);

        @SuppressWarnings("unchecked")
        List<DemographicExt> results = query.getResultList();

        if (results.isEmpty())
            return null;
        DemographicExt result = results.get(0);

        return result;
    }

    @Override
    public void updateDemographicExt(DemographicExt de) {

        if (de == null) {
            throw new IllegalArgumentException();
        }

        merge(de);
    }

    @Override
    public void saveDemographicExt(Integer demographicNo, String key, String value) {

        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        if (key == null || key.length() <= 0) {
            throw new IllegalArgumentException();
        }

        if (value == null) {
            return;
        }

        DemographicExt existingDe = this.getDemographicExt(demographicNo, key);

        if (existingDe != null) {
            existingDe.setDateCreated(new Date());
            existingDe.setValue(value);
            merge(existingDe);
        } else {
            DemographicExt de = new DemographicExt();
            de.setDateCreated(new Date());
            de.setDemographicNo(demographicNo);
            de.setHidden(false);
            de.setKey(key);
            de.setValue(value);
            persist(de);
        }

    }

    @Override
    public void removeDemographicExt(Integer id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        remove(id);
    }

    @Override
    public void removeDemographicExt(Integer demographicNo, String key) {

        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        if (key == null || key.length() <= 0) {
            throw new IllegalArgumentException();
        }

        DemographicExt tmp = getDemographicExt(demographicNo, key);
        if (tmp != null) {
            remove(tmp.getId());
        }
    }

    @Override
    public Map<String, String> getAllValuesForDemo(Integer demo) {
        Map<String, String> retval = new HashMap<String, String>();
        Query query = entityManager
                .createQuery("SELECT d from DemographicExt d where d.demographicNo=? order by d.dateCreated");
        query.setParameter(0, demo);

        @SuppressWarnings("unchecked")
        List<DemographicExt> demographicExts = query.getResultList();
        for (DemographicExt demographicExt : demographicExts) {
            retval.put(demographicExt.getKey(), demographicExt.getValue());
            retval.put(demographicExt.getKey() + "_id", demographicExt.getId().toString());
        }

        return retval;

    }

    /**
     * This Method is used to add a key value pair for a patient
     * 
     * @param providerNo providers Number entering the key value pair
     * @param demo       Demographic number of the patient that the key/value pair
     *                   is for
     * @param value      The value for this key
     */
    @Override
    public void addKey(String providerNo, Integer demo, String key, String value) {
        DemographicExt demographicExt = new DemographicExt();
        demographicExt.setProviderNo(providerNo);
        demographicExt.setDemographicNo(demo);
        demographicExt.setKey(key);
        demographicExt.setValue(value);
        demographicExt.setDateCreated(new java.util.Date());
        persist(demographicExt);
    }

    @Override
    public void addKey(String providerNo, Integer demo, String key, String newValue, String oldValue) {
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue != null && !oldValue.equalsIgnoreCase(newValue)) {
            DemographicExt demographicExt = new DemographicExt();
            demographicExt.setProviderNo(providerNo);
            demographicExt.setDemographicNo(demo);
            demographicExt.setKey(key);
            demographicExt.setValue(newValue);
            demographicExt.setDateCreated(new java.util.Date());
            persist(demographicExt);

        }
    }

    List<String[]> hashtable2ArrayList(Map<String, String> h) {
        Iterator<String> e = h.keySet().iterator();
        List<String[]> arr = new ArrayList<String[]>();
        while (e.hasNext()) {
            String key = e.next();
            String val = h.get(key);
            String[] sArr = new String[] { key, val };
            arr.add(sArr);
        }

        return arr;
    }

    @Override
    public List<String[]> getListOfValuesForDemo(Integer demo) {
        return hashtable2ArrayList(getAllValuesForDemo(demo));
    }

    @Override
    public String getValueForDemoKey(Integer demo, String key) {
        DemographicExt ext = this.getDemographicExt(demo, key);
        if (ext != null) {
            return ext.getValue();
        }
        return null;
    }

    @Override
    public List<Integer> findDemographicIdsByKeyVal(DemographicExtKey demographicExtKey, String val) {
        return findDemographicIdsByKeyVal(demographicExtKey.getKey(), val);
    }

    /**
     * @Deprecated: use alternate method with DemographicExtKey parameter
     * @param key
     * @param val
     * @return
     */
    @Deprecated
    @Override
    public List<Integer> findDemographicIdsByKeyVal(String key, String val) {
        Query query = entityManager
                .createQuery("SELECT distinct d.demographicNo from DemographicExt d where d.key=? and d.value=?");
        query.setParameter(0, key);
        query.setParameter(1, val);

        return query.getResultList();
    }

    @Override
    public List<DemographicExt> getMultipleDemographicExtKeyForDemographicNumbersByProviderNumber(
            final DemographicExtKey demographicExtKey,
            final Collection<Integer> demographicNumbers,
            final String midwifeNumber) {
        String sql = "select x from DemographicExt x where x.demographicNo IN (?1) "
                + "and x.key = ?2 "
                + "and x.value = ?3";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicNumbers);
        query.setParameter(2, demographicExtKey.getKey());
        query.setParameter(3, midwifeNumber);

        return query.getResultList();
    }

    @Override
    public List<Integer> getDemographicNumbersByDemographicExtKeyAndProviderNumberAndDemographicLastNameRegex(
            final DemographicExtKey key,
            final String providerNumber,
            final String lastNameRegex) {
        String sql = "select d.demographic_no from demographic d, demographicExt e "
                + "where e.key_val = ? "
                + "and e.value = ? "
                + "and d.demographic_no = e.demographic_no "
                + "and d.last_name REGEXP ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, key.getKey());
        query.setParameter(2, providerNumber);
        query.setParameter(3, lastNameRegex);

        return query.getResultList();
    }
}
