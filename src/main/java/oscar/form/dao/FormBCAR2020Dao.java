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
package oscar.form.dao;

import org.oscarehr.common.dao.AbstractDao;
import oscar.form.model.FormBCAR2020;

import javax.persistence.Query;

public class FormBCAR2020Dao extends AbstractDao<FormBCAR2020> {
    public FormBCAR2020Dao() { super(FormBCAR2020.class); }

    public Integer getLatestActiveFormIdByDemographic(Integer demographicNo) {
        Integer latestFormId = null;
        String sql = "select max(frm.formId) from FormBCAR2020 frm WHERE frm.demographicNo = :demographicNo and frm.active = true";
        Query query = entityManager.createQuery(sql);
        query.setParameter("demographicNo", demographicNo);
        Object result = query.getSingleResult();
        if (result instanceof Integer) {
            latestFormId = (Integer)result;
        }

        return latestFormId;
    }
}
