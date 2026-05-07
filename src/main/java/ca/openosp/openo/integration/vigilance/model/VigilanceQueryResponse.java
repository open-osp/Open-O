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

package ca.openosp.openo.integration.vigilance.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for a Vigilance query analysis.
 * Contains the result string returned by the API.
 */
public class VigilanceQueryResponse implements VigilanceResponse {
    @JsonProperty("result")
    private String result;

    @JsonProperty("alerts")
    private java.util.List<Alert> alerts;

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public java.util.List<Alert> getAlerts() { return alerts; }
    public void setAlerts(java.util.List<Alert> alerts) { this.alerts = alerts; }

    public static class Alert {
        private String type;
        private Basis basis;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public Basis getBasis() { return basis; }
        public void setBasis(Basis basis) { this.basis = basis; }
    }

    public static class Basis {
        private java.util.List<RxProblem> rxProblems;

        public java.util.List<RxProblem> getRxProblems() { return rxProblems; }
        public void setRxProblems(java.util.List<RxProblem> rxProblems) { this.rxProblems = rxProblems; }
    }

    public static class RxProblem {
        private String code;
        private String name;
        private String atc;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAtc() { return atc; }
        public void setAtc(String atc) { this.atc = atc; }
    }
}
