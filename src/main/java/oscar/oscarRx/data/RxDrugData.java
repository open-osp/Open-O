/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarRx.data;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.MiscUtils;

import oscar.oscarRx.data.model.DrugMonograph;
import oscar.oscarRx.data.model.DrugSearch;
import oscar.oscarRx.data.model.Interaction;
import oscar.oscarRx.util.RxDrugRef;

public class RxDrugData {

	/**
	 * Suggest Alias description here.
	 * @param alias
	 * @param aliasComment
	 * @param id
	 * @param name
	 * @param provider
	 * @throws Exception
	 */
	public void suggestAlias(String alias,String aliasComment,String id,String name,String provider) throws Exception{
		RxDrugRef d = new RxDrugRef();
		d.suggestAlias(alias,aliasComment,id,name,provider);
	}


	/**
	 * Get drug list by partial search string.
	 * All drugs, All classes, All activity
	 * @param searchStr
	 * @return
	 */
	public DrugSearch listDrug(String searchStr){
		DrugSearch drugSearch = new DrugSearch();
		RxDrugRef drugRef = new RxDrugRef();
		Vector vec= new Vector();
		//Vector vec = drugRef.list_drugs(searchStr,hashtable);
		try{
			vec  = drugRef.list_drug_element(searchStr);
		}catch(Exception connEx){
			drugSearch.failed = true;
			drugSearch.errorMessage = connEx.getMessage();
		}
		if (!drugSearch.failed){
			drugSearch.processResult(vec);
		}
		return drugSearch;
	}

	/**
	 * Get drug list by partial search string Version 2.
	 * All drugs, All classes, All activity
	 * @param searchStr
	 * @return
	 */
	public DrugSearch listDrug2(String searchStr){
		DrugSearch drugSearch = new DrugSearch();
		RxDrugRef drugRef = new RxDrugRef();
		Vector vec= new Vector();
		//Vector vec = drugRef.list_drugs(searchStr,hashtable);
		try{
			vec  = drugRef.list_drug_element2(searchStr);
		}catch(Exception connEx){
			drugSearch.failed = true;
			drugSearch.errorMessage = connEx.getMessage();
		}
		if (!drugSearch.failed){
			drugSearch.processResult(vec);
		}
		return drugSearch;
	}

	/**
	 * Get all drugs by search string and specific route.
	 * @param searchStr
	 * @param searchRoute
	 * @return
	 */
	public DrugSearch listDrugByRoute(String searchStr, String searchRoute){
		DrugSearch drugSearch = new DrugSearch();
		RxDrugRef drugRef = new RxDrugRef();
		Vector vec= new Vector();
		//Vector vec = drugRef.list_drugs(searchStr,hashtable);
		try{
			vec  = drugRef.list_drug_element_route(searchStr, searchRoute);
		}catch(Exception connEx){
			drugSearch.failed = true;
			drugSearch.errorMessage = connEx.getMessage();
		}
		if (!drugSearch.failed){
			drugSearch.processResult(vec);
		}
		return drugSearch;
	}

	/**
	 * Get all drugs that contain specific Element (partial)
	 * @param searchStr
	 * @return
	 */
	public DrugSearch listDrugFromElement(String searchStr){
		DrugSearch drugSearch = new DrugSearch();
		RxDrugRef drugRef = new RxDrugRef();
		Vector vec = new Vector();
		try{
			vec  = drugRef.list_brands_from_element(searchStr);
		}catch(Exception connEx){
			drugSearch.failed = true;
			drugSearch.errorMessage = connEx.getMessage();
		}
		if (!drugSearch.failed){
			drugSearch.processResult(vec);
		}
		return drugSearch;
	}

	/**
	 * Get drug with DrugRef ID key
	 * @param pKey
	 * @return
	 * @throws Exception
	 */
	public DrugMonograph getDrug(String pKey) throws Exception{
		RxDrugRef d = new RxDrugRef();
		return new DrugMonograph(d.getDrug(pKey,new Boolean(true)));
	}


	/**
	 * Get drug with DrugRef ID key. Version 2
	 * @param pKey
	 * @return
	 * @throws Exception
	 */
	public DrugMonograph getDrug2(String pKey) throws Exception{
		RxDrugRef d = new RxDrugRef();
		return new DrugMonograph(d.getDrug2(pKey,new Boolean(true)));
	}
	
	/**
	 * Get the drug package data by a Regional DIN number.
	 * @param DIN
	 * @return
	 * @throws Exception
	 */
	public DrugMonograph getDrugByDIN(String DIN) throws Exception {
		RxDrugRef drugRef = new RxDrugRef();
		Hashtable<String, Object> returnVal = drugRef.getDrugByDIN(DIN, Boolean.TRUE);
		if(returnVal == null) {
			return null;
		}
		return new DrugMonograph(returnVal);
	}


	/**
	 * Get the drug form (packaging) with DrugRef ID.
	 * @param pKey
	 * @return
	 * @throws Exception
	 */
	public String getDrugForm(String pKey) throws Exception {
		RxDrugRef d = new RxDrugRef();
		Hashtable h = d.getDrugForm(pKey);
		return (String) h.get("pharmaceutical_cd_form");
	}

	/**
	 * Get drug Generic Name with DrugRef ID.
	 * Be careful. A Generic Drug will not have a accurate 
	 * Generic Name.
	 * @param pKey
	 * @return
	 * @throws Exception
	 */
	public String getGenericName(String pKey) throws Exception{
		RxDrugRef d = new RxDrugRef();
		Hashtable h = d.getGenericName(pKey);
		return (String) h.getOrDefault("name", "");
	}


	/**
	 * Get drug Generic Name with DrugRef ID.
	 * Overload method.
	 * @param pKey
	 * @return
	 * @throws Exception
	 */
	public String getGenericName(int pKey) throws Exception{
		return getGenericName(pKey+"");
	}

	/**
	 * Get the drug form (packaging) with Drug Code??.
	 * @param drugCode
	 * @return
	 */
	@Deprecated
	public ArrayList getFormFromDrugCode(String drugCode){
		ArrayList lst = new ArrayList();
		Vector v = new Vector();
		RxDrugRef d = new RxDrugRef();
		v = d.getFormFromDrugCode(drugCode);
		for (int i = 0 ; i < v.size();i++){
			Hashtable h = (Hashtable) v.get(i);
			lst.add(h.get("form"));
		}
		return lst;
	}

	/**
	 * Get list of drug components from official drug code.
	 * @param drugCode
	 * @return ArrayList
	 */
	@Deprecated
	public ArrayList getComponentsFromDrugCode(String drugCode){
		ArrayList lst = new ArrayList();
		Vector v = new Vector();
		RxDrugRef d = new RxDrugRef();
		v = d.listComponents(drugCode);
		for (int i = 0 ; i < v.size();i++){
			Hashtable h = (Hashtable) v.get(i);
			lst.add(h.get("name"));
		}
		return lst;
	}

	/**
	 * Get ??
	 * @return ArrayList
	 */
	@Deprecated
	public ArrayList getDistinctForms(){
		ArrayList lst = new ArrayList();
		Vector v = new Vector();
		RxDrugRef d = new RxDrugRef();
		v = d.getDistinctForms();
		for (int i = 0 ; i < v.size();i++){
			Hashtable h = (Hashtable) v.get(i);
			lst.add(h.get("form"));
		}
		return lst;
	}

	/**
	 * Get a list of available Routes from official drug code.
	 * @param drugCode
	 * @return ArrayList
	 */
	@Deprecated
	public ArrayList getRouteFromDrugCode(String drugCode){
		ArrayList lst = new ArrayList();
		Vector v = new Vector();
		RxDrugRef d = new RxDrugRef();
		v = d.getRouteFromDrugCode(drugCode);
		for (int i = 0 ; i < v.size();i++){
			Hashtable h = (Hashtable) v.get(i);
			lst.add( h.get("route"));
		}
		return lst;
	}

	/**
	 * Get a list of drug strengths from official drug code.
	 * @param drugCode
	 * @return Hashtable 
	 */
	@Deprecated
	public Hashtable getStrengths(String drugCode){
		Hashtable retHash = new Hashtable();
		String dosage ="";
		String dosageDef ="";
		ArrayList lst = new ArrayList();
		Vector v = new Vector();
		RxDrugRef d = new RxDrugRef();
		v = d.getStrengths(drugCode);
		for (int i = 0 ; i < v.size();i++){
			Hashtable h = (Hashtable) v.get(i);
			dosage    = dosage + ((String) h.get("strength")) +" "+((String) h.get("strength_unit"));
			dosageDef = dosageDef + ((String) h.get("ingredient"));
			if (i < (v.size() -1 ) ){
				dosage = dosage+"/";
				dosageDef = dosageDef+"/";
			}
		}

		//select ingredient, strength, strength_unit
		retHash.put("dosage",dosage);
		retHash.put("dosageDef",dosageDef);

		return retHash;

	}


	/** 
	 * Get a list of drug Strengths with the Official Drug code.
	 * @param drugCode
	 * @return Hashtable
	 */
	@Deprecated
	public Hashtable getStrengthsLists(String drugCode){
		Hashtable retHash = new Hashtable();

		ArrayList lst = new ArrayList();
		Vector v = new Vector();
		RxDrugRef d = new RxDrugRef();
		v = d.getStrengths(drugCode);
		ArrayList dosage = new ArrayList();
		ArrayList dosageDef = new ArrayList();
		for (int i = 0 ; i < v.size();i++){
			Hashtable h = (Hashtable) v.get(i);
			dosage.add(((String) h.get("strength")) +" "+((String) h.get("strength_unit")));
			dosageDef.add((h.get("ingredient")));

		}

		//select ingredient, strength, strength_unit
		retHash.put("dosage",dosage);
		retHash.put("dosageDef",dosageDef);

		return retHash;

	}
	
	/**
	 * Get Allergy Warnings by drug ATC code and a list of the patients 
	 * Allergy codes.
	 * @param atcCode
	 * @param allerg
	 * @return Allergy[] 
	 * @throws Exception
	 */
	public Allergy[] getAllergyWarnings(String atcCode,Allergy[] allerg) throws Exception{
		return getAllergyWarnings(atcCode, allerg,null);
	}
	
	public Allergy[] getAllergyWarnings(String atcCode,Allergy[] allerg,List<Allergy> missing) throws Exception {
		Vector vec = new Vector();
		for (int i = 0; i < allerg.length; i++) {
			Hashtable h = new Hashtable();
			h.put("id", "" + i);
			h.put("description", allerg[i].getDescription());
			h.put("type", "" + allerg[i].getTypeCode());
			if (allerg[i].getRegionalIdentifier() != null) {
				h.put("din", allerg[i].getRegionalIdentifier());
			}
			if (allerg[i].getAtc() != null) {
				h.put("atc", allerg[i].getAtc());
			} else if (allerg[i].getTypeCode() == 8) {
				h.put("atc", allerg[i].getDrugrefId());
			}
			vec.add(h);
		}
		RxDrugRef d = new RxDrugRef();
		Vector res = d.getAlergyWarnings(atcCode, vec);

		Allergy[] actualAllergies = {};
		ArrayList li = new ArrayList();
		if(res != null ){
			Hashtable hashObject  = (Hashtable) res.get(0);
			if (hashObject != null){
				Vector alli = (Vector) hashObject.get("warnings");
				if(alli != null) {
					for (int k = 0; k < alli.size(); k++){
						String str = (String) alli.get(k);
						int id = Integer.parseInt(str);
						li.add(allerg[id]);
						MiscUtils.getLogger().debug(str);
					}
				}
				
				Vector allmissing = (Vector) hashObject.get("missing");
				if(allmissing != null) {
					for (int k = 0; k < allmissing.size(); k++){
						String str = (String) allmissing.get(k);
						int id = Integer.parseInt(str);
						if(missing != null) {
							missing.add(allerg[id]);
						}

					}
				}
			}
		}
		actualAllergies  =  (Allergy[]) li.toArray(actualAllergies);

		return actualAllergies;
	}


	/**
	 * Get a list of Interactions from a list of ATC Codes.
	 * @param atcCodes
	 * @return
	 * @throws Exception
	 */
	public Interaction[] getInteractions(Vector atcCodes) throws Exception{
		Interaction[] arr = {};
		ArrayList lst = new ArrayList();
		Vector v = new Vector();
		RxDrugRef d = new RxDrugRef();
		for(int i = 0; i < atcCodes.size(); i++){
			String ss = (String) atcCodes.get(i);
			MiscUtils.getLogger().debug(ss);
		}

		v = d.getInteractions(atcCodes);
		for (int i = 0 ; i < v.size();i++){
			Hashtable h = (Hashtable) v.get(i);
			Interaction inact = new Interaction();
			inact.affectedatc = (String) h.get("affectedatc");
			inact.affecteddrug = (String) h.get("affecteddrug");
			inact.affectingatc = (String) h.get("affectingatc");
			inact.affectingdrug = (String) h.get("affectingdrug");
			inact.effect = (String) h.get("effect");
			inact.evidence = (String) h.get("evidence");
			inact.significance = (String) h.get("significance");
			inact.comment = (String) h.get("comment");
			lst.add(inact);
			MiscUtils.getLogger().debug("affectingDrug"+inact.affectingdrug);
		}
		MiscUtils.getLogger().debug(lst.size());
		arr = (Interaction[])lst.toArray(arr);
		MiscUtils.getLogger().debug(arr.length);
		return arr;
	}


}
