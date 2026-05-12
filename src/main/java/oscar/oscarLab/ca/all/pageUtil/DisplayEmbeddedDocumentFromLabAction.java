package oscar.oscarLab.ca.all.pageUtil;

import com.twelvemonkeys.lang.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;
import org.oscarehr.common.dao.SystemPreferencesDao;
import org.oscarehr.common.model.SystemPreferences;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.PATHL7Handler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayEmbeddedDocumentFromLabAction extends DownloadAction {

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    @Override
    protected StreamInfo getStreamInfo(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String labNo = httpServletRequest.getParameter("labNo");
        String segment = httpServletRequest.getParameter("segment");
        String group = httpServletRequest.getParameter("group");
        String legacy = httpServletRequest.getParameter("legacy");

        SystemPreferencesDao systemPreferencesDao = SpringUtils.getBean(SystemPreferencesDao.class);

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(httpServletRequest), "_lab", "r", null)) {
            throw new SecurityException("missing required security object (_lab)");
        }

        MessageHandler handler = Factory.getHandler(labNo);

        String result;
        if("true".equals(legacy)) {
            result = ((PATHL7Handler)handler).getLegacyOBXResult(Integer.parseInt(segment), Integer.parseInt(group));
        } else {
            result = handler.getOBXResult(Integer.parseInt(segment), Integer.parseInt(group));
        }

        byte[] decodedData = Base64.decodeBase64(result);
        
        SystemPreferences labMaxPref = systemPreferencesDao.findPreferenceByName(SystemPreferences.LAB_DISPLAY_PREFERENCE_KEYS.lab_pdf_max_size);
        BigDecimal labMaxSize = new BigDecimal(3145728); //restrict file size to 3MB by default
        
        if(labMaxPref != null && !StringUtil.isEmpty(labMaxPref.getValue())){
            Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
            Pattern unitPattern = Pattern.compile("[KMG]?[B]", Pattern.CASE_INSENSITIVE);
            
            Matcher numberMatcher = numberPattern.matcher(labMaxPref.getValue());
            Matcher unitMatcher = unitPattern.matcher(labMaxPref.getValue());
            if(numberMatcher.find() && unitMatcher.find()){
                labMaxSize = new BigDecimal(labMaxPref.getValue().substring(numberMatcher.start(), numberMatcher.end()));
                char unit = labMaxPref.getValue().charAt(unitMatcher.start());
                
                if("K".equalsIgnoreCase("" + unit)){
                    labMaxSize = labMaxSize.multiply(new BigDecimal(1024));
                }
                if("M".equalsIgnoreCase("" + unit)){
                    labMaxSize = labMaxSize.multiply(new BigDecimal(1024 * 1024));
                }
                if("G".equalsIgnoreCase("" + unit)){
                    labMaxSize = labMaxSize.multiply(new BigDecimal(1024 * 1024 *1024));
                }
            }
        }

        if(labMaxSize.compareTo(new BigDecimal(decodedData.length)) > 0) {
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-disposition", "inline; filename=\"Lab-" + labNo + ".pdf\"");
            httpServletResponse.setContentLength(decodedData.length);

            try (ServletOutputStream outputStream = httpServletResponse.getOutputStream()) {
                outputStream.write(decodedData);
            } catch (IOException e) {
                MiscUtils.getLogger().error("Error loading pdf", e);
            }
        }
        return null;
    } 
}
