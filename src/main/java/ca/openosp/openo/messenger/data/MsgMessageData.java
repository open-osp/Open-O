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


package ca.openosp.openo.messenger.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.openosp.openo.messenger.util.MsgStringQuote;
import ca.openosp.openo.commn.dao.MessageListDao;
import ca.openosp.openo.commn.dao.MessageTblDao;
import ca.openosp.openo.commn.dao.OscarCommLocationsDao;
import ca.openosp.openo.commn.model.MessageList;
import ca.openosp.openo.commn.model.MessageTbl;
import ca.openosp.openo.commn.model.OscarCommLocations;
import ca.openosp.openo.managers.MessengerGroupManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ca.openosp.openo.messenger.util.Msgxml;

/**
 * @deprecated Use the MessagingManger
 */
@Deprecated
public class MsgMessageData {

    boolean areRemotes = false;
    boolean areLocals = false;
    private java.util.ArrayList<MsgProviderData> providerArrayList;
    private int currentLocationId = 0;

    private String messageSubject;
    private String messageDate;
    private String messageTime;

    MessageTblDao messageTblDao = SpringUtils.getBean(MessageTblDao.class);
    MessageListDao messageListDao = SpringUtils.getBean(MessageListDao.class);
    OscarCommLocationsDao oscarCommLocationsDao = SpringUtils.getBean(OscarCommLocationsDao.class);
    MessengerGroupManager messengerGroupManager = SpringUtils.getBean(MessengerGroupManager.class);

    public MsgMessageData() {
    }

    public MsgMessageData(String msgID) {
        MessageTbl message = null;
        if (msgID != null && !msgID.isEmpty()) {
            message = messageTblDao.find(Integer.parseInt(msgID));
        }
        if (message != null) {
            SimpleDateFormat date = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
            this.messageSubject = message.getSubject();
            this.messageDate = date.format(message.getDate());
            this.messageTime = time.format(message.getTime());
        }
    }

    public int getCurrentLocationId() {
        if (currentLocationId == 0) {
            List<OscarCommLocations> oscarCommLocations = oscarCommLocationsDao.findByCurrent1(1);
            Integer oscarCommLocationsID = null;

            if (oscarCommLocations != null) {
                oscarCommLocationsID = oscarCommLocations.get(0).getId();
            }

            if (oscarCommLocationsID != null) {
                currentLocationId = oscarCommLocationsID;
            }
        }
        return currentLocationId;
    }

    /************************************************************************
     * getDups4
     * @param strarray is a String Array,
     * @return turns a String Array
     */
    public String[] getDups4(String[] strarray) {
        List<String> arrayList = new ArrayList<String>(Arrays.asList(strarray));
        Set<String> hashSet = new HashSet<String>(arrayList);
        String[] outputArray = new String[hashSet.size()];
        return hashSet.toArray(outputArray);
    }

    public String createSentToString(java.util.ArrayList<MsgProviderData> providerList) {
        StringBuilder stringBuilder = new StringBuilder("");
        String comma = "";
        for (MsgProviderData provider : providerList) {
            stringBuilder.append(comma);
            if (!provider.getFirstName().isEmpty()) {
                stringBuilder.append(provider.getFirstName());
                stringBuilder.append(" ");
            }

            if (!provider.getLastName().isEmpty()) {
                stringBuilder.append(provider.getLastName());
            }

            comma = ", ";
        }

        stringBuilder.append(".");
        return stringBuilder.toString();
    }

    @Deprecated
    public String sendMessage(String message, String subject, String userName, String sentToWho, String userNo, String[] providers) {
        String messageid = null;
        MsgStringQuote str = new MsgStringQuote();


        MessageTbl mt = new MessageTbl();
        mt.setDate(new Date());
        mt.setTime(new Date());
        mt.setMessage(messageid);
        mt.setSubject(subject);
        mt.setSentBy(userName);
        mt.setSentTo(sentToWho);
        mt.setSentByNo(userNo);

        messageTblDao.persist(mt);
        int msgid = mt.getId();


        messageid = String.valueOf(msgid);
        for (int i = 0; i < providers.length; i++) {
            MessageList ml = new MessageList();
            ml.setMessage(Integer.parseInt(messageid));
            ml.setProviderNo(providers[i]);
            ml.setStatus("new");
            messageListDao.persist(ml);

        }


        return messageid;
    }

    public String sendMessageReview(String message, String subject, String userName, String sentToWho, String userNo, ArrayList<MsgProviderData> providers, String attach, String pdfAttach, Integer type, String typeLink) {
        MsgStringQuote str = new MsgStringQuote();


        if (attach != null) {
            attach = str.q(attach);
        }

        if (pdfAttach != null) {
            pdfAttach = str.q(pdfAttach);
        }

        sentToWho = org.apache.commons.lang.StringEscapeUtils.escapeSql(sentToWho);
        userName = org.apache.commons.lang.StringEscapeUtils.escapeSql(userName);

        MessageTbl mt = new MessageTbl();
        mt.setDate(new Date());
        mt.setTime(new Date());
        mt.setMessage(message);
        mt.setSubject(subject);
        mt.setSentBy(userName);
        mt.setSentTo(sentToWho);
        mt.setSentByNo(userNo);
        mt.setSentByLocation(getCurrentLocationId());
        mt.setAttachment(attach);
        mt.setType(type);
        mt.setType_link(typeLink);
        if (pdfAttach != null) {
            mt.setPdfAttachment(pdfAttach.getBytes());
        }
        messageTblDao.persist(mt);


        String messageid = String.valueOf(mt.getId());


        for (MsgProviderData providerData : providers) {
            MessageList ml = new MessageList();
            ml.setMessage(Integer.parseInt(messageid));
            ml.setProviderNo(providerData.getId().getContactId());
            ml.setStatus("new");
            ml.setRemoteLocation(providerData.getId().getClinicLocationNo());
            messageListDao.persist(ml);
        }


        return messageid;

    }

    ////////////////////////////////////////////////////////////////////////////
    public String sendMessage2(String message, String subject, String userName, String sentToWho, String userNo, ArrayList<MsgProviderData> providers, String attach, String pdfAttach, Integer type) {

        MsgStringQuote str = new MsgStringQuote();


        if (attach != null) {
            attach = str.q(attach);
        }

        if (pdfAttach != null) {
            pdfAttach = str.q(pdfAttach);
        }

        sentToWho = org.apache.commons.lang.StringEscapeUtils.escapeSql(sentToWho);
        userName = org.apache.commons.lang.StringEscapeUtils.escapeSql(userName);

        MessageTbl mt = new MessageTbl();
        mt.setDate(new Date());
        mt.setTime(new Date());
        mt.setMessage(message);
        mt.setSubject(subject);
        mt.setSentBy(userName);
        mt.setSentTo(sentToWho);
        mt.setSentByNo(userNo);
        mt.setSentByLocation(getCurrentLocationId());
        mt.setAttachment(attach);
        mt.setType(type);
        if (pdfAttach != null) {
            mt.setPdfAttachment(pdfAttach.getBytes());
        }
        messageTblDao.persist(mt);

        String messageid = String.valueOf(mt.getId());

        for (MsgProviderData providerData : providers) {
            MessageList ml = new MessageList();
            ml.setMessage(Integer.parseInt(messageid));
            ml.setProviderNo(providerData.getId().getContactId());
            ml.setStatus("new");
            ml.setRemoteLocation(providerData.getId().getClinicLocationNo());
            ml.setDestinationFacilityId(providerData.getId().getFacilityId());
            messageListDao.persist(ml);
        }
        return messageid;
    }

    public java.util.ArrayList<MsgProviderData> getProviderStructure(LoggedInInfo loggedInInfo, String[] providerArray) {
        providerArrayList = new java.util.ArrayList<MsgProviderData>();
        ContactIdentifier contactIdentifier = null;

        for (String providerId : providerArray) {
            contactIdentifier = new ContactIdentifier(providerId);
            MsgProviderData msgProviderData = messengerGroupManager.getMemberData(loggedInInfo, contactIdentifier);
            providerArrayList.add(msgProviderData);
        }

        return providerArrayList;
    }

    public java.util.ArrayList<MsgProviderData> getRemoteProvidersStructure() {
        java.util.ArrayList<MsgProviderData> arrayList = new java.util.ArrayList<MsgProviderData>();
        if (providerArrayList != null) {
            for (int i = 0; i < providerArrayList.size(); i++) {
                MsgProviderData providerData = providerArrayList.get(i);
                if (providerData.getId().getClinicLocationNo() > 0 && providerData.getId().getClinicLocationNo() != getCurrentLocationId()) {
                    arrayList.add(providerData);
                }
            }
        }
        return arrayList;
    }

    public String getRemoteNames(java.util.ArrayList<MsgProviderData> arrayList) {

        String[] arrayOfLocations = new String[arrayList.size()];
        String[] sortedArrayOfLocations;
        MsgProviderData providerData;
        for (int i = 0; i < arrayList.size(); i++) {
            providerData = arrayList.get(i);
            arrayOfLocations[i] = providerData.getId().getClinicLocationNo() + "";
        }
        sortedArrayOfLocations = getDups4(arrayOfLocations);

        java.util.ArrayList<ArrayList<String>> vectOfSortedProvs = new java.util.ArrayList<ArrayList<String>>();

        for (int i = 0; i < sortedArrayOfLocations.length; i++) {
            java.util.ArrayList<String> sortedProvs = new java.util.ArrayList<String>();
            for (int j = 0; j < arrayList.size(); j++) {
                providerData = arrayList.get(j);
                if (providerData.getId().getClinicLocationNo() == Integer.parseInt(sortedArrayOfLocations[i])) {

                    sortedProvs.add(providerData.getId().getContactId());
                }
            }
            vectOfSortedProvs.add(sortedProvs);
        }

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < sortedArrayOfLocations.length; i++) {
            //for each location get there address book and there locationDesc
            String theAddressBook = new String();
            String theLocationDesc = new String();
            OscarCommLocations oscarCommLocations = oscarCommLocationsDao.find(sortedArrayOfLocations[i]);
            if (oscarCommLocations != null) {
                theLocationDesc = oscarCommLocations.getLocationDesc();
                theAddressBook = oscarCommLocations.getAddressBook();
            }

            Document xmlDoc = Msgxml.parseXML(theAddressBook);

            if (xmlDoc != null) {
                ArrayList<String> sortedProvs = vectOfSortedProvs.get(i);

                stringBuffer.append("<br/><br/>Providers at " + theLocationDesc + " receiving this message: <br/> ");

                Element addressBook = xmlDoc.getDocumentElement();
                NodeList lst = addressBook.getElementsByTagName("address");

                for (int z = 0; z < sortedProvs.size(); z++) {

                    String providerNo = sortedProvs.get(z);

                    for (int j = 0; j < lst.getLength(); j++) {
                        Node currNode = lst.item(j);
                        Element elly = (Element) currNode;


                        if (providerNo.equals(elly.getAttribute("id"))) {
                            j = lst.getLength();
                            stringBuffer.append(elly.getAttribute("desc") + ". ");
                        }
                    }

                }//for

                stringBuffer.append(" ) ");

            }//if

        }//for

        return stringBuffer.toString();
    }

    public String getSubject(String msgID) {
        MessageTbl message = messageTblDao.find(msgID);
        String subject = "error: subject not found!";
        if (message != null && message.getSubject() != null) {
            subject = message.getSubject();
        }
        return subject;
    }

    public String getSubject() {
        return this.messageSubject;
    }

    public String getDate() {
        return this.messageDate;
    }

    public String getTime() {
        return this.messageTime;
    }
}
