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

import ca.openosp.openo.caisi_integrator.ws.CachedProvider;
import ca.openosp.openo.commn.model.Provider;

public final class MsgProviderData {

    private ContactIdentifier id;
    private String firstName;
    private String lastName;
    private String prefix;
    private String providerType;
    private String location;
    private boolean member;

    public MsgProviderData() {
        // default
    }

    public MsgProviderData(CachedProvider cachedProvider) {
        getId().setContactId(cachedProvider.getFacilityIdStringCompositePk().getCaisiItemId());
        getId().setFacilityId(cachedProvider.getFacilityIdStringCompositePk().getIntegratorFacilityId());
        setFirstName(cachedProvider.getFirstName());
        setLastName(cachedProvider.getLastName());
        setLocation("Integrator");
        setProviderType(cachedProvider.getSpecialty());
    }

    public MsgProviderData(Provider provider) {
        getId().setContactId(provider.getProviderNo());
        getId().setContactUniqueId(provider.getPractitionerNo());
        getId().setFacilityId(0);
        setFirstName(provider.getFirstName());
        setLastName(provider.getLastName());
        setLocation(provider.getAddress());
        setProviderType(provider.getSpecialty());
    }

    public ContactIdentifier getId() {
        if (id == null) {
            id = new ContactIdentifier();
        }
        return id;
    }

    public void setId(ContactIdentifier id) {
        this.id = id;
    }

    public String getFirstName() {
        if (firstName == null) {
            return "";
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        if (lastName == null) {
            return "";
        }
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

}
