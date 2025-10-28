//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package ca.openosp.openo.messenger.tld;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import ca.openosp.openo.PMmodule.service.ProviderManager;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.commn.model.Security;
import ca.openosp.openo.managers.FacilityManager;
import ca.openosp.openo.managers.MessagingManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;

/**
 * Enhanced JSP tag for displaying detailed new message counts with categorization.
 * 
 * <p>This tag provides a more sophisticated message indicator than MsgNewMessagesTag,
 * showing separate counts for demographic messages, integrator messages, and total
 * new messages. It displays the counts as a superscript notification badge with
 * hover tooltips explaining each count.</p>
 * 
 * <p>The tag generates HTML that shows:</p>
 * <ul>
 *   <li>Demographic message count - Messages linked to patient demographics</li>
 *   <li>Integrator message count - Cross-facility messages (if enabled)</li>
 *   <li>Total message count - Combined count of all new messages</li>
 * </ul>
 * 
 * <p>Output format examples:</p>
 * <ul>
 *   <li>With new messages: {@code <span class='tabalert'>text<sup>5|2/7</sup></span>}</li>
 *   <li>Without new messages: {@code <span>text</span>}</li>
 * </ul>
 * 
 * <p>The format "5|2/7" represents: 5 demographic | 2 integrator / 7 total messages.
 * If the facility doesn't have integrator enabled, only demographic/total is shown.</p>
 * 
 * <p>Usage in JSP:</p>
 * <pre>
 * &lt;msg:newMessage providerNo="${user}"&gt;
 *   Message Text
 * &lt;/msg:newMessage&gt;
 * </pre>
 * 
 * <p>Note: This tag uses EVAL_BODY_INCLUDE to process body content, allowing the
 * tag to wrap around text that should be highlighted when new messages exist.</p>
 * 
 * @version 2.0
 * @since 2003
 * @see MsgNewMessagesTag
 * @see MessagingManager
 * @see FacilityManager
 */
public class MsgNewMessageTag extends TagSupport {

    /**
     * Service for managing messages and message counts.
     */
    private static MessagingManager messagingManager = SpringUtils.getBean(MessagingManager.class);
    
    /**
     * Service for managing provider information.
     */
    private static ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    
    /**
     * Service for managing facility settings and integrator configuration.
     */
    private static FacilityManager facilityManager = SpringUtils.getBean(FacilityManager.class);

    /**
     * The provider number to check for messages.
     */
    private String providerNo;
    
    /**
     * Total count of new messages for the provider.
     */
    private int numNewMessages;
    
    /**
     * Count of new messages linked to patient demographics.
     */
    private int numNewDemographicMessages;
    
    /**
     * Count of new messages from the integrator system.
     */
    private int numIntegratedMessages;
    
    /**
     * Session information for the logged-in provider.
     */
    private LoggedInInfo loggedInInfo;

    /**
     * Constructs a new message tag with initialized counters.
     * 
     * <p>Initializes all message counts to zero and creates a new
     * LoggedInInfo object for tracking session information.</p>
     */
    public MsgNewMessageTag() {
        numNewMessages = 0;
        numNewDemographicMessages = 0;
        numIntegratedMessages = 0;
        loggedInInfo = new LoggedInInfo();
    }

    /**
     * Sets the provider number for message counting.
     * 
     * <p>This method is called by the JSP container to set the provider
     * whose messages should be counted. The provider number is used to
     * query the messaging system for unread message counts.</p>
     * 
     * @param providerNo1 String the unique identifier of the provider
     */
    public void setProviderNo(String providerNo1) {
        providerNo = providerNo1;
    }

    /**
     * Gets the provider number being checked.
     * 
     * @return String the provider number
     */
    public String getProviderNo() {
        return providerNo;
    }

    /**
     * Processes the opening tag and begins the message indicator output.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Retrieves the provider information from the provider manager</li>
     *   <li>Sets up the logged-in info context for the provider</li>
     *   <li>Counts total new messages in the provider's inbox</li>
     *   <li>Counts demographic-linked messages separately</li>
     *   <li>Counts integrator messages if facility has it enabled</li>
     *   <li>Opens a span tag with 'tabalert' class if messages exist</li>
     * </ol>
     * 
     * <p>The 'tabalert' CSS class is applied to highlight the wrapped content
     * when new messages are present, providing visual notification to the user.</p>
     * 
     * <p>Note: Returns EVAL_BODY_INCLUDE to process the tag's body content,
     * which differs from MsgNewMessagesTag that returns SKIP_BODY.</p>
     * 
     * @return int EVAL_BODY_INCLUDE to process body content
     * @throws JspException if there's an error during processing
     */
    public int doStartTag() throws JspException {

        // Set up provider context for message queries
        Provider provider = providerManager.getProvider(providerNo);
        loggedInInfo.setLoggedInProvider(provider);
        loggedInInfo.setLoggedInSecurity(new Security());

        // Count different categories of new messages
        numNewMessages = messagingManager.getMyInboxMessageCount(loggedInInfo, providerNo, false);
        numNewDemographicMessages = messagingManager.getMyInboxMessageCount(loggedInInfo, providerNo, true);

        // Check for integrator messages if facility supports it
        if (facilityManager.getDefaultFacility(loggedInInfo).isIntegratorEnabled()) {
            numIntegratedMessages = messagingManager.getMyInboxIntegratorMessagesCount(loggedInInfo, providerNo);
        }

        try {
            JspWriter out = super.pageContext.getOut();
            // Apply alert styling if there are new messages
            if (numNewMessages > 0) {
                out.print("<span class='tabalert'>");
            } else {
                out.print("<span>");
            }
        } catch (Exception p) {
            MiscUtils.getLogger().error("Error", p);
        }
        return (EVAL_BODY_INCLUDE);
    }

    /**
     * Processes the closing tag and completes the message indicator output.
     * 
     * <p>This method generates the message count badge as a superscript element
     * showing categorized message counts. The output format depends on whether
     * new messages exist and if the integrator is enabled.</p>
     * 
     * <p>Output formats:</p>
     * <ul>
     *   <li>With integrator: {@code <sup>5|2/7</sup>} (demographic|integrator/total)</li>
     *   <li>Without integrator: {@code <sup>5/7</sup>} (demographic/total)</li>
     *   <li>No messages: Closes the span tag only</li>
     * </ul>
     * 
     * <p>Each count is wrapped in a span with:</p>
     * <ul>
     *   <li>Unique ID for JavaScript access (demographicMessageCount, etc.)</li>
     *   <li>Title attribute providing hover tooltip explanation</li>
     * </ul>
     * 
     * <p>Historical note: Comment indicates enhancement by "ronnie 2007-4-26"
     * suggesting this formatting was added as an improvement to the original tag.</p>
     * 
     * @return int EVAL_PAGE to continue processing the rest of the page
     * @throws JspException if there's an error during output
     */
    public int doEndTag() throws JspException {
        // Enhancement added by ronnie 2007-4-26
        try {
            JspWriter out = super.pageContext.getOut();
            if (numNewMessages > 0) {
                // Build the message count badge with tooltips
                StringBuilder stringBuilder = new StringBuilder("<sup>");
                
                // Demographic message count with tooltip
                stringBuilder.append("<span id='demographicMessageCount' title='New Demographic Messages'>");
                stringBuilder.append(numNewDemographicMessages);
                stringBuilder.append("</span>");

                // Integrator message count if enabled
                if (facilityManager.getDefaultFacility(loggedInInfo).isIntegratorEnabled()) {
                    stringBuilder.append("|<span id='integratorMessageCount' title='New Integrator Messages'>");
                    stringBuilder.append(numIntegratedMessages);
                    stringBuilder.append("</span>");
                }

                // Total message count
                stringBuilder.append("/<span id='totalMessageCount' title='Total New Messages'>");
                stringBuilder.append(numNewMessages);
                stringBuilder.append("</span>");

                stringBuilder.append("</sup></span>  ");

                out.print(stringBuilder);
            } else {
                // No messages, just close the span
                out.print("</span>  ");
            }
        } catch (Exception p) {
            MiscUtils.getLogger().error("Error", p);
        }
        return EVAL_PAGE;
    }


}
