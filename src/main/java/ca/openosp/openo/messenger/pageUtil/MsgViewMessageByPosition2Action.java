package ca.openosp.openo.messenger.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import ca.openosp.openo.commn.dao.ProviderDataDao;
import ca.openosp.openo.commn.model.ProviderData;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;

public class MsgViewMessageByPosition2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute() throws IOException, ServletException {

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_msg", "r", null)) {
            throw new SecurityException("missing required sec object (_msg)");
        }

        // Extract attributes we will need
        String provNo = (String) request.getSession().getAttribute("user");

        if (request.getSession().getAttribute("msgSessionBean") == null) {
            MsgSessionBean bean = new MsgSessionBean();
            bean.setProviderNo(provNo);

            ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
            ProviderData pd = dao.findByProviderNo(provNo);
            if (pd != null) {
                bean.setUserName(pd.getFirstName() + " " + pd.getLastName());
            }
            request.getSession().setAttribute("msgSessionBean", bean);
        }

        String orderBy = request.getParameter("orderBy") == null ? "date" : request.getParameter("orderBy");
        String messagePosition = request.getParameter("messagePosition");
        String demographic_no = request.getParameter("demographic_no");

        /*
        MsgDisplayMessagesBean displayMsgBean = new MsgDisplayMessagesBean();
        ParameterActionForward actionforward = new ParameterActionForward(mapping.findForward("success"));

        String sql = "select m.messageid  "
                + "from  messagetbl m, msgDemoMap mapp where mapp.demographic_no = '" + demographic_no + "'  "
                + "and m.messageid = mapp.messageID  order by " + displayMsgBean.getOrderBy(orderBy);
        FormsDao daos = SpringUtils.getBean(FormsDao.class);
        try {
            Integer messageId = (Integer) daos.runNativeQueryWithOffset(sql, Integer.parseInt(messagePosition));
            actionforward.addParameter("messageID", messageId.toString());
            actionforward.addParameter("from", "encounter");
            actionforward.addParameter("demographic_no", demographic_no);
            actionforward.addParameter("messagePostion", messagePosition);
        } catch (Exception e) {
            MiscUtils.getLogger().error("error", e);
        }


        return actionforward;
        */
        return SUCCESS;
    }
}
