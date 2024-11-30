package org.oscarehr.managers;

import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.common.model.enumerator.UserPropertyKey;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * This class should only be used to fetch preference values by
 * logged in user.
 * Different classes should be created or used to fetch other property entities
 */
@Service
public class UserPropertyManager {

	@Autowired
	private UserPropertyDAO userPropertyDao;

	/**
	 * LoggedInInfo and UserProperty ENUM required to fetch single user
	 * property for the logged in provider ONLY.
	 * This method cannot be used to fetch user preferences for 3rd party providers.
	 * @param loggedInInfo logged-in user security object
	 * @param property UserProperty constant
	 * @return a single UserProperty object
	 */
	public UserProperty getUserProperty(LoggedInInfo loggedInInfo, UserPropertyKey property) {
		String providerNumber = loggedInInfo.getLoggedInProviderNo();
		UserProperty userProperty = null;
		if(providerNumber != null) {
			userProperty = userPropertyDao.getProp(providerNumber, property);
		}
		return userProperty;
	}

	public Map<String, String> getAllUserProperties(LoggedInInfo loggedInInfo) {
		String providerNumber = loggedInInfo.getLoggedInProviderNo();
		Map<String, String> userProperties = Collections.emptyMap();
		if(providerNumber != null) {
			userProperties = userPropertyDao.getProviderPropertiesAsMap(providerNumber);
		}
		return userProperties;
	}

}
