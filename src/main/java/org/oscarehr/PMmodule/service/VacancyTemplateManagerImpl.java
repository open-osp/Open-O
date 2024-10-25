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

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.CriteriaDao;
import org.oscarehr.PMmodule.dao.CriteriaSelectionOptionDao;
import org.oscarehr.PMmodule.dao.CriteriaTypeDao;
import org.oscarehr.PMmodule.dao.CriteriaTypeOptionDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.dao.VacancyTemplateDao;
import org.oscarehr.PMmodule.model.Criteria;
import org.oscarehr.PMmodule.model.CriteriaSelectionOption;
import org.oscarehr.PMmodule.model.CriteriaType;
import org.oscarehr.PMmodule.model.CriteriaTypeOption;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.PMmodule.model.VacancyTemplate;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class VacancyTemplateManagerImpl implements VacancyTemplateManager{
	

}
