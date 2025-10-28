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
package ca.openosp.openo.commn.model;

import javax.persistence.*;

@Entity
@Table(name = "fax_config")
public class FaxConfig extends AbstractModel<Integer> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String url = "";
    private String siteUser = "";
    private String passwd = "";
    private String faxUser = "";
    private String faxPasswd = "";
    private String faxNumber = "";
    private String senderEmail = "";

    @Column(columnDefinition = "boolean default false")
    private boolean active;
    private Integer queue = 0;
    private String accountName = "";

    @Column(columnDefinition = "boolean default true")
    private boolean download;

    @Override
    public Integer getId() {
        return Id;
    }


    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }


    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * @return the siteUser
     */
    public String getSiteUser() {
        return siteUser;
    }


    /**
     * @param siteUser the siteUser to set
     */
    public void setSiteUser(String siteUser) {
        this.siteUser = siteUser;
    }


    /**
     * @return the passwd
     */
    public String getPasswd() {
        return passwd;
    }


    /**
     * @param passwd the passwd to set
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


    /**
     * @return the faxUser
     */
    public String getFaxUser() {
        return faxUser;
    }


    /**
     * @param faxUser the faxUser to set
     */
    public void setFaxUser(String faxUser) {
        this.faxUser = faxUser;
    }


    /**
     * @return the faxPasswd
     */
    public String getFaxPasswd() {
        return faxPasswd;
    }


    /**
     * @param faxPasswd the faxPasswd to set
     */
    public void setFaxPasswd(String faxPasswd) {
        this.faxPasswd = faxPasswd;
    }


    /**
     * @return the faxNumber
     */
    public String getFaxNumber() {
        return faxNumber;
    }


    /**
     * @param faxNumber the faxNumber to set
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }


    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        Id = id;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Sender email as required by fax gateway integration
     */
    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * Sender email as required by fax gateway integration
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }


    /**
     * @return the active
     */
    public boolean getActive() {
        return active;
    }


    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * @return the queue
     */
    public Integer getQueue() {
        return queue;
    }


    /**
     * @param queue the queue to set
     */
    public void setQueue(Integer queue) {
        this.queue = queue;
    }

    public String getAccountName() {
        if (accountName == null || accountName.isEmpty()) {
            return getFaxUser();
        }

        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isDownload() {
        return download;
    }


    public void setDownload(boolean download) {
        this.download = download;
    }
}
