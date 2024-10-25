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
package oscar.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import oscar.dao.OscarSuperDao;

public class OscarSuperManagerImpl implements OscarSuperManager {

    private Map<String, OscarSuperDao> oscarDaoMap = new TreeMap<String, OscarSuperDao>();
    private OscarSuperDao providerSuperDao;

    @Override
    public void setProviderSuperDao(OscarSuperDao providerDao) {
        this.providerSuperDao = providerDao;
    }

    @Override
    public void init() {
        oscarDaoMap.put("providerDao", providerSuperDao);

        for (String daoName : oscarDaoMap.keySet()) {
            if (oscarDaoMap.get(daoName) == null) {
                throw new IllegalStateException(
                        "Dao with specified name has not been injected into OscarSuperManager: " + daoName);
            }
        }
    }

    @Override
    public List<Map<String, Object>> find(String daoName, String queryName, Object[] params) {
        return getDao(daoName).executeSelectQuery(queryName, params);
    }

    @Override
    public List<Object> populate(String daoName, String queryName, Object[] params) {
        return getDao(daoName).executeRowMappedSelectQuery(queryName, params);
    }

    protected OscarSuperDao getDao(String daoName) {
        OscarSuperDao oscarSuperDao = oscarDaoMap.get(daoName);
        if (oscarSuperDao != null) {
            return oscarSuperDao;
        }
        throw new IllegalArgumentException("OscarSuperManager contains no dao with specified name: " + daoName);
    }
}
