package org.oscarehr.managers;

import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ProfessionalSpecialistsManager implements Serializable {

	@Autowired
	private ProfessionalSpecialistDao professionalSpecialistDao;

	@Autowired
	private SecurityInfoManager securityInfoManager;

	public ProfessionalSpecialistsManager() {
		// default
	}

	public ProfessionalSpecialist getProfessionalSpecialist(LoggedInInfo loggedInInfo, int id) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_con", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_con)");
		}
		return professionalSpecialistDao.find(id);
	}

	public List<ProfessionalSpecialist> searchProfessionalSpecialist(LoggedInInfo loggedInInfo, String keyword) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_con", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_con)");
		}
		return professionalSpecialistDao.search(keyword);
	}
}
