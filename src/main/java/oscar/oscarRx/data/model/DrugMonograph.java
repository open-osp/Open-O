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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.util.MiscUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Full Drug Monograph.
 */
public class DrugMonograph {

    public String name;        // : string. International nonproprietary name (INPN) of this drug (=generic)
    public String atc;         //: string. ATC code
    public String regionalIdentifier;
    //     generics : struct. Lists all generic components (usually just one). Key (string) is the generic name, value (integer) is the repective primary key
    public boolean essential;  //: True if this drug is on the WHO essential drug list
    public String product;     //: string. If this drug is not a generic, the product brand name is listed under this key, else this key is not available
    public String action;      //: string. Description of mode of action.
    public Vector indications; //: array of Indications. Each struct has indication as key, and a struct as value containing the following keys:
    public Vector components = new Vector();
    public Vector contraindications; //: array of contraindications. Each struct has contraindication as key, and a struct as value containing the following keys:
    public String[] practice_points; //: array of strings
    public String paediatric_use; //:  string. Describing special considerations in paediatric use
    public String[] common_adverse_effects; //: array of strings
    public String[] rare_adverse_effects;   //: array of strings
    public Vector dosage; //array of Dosage
    public String drugForm;//drug form
    public Vector route = new Vector();//route for taking drug

    public ArrayList<DrugComponent> drugComponentList = new ArrayList<DrugComponent>();
    public PregnancyUse pregnancyUse;
    public LactationUse lactationUse;
    public RenalImpairment renalImpairment;
    public HepaticImpairment hepaticImpairment;

    public String drugCode;

    public DrugMonograph() {
        //default
    }

    public DrugMonograph(Hashtable hash) {
        MiscUtils.getLogger().debug(hash);
        name = (String) hash.get("name");
        atc = (String) hash.get("atc");
        product = (String) hash.get("product");
        regionalIdentifier = StringUtils.isEmpty((String) hash.get("regional_identifier")) ? null : (String) hash.get("regional_identifier");
        drugForm = (String) hash.get("drugForm");
        if (hash.get("drugCode") != null) {
            drugCode = (String) hash.get("drugCode");
        }

        Vector drugRoute = (Vector) hash.get("drugRoute");
        if (drugRoute != null) {
            for (int i = 0; i < drugRoute.size(); i++) {
                String r = (String) drugRoute.get(i);

                route.add(r);
            }
        }

        Vector comps = (Vector) hash.get("components");
        for (int i = 0; i < comps.size(); i++) {

            Hashtable h = (Hashtable) comps.get(i);
            DrugComponent comp = new DrugComponent(h);
            components.add(comp);
            drugComponentList.add(comp);
        }
        //{name=WARFARIN SODIUM, regional_identifier=02007959, product=COUMADIN TAB 4MG, atc=808774}

    }

    class Contraindications {
        int code;          // integer. Drugref condition code primary key.
        int severity;      // : integer (1-3, 3 being absolute contraindication)
        String comment;    // : string
    }

    class Indications {
        int code;         // : integer. Drugref condition code primary key
        boolean firstline;// : boolean. True if for this indication this drug is considered a first line treatment.
        String comment;   // : string
    }

    class PregnancyUse {    //: struct with following keys:
        char code;    // : character. ADEC category
        String comment; // : string
    }

    class LactationUse {  //: struct with following keys:
        int code;          // : integer. 1=compatible, 2=restricted, 3=dangerous
        String comment;    // : string
    }

    class RenalImpairment { //: struct with following keys:
        int code;            // : integer. 1=compatible, 2=restricted, 3=dangerous
        String comment;      // : string
    }

    class HepaticImpairment { //: struct with following keys:
        int code;              //: integer. 1=compatible, 2=restricted, 3=dangerous
        String comment;        // : string
    }

    class Dosage {
        String text;//: string. If this key is available, only free text dosage information is available, as described in this string.
        int indication; // : integer. Drugref condition code primary key. 0 is wild card, indicating general dosage recommendation.
        String units;   //: string. SI unit for this dosage
        int calculation_base_units;//: integer. 0=not applicable, 1=age in months, 2=age in years, 3=kg body weight, 4=cm2 body surface
        double calculation_base; // : real. Meaning depends on calculation_base_units
        double starting_range;  // : real. Usual mimimal recommended dosage
        double upper_range; // : real. Usual maximal recommended dosage
        int frequency_units; //: integer. 0=not applicable, 1=seconds, 2=minutes, 3=hours, 4=days, 5=weeks, 6=months, 7=years
        int frequency;       //: integer. How often this drug should be administered
        int duration_units; //: integer. Same as frequency_units, additional value 8='times'
        int duration_minimum; // ; integer. How long a usual course of this drug should be given. -1 is permanent, -2=p.r.n.
        int duration_maximum; // : integer. -1 is permanent, -2=p.r.n.
        boolean constrained;  //: boolean. If true, no automazied dosage suggestion must be generated, prescriber must read comment. (e.g. dosage per body surface etc.)
        String comment;
    }


    public class DrugComponent {

        public String name;
        public String strength;
        public String unit;

        public DrugComponent() {
            // default
        }

        public DrugComponent(Hashtable h) {
            name = (String) h.get("name");
            strength = ((Double) h.get("strength")).toString();
            unit = (String) h.get("unit");
        }

        public String getName() {
            return name;
        }

        public String getStrength() {
            return strength;
        }

        public String getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }

    public String getName() {
        return name;
    }

    public String getAtc() {
        return atc;
    }

    public String getRegionalIdentifier() {
        return regionalIdentifier;
    }

    public boolean isEssential() {
        return essential;
    }

    public String getProduct() {
        return product;
    }

    public String getAction() {
        return action;
    }

    public Vector getIndications() {
        return indications;
    }

    public ArrayList<DrugComponent> getDrugComponentList() {
        return drugComponentList;
    }

    public Vector getComponents() {
        return components;
    }

    public Vector getContraindications() {
        return contraindications;
    }

    public String[] getPractice_points() {
        return practice_points;
    }

    public String getPaediatric_use() {
        return paediatric_use;
    }

    public String[] getCommon_adverse_effects() {
        return common_adverse_effects;
    }

    public String[] getRare_adverse_effects() {
        return rare_adverse_effects;
    }

    public Vector getDosage() {
        return dosage;
    }

    public String getDrugForm() {
        return drugForm;
    }

    public Vector getRoute() {
        return route;
    }

    public PregnancyUse getPregnancyUse() {
        return pregnancyUse;
    }

    public LactationUse getLactationUse() {
        return lactationUse;
    }

    public RenalImpairment getRenalImpairment() {
        return renalImpairment;
    }

    public HepaticImpairment getHepaticImpairment() {
        return hepaticImpairment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
