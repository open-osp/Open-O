package org.oscarehr.managers;

import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.common.model.enumerator.UserPropertyKey;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
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

		if(userProperty == null) {
			userProperty = new UserProperty();
			userProperty.setName(property.getName());
		}
		return userProperty;
	}

	/**
	 * returns all user properties for the logged in provider as a HashMap
	 * @param loggedInInfo logged in object from session.
	 * @return Map<key, value>
	 */
	public Map<String, String> getAllUserProperties(LoggedInInfo loggedInInfo) {
		String providerNumber = loggedInInfo.getLoggedInProviderNo();
		Map<String, String> userProperties = new HashMap<>(Collections.emptyMap());

		/* insert ALL potential values if active or not.
		 * This makes the code tidier without null checks everywhere.
		 */
		for(UserPropertyKey userPropertyKey : UserPropertyKey.values()) {
			userProperties.put(userPropertyKey.getName(), "");
		}

		// overwrite mapped values with actual user set values.
		if(providerNumber != null) {
			userProperties.putAll(userPropertyDao.getProviderPropertiesAsMap(providerNumber));
		}
		return userProperties;
	}

	/**
	 * Retrieves the encounter note password for the logged-in user if the feature
	 * is enabled and a valid password exists.
	 *
	 * @param loggedInInfo the logged-in user's security information
	 * @return the encounter note password if the feature is enabled and a valid password is set, otherwise null
	 */
	public String getEncounterNotePassword(LoggedInInfo loggedInInfo) {
		UserProperty userProperty = getUserProperty(loggedInInfo, UserPropertyKey.CASEMGMT_NOTE_PASSWORD_ENABLED);
		if (userProperty != null && userProperty.isChecked()) {
			userProperty = getUserProperty(loggedInInfo, UserPropertyKey.CASEMGMT_NOTE_PASSWORD);
			if (userProperty != null && userProperty.getValue() != null && !userProperty.getValue().trim().isEmpty()) {
				return userProperty.getValue();
			}
		}
		return null;
	}

}
