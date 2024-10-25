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
package com.quatro.service.security;

import java.util.List;

import com.quatro.model.security.Secobjprivilege;
import com.quatro.model.security.Secrole;

public interface RolesManager {

    List<Secrole> getRoles();

    Secrole getRole(String id);

    Secrole getRole(int id);

    Secrole getRoleByRolename(String roleName);

    void save(Secrole secrole);

    void saveFunction(Secobjprivilege secobjprivilege);

    void saveFunctions(Secrole secrole, List newLst, String roleName);

    List<Secobjprivilege> getFunctions(String roleName);

    String getFunctionDesc(String function_code);

    String getAccessDesc(String accessType_code);
}
