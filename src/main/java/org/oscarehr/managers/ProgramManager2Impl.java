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

package org.oscarehr.managers;

import java.util.List;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.dao.ProviderDefaultProgramDao;
import org.oscarehr.common.model.ProviderDefaultProgram;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class ProgramManager2Impl implements ProgramManager2 {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private ProgramProviderDAO programProviderDAO;

    @Autowired
    private ProviderDefaultProgramDao providerDefaultProgramDao;

    @Override
    public Program getProgram(LoggedInInfo loggedInInfo, Integer programId) {
        return programDao.getProgram(programId);
    }

    @Override
    public List<Program> getAllPrograms(LoggedInInfo loggedInInfo) {
        return programDao.findAll();
    }

    @Override
    public List<ProgramProvider> getAllProgramProviders(LoggedInInfo loggedInInfo) {
        return programProviderDAO.getAllProgramProviders();
    }

    @Override
    public List<ProgramProvider> getProgramDomain(LoggedInInfo loggedInInfo, String providerNo) {
        return programProviderDAO.getProgramProvidersByProvider(providerNo);
    }

    @Override
    public ProgramProvider getCurrentProgramInDomain(LoggedInInfo loggedInInfo) {
        return getCurrentProgramInDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
    }

    @Override
    public ProgramProvider getCurrentProgramInDomain(LoggedInInfo loggedInInfo, String providerNo) {
        ProgramProvider result = null;
        int defProgramId = 0;
        List<ProviderDefaultProgram> rs = providerDefaultProgramDao.getProgramByProviderNo(providerNo);
        if (!rs.isEmpty()) {
            defProgramId = rs.get(0).getProgramId();
            if (defProgramId > 0) {
                result = programProviderDAO.getProgramProvider(providerNo, Long.valueOf(defProgramId));
            }
        }
        return (result);
    }

    @Override
    public void setCurrentProgramInDomain(String providerNo, Integer programId) {

        if (programProviderDAO.getProgramProvider(providerNo, programId.longValue()) != null) {
            providerDefaultProgramDao.setDefaultProgram(providerNo, programId);
        } else {
            throw new RuntimeException("Program not in user's domain");
        }
    }
}
