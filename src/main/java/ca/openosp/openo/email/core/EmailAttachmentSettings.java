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

package ca.openosp.openo.email.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Data Transfer Object to encapsulate email attachment settings and IDs.
 * Reduces parameter count and centralizes parsing logic for email composition.
 * Used to pass attachment configuration between eForm actions and email composition.
 *
 * @since 2025-11-13
 */
public class EmailAttachmentSettings {
    public final String fdid;
    public final String demographicNo;
    public final String[] attachedEForms;
    public final String[] attachedDocuments;
    public final String[] attachedLabs;
    public final String[] attachedHRMDocuments;
    public final String[] attachedForms;
    public final boolean attachEFormItSelf;
    public final boolean openAfterEmail;
    public final boolean isEmailEncrypted;
    public final boolean isEmailAttachmentEncrypted;
    public final boolean isEmailAutoSend;
    public final boolean deleteEFormAfterEmail;
    public final String emailPDFPassword;
    public final String emailPDFPasswordClue;

    private EmailAttachmentSettings(Builder builder) {
        this.fdid = builder.fdid;
        this.demographicNo = builder.demographicNo;
        this.attachedEForms = builder.attachedEForms;
        this.attachedDocuments = builder.attachedDocuments;
        this.attachedLabs = builder.attachedLabs;
        this.attachedHRMDocuments = builder.attachedHRMDocuments;
        this.attachedForms = builder.attachedForms;
        this.attachEFormItSelf = builder.attachEFormItSelf;
        this.openAfterEmail = builder.openAfterEmail;
        this.isEmailEncrypted = builder.isEmailEncrypted;
        this.isEmailAttachmentEncrypted = builder.isEmailAttachmentEncrypted;
        this.isEmailAutoSend = builder.isEmailAutoSend;
        this.deleteEFormAfterEmail = builder.deleteEFormAfterEmail;
        this.emailPDFPassword = builder.emailPDFPassword;
        this.emailPDFPasswordClue = builder.emailPDFPasswordClue;
    }

    /**
     * Creates a builder from HTTP request parameters.
     * Centralizes all parameter parsing logic in one place.
     *
     * @param request HTTP servlet request containing email parameters
     * @return Builder instance with parameters parsed from request
     */
    public static Builder fromRequest(HttpServletRequest request) {
        Builder builder = new Builder();
        builder.attachEFormItSelf = !"false".equals(request.getParameter("attachEFormToEmail"));
        builder.openAfterEmail = "true".equals(request.getParameter("openEFormAfterSendingEmail"));
        builder.isEmailEncrypted = !"false".equals(request.getParameter("enableEmailEncryption"));
        builder.isEmailAttachmentEncrypted = !"false".equals(request.getParameter("encryptEmailAttachments"));
        builder.isEmailAutoSend = "true".equals(request.getParameter("autoSendEmail"));
        builder.deleteEFormAfterEmail = "true".equals(request.getParameter("deleteEFormAfterSendingEmail"));
        builder.emailPDFPassword = request.getParameter("emailPDFPassword");
        builder.emailPDFPasswordClue = request.getParameter("emailPDFPasswordClue");
        return builder;
    }

    /**
     * Builder pattern for EmailAttachmentSettings.
     * Allows flexible construction with method chaining.
     *
     * @since 2025-11-13
     */
    public static class Builder {
        private String fdid;
        private String demographicNo;
        private String[] attachedEForms;
        private String[] attachedDocuments;
        private String[] attachedLabs;
        private String[] attachedHRMDocuments;
        private String[] attachedForms;
        private boolean attachEFormItSelf;
        private boolean openAfterEmail;
        private boolean isEmailEncrypted;
        private boolean isEmailAttachmentEncrypted;
        private boolean isEmailAutoSend;
        private boolean deleteEFormAfterEmail;
        private String emailPDFPassword;
        private String emailPDFPasswordClue;

        /**
         * Sets all attachment IDs and metadata.
         *
         * @param fdid eForm data ID
         * @param demographicNo demographic number
         * @param eForms array of attached eForm IDs
         * @param docs array of attached document IDs
         * @param labs array of attached lab IDs
         * @param hrmDocs array of attached HRM document IDs
         * @param forms array of attached form IDs
         * @return this builder for method chaining
         */
        public Builder withIds(String fdid, String demographicNo,
                               String[] eForms, String[] docs,
                               String[] labs, String[] hrmDocs,
                               String[] forms) {
            this.fdid = fdid;
            this.demographicNo = demographicNo;
            this.attachedEForms = eForms;
            this.attachedDocuments = docs;
            this.attachedLabs = labs;
            this.attachedHRMDocuments = hrmDocs;
            this.attachedForms = forms;
            return this;
        }

        /**
         * Builds the immutable EmailAttachmentSettings instance.
         *
         * @return configured EmailAttachmentSettings
         */
        public EmailAttachmentSettings build() {
            return new EmailAttachmentSettings(this);
        }
    }
}
