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
package org.oscarehr.common.dao;

import org.oscarehr.common.model.SystemPreferences;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@SuppressWarnings("unchecked")
public class SystemPreferencesDao extends AbstractDao<SystemPreferences>
{
    public SystemPreferencesDao() { super(SystemPreferences.class); }

    public SystemPreferences findPreferenceByName(String name)
    {
        Query query = entityManager.createQuery("FROM SystemPreferences sp WHERE sp.name = :name");
        query.setParameter("name", name);

        List<SystemPreferences> results = query.getResultList();
        if (!results.isEmpty())
        {
            return results.get(0);
        }

        return null;
    }
    
    public List<SystemPreferences> findPreferencesByNames(List<String> names) {
        Query query = entityManager.createQuery("FROM SystemPreferences sp WHERE sp.name IN (:names)");
        query.setParameter("names", names);

        List<SystemPreferences> results = query.getResultList();
        return results;
    }

    /**
     * Gets a map of system preference values
     * 
     * @param keys List of preference keys to search for in the database
     * @return Map of preference keys with their associated boolean value
     */
    public Map<String, Boolean> findByKeysAsMap(List<String> keys) {
        List<SystemPreferences> preferences = findPreferencesByNames(keys);
        Map<String, Boolean> preferenceMap = new HashMap<String, Boolean>();
        
        for (SystemPreferences preference : preferences) {
            preferenceMap.put(preference.getName(), preference.getValueAsBoolean());
        }
        
        return preferenceMap;
    }

    /**
     * Gets a map of system preferences with the preference name as the key
     * @param keys List of keys to get the preferences for
     * @return A map of SystemPreferences with the preference name as the key
     */
    public Map<String, SystemPreferences> findByKeysAsPreferenceMap(List<String> keys) {
        Map<String, SystemPreferences> preferenceMap = new HashMap<>();
        
        List<SystemPreferences> preferences = findPreferencesByNames(keys);
        
        for (SystemPreferences preference : preferences) {
            preferenceMap.put(preference.getName(), preference);
        }
        
        return preferenceMap;
    }
    
    public boolean isReadBooleanPreference(String name) {
        SystemPreferences preference = findPreferenceByName(name);
        return (preference != null && Boolean.parseBoolean(preference.getValue()));
    }

    public boolean isPreferenceValueEquals(String preferenceName, String trueValueStr) {
        SystemPreferences preference = findPreferenceByName(preferenceName);
        return (preference != null && trueValueStr.equals(preference.getValue()));
    }
}
