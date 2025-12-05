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

import ca.openosp.openo.utility.EncryptionUtils;
import ca.openosp.openo.utility.MiscUtils;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "fax_config")
public class FaxConfig extends AbstractModel<Integer> {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = MiscUtils.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String url = "";
    private String siteUser = "";

    @Column(name = "passwd")
    private String encryptedPasswd = "";

    @Transient
    private String passwd = "";

    private String faxUser = "";

    @Column(name = "faxPasswd")
    private String encryptedFaxPasswd = "";

    @Transient
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
     * @return the passwd (decrypted plain text)
     */
    public String getPasswd() {
        return passwd;
    }


    /**
     * @param passwd the passwd to set (plain text, will be encrypted before persistence)
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
     * @return the faxPasswd (decrypted plain text)
     */
    public String getFaxPasswd() {
        return faxPasswd;
    }


    /**
     * @param faxPasswd the faxPasswd to set (plain text, will be encrypted before persistence)
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

    /**
     * JPA lifecycle callback to decrypt passwords after loading from database.
     * Automatically handles migration of legacy unencrypted passwords.
     */
    @PostLoad
    private void decryptPasswords() {
        this.passwd = decryptPassword(encryptedPasswd);
        this.faxPasswd = decryptPassword(encryptedFaxPasswd);
    }

    /**
     * JPA lifecycle callback to encrypt passwords before persisting to database.
     * Called before both INSERT and UPDATE operations.
     */
    @PrePersist
    @PreUpdate
    private void encryptPasswords() {
        // Only encrypt and overwrite if plaintext password is non-null and non-empty
        if (passwd != null && !passwd.isEmpty()) {
            this.encryptedPasswd = encryptPassword(passwd, "passwd");
        }
        if (faxPasswd != null && !faxPasswd.isEmpty()) {
            this.encryptedFaxPasswd = encryptPassword(faxPasswd, "faxPasswd");
        }
    }

    /**
     * Helper method to decrypt a password field.
     * Handles legacy unencrypted passwords and decryption failures gracefully.
     *
     * @param encryptedValue the encrypted password value from database
     * @return decrypted plain text password, or empty string if decryption fails
     */
    private String decryptPassword(String encryptedValue) {
        try {
            if (encryptedValue != null && !encryptedValue.isEmpty()) {
                if (EncryptionUtils.isEncrypted(encryptedValue)) {
                    return EncryptionUtils.decrypt(encryptedValue);
                } else {
                    // Legacy unencrypted password - use as-is and it will be encrypted on next save
                    return encryptedValue;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to decrypt fax password - possible key rotation or corruption. Field will need to be re-entered.", e);
            // Return empty to force credential re-entry rather than using potentially compromised value
            return "";
        }
        return "";
    }

    /**
     * Helper method to encrypt a password field.
     *
     * @param plainText the plain text password to encrypt
     * @param fieldName the name of the field being encrypted (for error messages)
     * @return encrypted password value
     * @throws RuntimeException if encryption fails
     */
    private String encryptPassword(String plainText, String fieldName) {
        try {
            if (plainText != null && !plainText.isEmpty()) {
                return EncryptionUtils.encrypt(plainText);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt " + fieldName, e);
        }
        return "";
    }
}
