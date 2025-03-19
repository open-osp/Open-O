/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.oscarRx.data.model;

public class Provider {
    private final String providerNo;
    private final String surname;
    private final String firstName;
    private final String clinicName;
    private final String clinicAddress;
    private final String clinicCity;
    private final String clinicPostal;
    private final String clinicPhone;
    private final String clinicFax;
    private final String practitionerNo;
    private final String practitionerNoType;
    private boolean fullAddress = true;
    private String clinicProvince;

    public Provider(String providerNo, String surname, String firstName,
                    String clinicName, String clinicAddress, String clinicCity,
                    String clinicPostal, String clinicPhone, String clinicFax, String practitionerNo, String practitionerNoType) {
        this.providerNo = providerNo;
        this.surname = surname;
        this.firstName = firstName;
        this.clinicName = clinicName;
        this.clinicAddress = clinicAddress;
        this.clinicCity = clinicCity;
        this.clinicPostal = clinicPostal;
        this.clinicPhone = clinicPhone;
        this.clinicFax = clinicFax;
        this.practitionerNo = practitionerNo;
        this.practitionerNoType = practitionerNoType;
    }

    public Provider(String providerNo, String surname, String firstName,
                    String clinicName, String clinicAddress, String clinicCity,
                    String clinicPostal, String clinicPhone, String clinicFax, String clinicProvince, String practitionerNo, String practitionerNoType) {
        this(providerNo, surname, firstName, clinicName, clinicAddress, clinicCity, clinicPostal, clinicPhone, clinicFax, practitionerNo, practitionerNoType);
        this.clinicProvince = clinicProvince;
    }


    public String getProviderNo() {
        return this.providerNo;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getClinicName() {
        return this.clinicName;
    }

    public String getClinicAddress() {
        return this.clinicAddress;
    }

    public String getClinicCity() {
        return this.clinicCity;
    }

    public String getClinicPostal() {
        return this.clinicPostal;
    }

    public String getClinicPhone() {
        return this.clinicPhone;
    }

    public String getClinicFax() {
        return this.clinicFax;
    }

    public String getClinicProvince() {
        return this.clinicProvince;
    }

    public String getPractitionerNo() {
        return this.practitionerNo;
    }

    public String getPractitionerNoType() {
        return this.practitionerNoType;
    }

    public String getFullAddress() {
        if (fullAddress)
            return (getClinicAddress() + "  " + getClinicCity() + "   " + getClinicProvince() + "  " + getClinicPostal()).trim();
        else
            return getClinicAddress().trim();
    }

    public void setFullAddress(boolean fullAddress) {
        this.fullAddress = fullAddress;
    }

}
