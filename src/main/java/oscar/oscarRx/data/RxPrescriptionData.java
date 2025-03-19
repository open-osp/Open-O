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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.FavoriteDao;
import org.oscarehr.common.dao.IndivoDocsDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.IndivoDocs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.data.model.Favorite;
import oscar.oscarRx.data.model.Patient;
import oscar.oscarRx.data.model.Prescription;
import oscar.oscarRx.data.model.Provider;
import oscar.oscarRx.util.RxUtil;
import oscar.util.ConversionUtils;

public class RxPrescriptionData {

	public static final Logger logger = MiscUtils.getLogger();

	public static String getFullOutLine(String special) {
		String ret = "";
		if (special != null) {
			if (special.length() > 0) {
				int i;
				String[] arr = special.split("\n");
				for (i = 0; i < arr.length; i++) {
					ret += arr[i].trim();
					if (i < arr.length - 1) {
						ret += "; ";
					}
				}
			}
		} else {
			logger.warn("Drugs special field was null, this means nothing will print.");
		}

		return ret;
	}

	public Prescription getPrescription(int drugId) {

		DrugDao drugDao = (DrugDao) SpringUtils.getBean(DrugDao.class);
		Drug drug = drugDao.find(drugId);

		Prescription prescription = new Prescription(drugId, drug.getProviderNo(), drug.getDemographicId());
		prescription.setRxCreatedDate(drug.getCreateDate());
		prescription.setRxDate(drug.getRxDate());
		prescription.setEndDate(drug.getEndDate());
		prescription.setWrittenDate(drug.getWrittenDate());
		prescription.setBrandName(drug.getBrandName());
		prescription.setGCN_SEQNO(drug.getGcnSeqNo());
		prescription.setCustomName(drug.getCustomName());
		prescription.setTakeMin(drug.getTakeMin());
		prescription.setTakeMax(drug.getTakeMax());
		prescription.setFrequencyCode(drug.getFreqCode());
		String dur = drug.getDuration();
		if (StringUtils.isBlank(dur) || dur.equalsIgnoreCase("null")) dur = "";
		prescription.setDuration(dur);
		prescription.setDurationUnit(drug.getDurUnit());
		prescription.setQuantity(drug.getQuantity());
		prescription.setRepeat(drug.getRepeat());
		prescription.setLastRefillDate(drug.getLastRefillDate());
		prescription.setNosubs(drug.isNoSubs());
		prescription.setPrn(drug.isPrn());
		prescription.setSpecial(drug.getSpecial());
		prescription.setGenericName(drug.getGenericName());
		prescription.setAtcCode(drug.getAtc());
		prescription.setScript_no(String.valueOf(drug.getScriptNo()));
		prescription.setRegionalIdentifier(drug.getRegionalIdentifier());
		prescription.setUnit(drug.getUnit());
		prescription.setUnitName(drug.getUnitName());
		prescription.setMethod(drug.getMethod());
		prescription.setRoute(drug.getRoute());
		prescription.setDrugForm(drug.getDrugForm());
		prescription.setCustomInstr(drug.isCustomInstructions());
		prescription.setDosage(drug.getDosage());
		prescription.setLongTerm(drug.getLongTerm());
		prescription.setShortTerm(drug.getShortTerm());
		prescription.setCustomNote(drug.isCustomNote());
		prescription.setPastMed(drug.getPastMed());
		prescription.setDispenseInternal(drug.getDispenseInternal());
		prescription.setStartDateUnknown(drug.getStartDateUnknown());
		prescription.setComment(drug.getComment());
		prescription.setPatientCompliance(drug.getPatientCompliance());
		prescription.setOutsideProviderName(drug.getOutsideProviderName());
		prescription.setOutsideProviderOhip(drug.getOutsideProviderOhip());
		prescription.setSpecialInstruction(drug.getSpecialInstruction());
		prescription.setPickupDate(drug.getPickUpDateTime());
		prescription.setPickupTime(drug.getPickUpDateTime());
		prescription.setProtocol(drug.getProtocol());
		prescription.setPriorRxProtocol(drug.getPriorRxProtocol());
		prescription.setETreatmentType(drug.getETreatmentType());
		prescription.setRxStatus(drug.getRxStatus());
		if (drug.getDispenseInterval() != null) prescription.setDispenseInterval(drug.getDispenseInterval());
		if (drug.getRefillDuration() != null) prescription.setRefillDuration(drug.getRefillDuration());
		if (drug.getRefillQuantity() != null) prescription.setRefillQuantity(drug.getRefillQuantity());

		if (prescription.getSpecial() == null || prescription.getSpecial().length() <= 6) {
			logger.warn("I strongly suspect something is wrong, either special is null or it appears to not contain anything useful. drugId=" + drugId + ", drug.special=" + prescription.getSpecial());
			logger.warn("data from db is : " + drug.getSpecial());
		}
		prescription.setDispenseInternal(drug.getDispenseInternal());
		prescription.setPharmacyId(drug.getPharmacyId());
		return prescription;
	}

	public Prescription newPrescription(String providerNo, int demographicNo) {
		// Create new prescription (only in memory)
		return new Prescription(0, providerNo, demographicNo);
	}

	public Prescription newPrescription(String providerNo, int demographicNo, Favorite favorite) {
		// Create new prescription from favorite (only in memory)
		Prescription prescription = new Prescription(0, providerNo, demographicNo);

		prescription.setRxDate(RxUtil.Today());
		prescription.setWrittenDate(RxUtil.Today());
		prescription.setEndDate(null);
		prescription.setBrandName(favorite.getBN());
		prescription.setGCN_SEQNO(favorite.getGCN_SEQNO());
		prescription.setCustomName(favorite.getCustomName());
		prescription.setTakeMin(favorite.getTakeMin());
		prescription.setTakeMax(favorite.getTakeMax());
		prescription.setFrequencyCode(favorite.getFrequencyCode());
		prescription.setDuration(favorite.getDuration());
		prescription.setDurationUnit(favorite.getDurationUnit());
		prescription.setQuantity(favorite.getQuantity());
		prescription.setRepeat(favorite.getRepeat());
		prescription.setNosubs(favorite.getNosubs());
		prescription.setPrn(favorite.getPrn());
		prescription.setSpecial(favorite.getSpecial());
		prescription.setGenericName(favorite.getGN());
		prescription.setAtcCode(favorite.getAtcCode());
		prescription.setRegionalIdentifier(favorite.getRegionalIdentifier());
		prescription.setUnit(favorite.getUnit());
		prescription.setUnitName(favorite.getUnitName());
		prescription.setMethod(favorite.getMethod());
		prescription.setRoute(favorite.getRoute());
		prescription.setDrugForm(favorite.getDrugForm());
		prescription.setCustomInstr(favorite.getCustomInstr());
		prescription.setDosage(favorite.getDosage());

		return prescription;
	}

	public Prescription newPrescription(String providerNo, int demographicNo, Prescription rePrescribe) {
		// Create new prescription
		Prescription prescription = new Prescription(0, providerNo, demographicNo);

		prescription.setRxDate(RxUtil.Today());
		prescription.setWrittenDate(RxUtil.Today());
		prescription.setEndDate(null);
		prescription.setBrandName(rePrescribe.getBrandName());
		prescription.setGCN_SEQNO(rePrescribe.getGCN_SEQNO());
		prescription.setCustomName(rePrescribe.getCustomName());
		prescription.setTakeMin(rePrescribe.getTakeMin());
		prescription.setTakeMax(rePrescribe.getTakeMax());
		prescription.setFrequencyCode(rePrescribe.getFrequencyCode());
		prescription.setDuration(rePrescribe.getDuration());
		prescription.setDurationUnit(rePrescribe.getDurationUnit());
		prescription.setQuantity(rePrescribe.getQuantity());
		prescription.setRepeat(rePrescribe.getRepeat());
		prescription.setLastRefillDate(rePrescribe.getLastRefillDate());
		prescription.setNosubs(rePrescribe.getNosubs());
		prescription.setPrn(rePrescribe.getPrn());
		prescription.setSpecial(rePrescribe.getSpecial());
		prescription.setGenericName(rePrescribe.getGenericName());
		prescription.setAtcCode(rePrescribe.getAtcCode());
		prescription.setScript_no(rePrescribe.getScript_no());
		prescription.setRegionalIdentifier(rePrescribe.getRegionalIdentifier());
		prescription.setUnit(rePrescribe.getUnit());
		prescription.setUnitName(rePrescribe.getUnitName());
		prescription.setMethod(rePrescribe.getMethod());
		prescription.setRoute(rePrescribe.getRoute());
		prescription.setDrugForm(rePrescribe.getDrugForm());
		prescription.setCustomInstr(rePrescribe.getCustomInstr());
		prescription.setDosage(rePrescribe.getDosage());
		prescription.setLongTerm(rePrescribe.getLongTerm());
		prescription.setShortTerm(rePrescribe.getShortTerm());
		prescription.setCustomNote(rePrescribe.isCustomNote());
		prescription.setPastMed(rePrescribe.getPastMed());
		prescription.setDispenseInternal(rePrescribe.isDispenseInternal());
		prescription.setPatientCompliance(rePrescribe.getPatientCompliance());
		prescription.setOutsideProviderName(rePrescribe.getOutsideProviderName());
		prescription.setOutsideProviderOhip(rePrescribe.getOutsideProviderOhip());
		prescription.setSpecialInstruction(rePrescribe.getSpecialInstruction());
		prescription.setPickupDate(rePrescribe.getPickupDate());
		prescription.setPickupTime(rePrescribe.getPickupTime());
		prescription.setETreatmentType(rePrescribe.getETreatmentType());
		prescription.setRxStatus(rePrescribe.getRxStatus());
		if (rePrescribe.getDispenseInterval() != null) prescription.setDispenseInterval(rePrescribe.getDispenseInterval());
		if (rePrescribe.getRefillDuration() != null) prescription.setRefillDuration(rePrescribe.getRefillDuration());
		if (rePrescribe.getRefillQuantity() != null) prescription.setRefillQuantity(rePrescribe.getRefillQuantity());
		prescription.setDrugReferenceId(rePrescribe.getDrugId());
		prescription.setDispenseInternal(rePrescribe.getDispenseInternal());
		prescription.setProtocol(rePrescribe.getProtocol());
		prescription.setPriorRxProtocol(rePrescribe.getPriorRxProtocol());
		return prescription;
	}

	public Prescription[] getPrescriptionsByPatient(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();

		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Drug drug : dao.findByDemographicIdOrderByPosition(demographicNo, false)) {
			Prescription p = toPrescription(drug, demographicNo);
			lst.add(p);
		}

		return lst.toArray(new Prescription[lst.size()]);
	}
	
	public Prescription[] getPrescriptionsByPatientForExport(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();

		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Drug drug : dao.findByDemographicIdOrderByPositionForExport(demographicNo, null)) {
			Prescription p = toPrescription(drug, demographicNo);
			lst.add(p);
		}

		return lst.toArray(new Prescription[lst.size()]);
	}

	public Prescription toPrescription(Drug drug, int demographicNo) {
		Prescription p = new Prescription(drug.getId(), drug.getProviderNo(), demographicNo);
		p.setRxCreatedDate(drug.getCreateDate());
		p.setRxDate(drug.getRxDate());
		p.setEndDate(drug.getEndDate());
		p.setWrittenDate(drug.getWrittenDate());
		p.setBrandName(drug.getBrandName());
		p.setGCN_SEQNO(drug.getGcnSeqNo());
		p.setCustomName(drug.getCustomName());
		p.setTakeMin(drug.getTakeMin());
		p.setTakeMax(drug.getTakeMax());
		p.setFrequencyCode(drug.getFreqCode());
		p.setDuration(drug.getDuration());
		p.setDurationUnit(drug.getDuration());
		p.setQuantity(drug.getQuantity());
		p.setRepeat(drug.getRepeat());
		p.setLastRefillDate(drug.getLastRefillDate());
		p.setNosubs(drug.isNoSubs());
		p.setPrn(drug.isPrn());
		p.setSpecial(drug.getSpecial());
		p.setSpecialInstruction(drug.getSpecialInstruction());
		p.setArchived(String.valueOf(drug.isArchived()));
		p.setGenericName(drug.getGenericName());
		p.setAtcCode(drug.getAtc());
		p.setScript_no(String.valueOf(drug.getScriptNo()));
		p.setRegionalIdentifier(drug.getRegionalIdentifier());
		p.setUnit(drug.getUnit());
		p.setUnitName(drug.getUnitName());
		p.setMethod(drug.getMethod());
		p.setRoute(drug.getRoute());
		p.setDrugForm(drug.getDrugForm());
		p.setCustomInstr(drug.isCustomInstructions());
		p.setDosage(drug.getDosage());
		p.setLongTerm(drug.getLongTerm());
		p.setShortTerm(drug.getShortTerm());
		p.setCustomNote(drug.isCustomNote());
		p.setPastMed(drug.getPastMed());
		p.setStartDateUnknown(drug.getStartDateUnknown());
		p.setComment(drug.getComment());	
		p.setPatientCompliance(drug.getPatientCompliance());
		p.setOutsideProviderName(drug.getOutsideProviderName());
		p.setOutsideProviderOhip(drug.getOutsideProviderOhip());
		p.setPickupDate(drug.getPickUpDateTime());
		p.setPickupTime(drug.getPickUpDateTime());
		p.setProtocol(drug.getProtocol());
		p.setPriorRxProtocol(drug.getPriorRxProtocol());
		p.setETreatmentType(drug.getETreatmentType());
		p.setRxStatus(drug.getRxStatus());
		if (drug.getDispenseInterval() != null) p.setDispenseInterval(drug.getDispenseInterval());
		if (drug.getRefillDuration() != null) p.setRefillDuration(drug.getRefillDuration());
		if (drug.getRefillQuantity() != null) p.setRefillQuantity(drug.getRefillQuantity());
		p.setHideCpp(drug.getHideFromCpp());
		p.setPharmacyId(drug.getPharmacyId());
		if(drug.isNonAuthoritative() != null) p.setNonAuthoritative(drug.isNonAuthoritative());
		p.setDiscontinued(drug.isDiscontinued());
		p.setArchivedDate(drug.getArchivedDate());
		return p;
	}

	public Prescription[] getPrescriptionScriptsByPatientATC(int demographicNo, String atc) {
		List<Prescription> lst = new ArrayList<Prescription>();

		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Drug drug : dao.findByDemographicIdAndAtc(demographicNo, atc))
			lst.add(toPrescription(drug, demographicNo));

		return lst.toArray(new Prescription[lst.size()]);
	}

	// do not return customed drugs
	public Prescription[] getPrescriptionScriptsByPatientRegionalIdentifier(int demographicNo, String regionalIdentifier) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		for (Drug drug : dao.findByDemographicIdAndRegion(demographicNo, regionalIdentifier)) {
			Prescription p = toPrescription(drug, demographicNo); 
			lst.add(p);
		}
		return lst.toArray(new Prescription[lst.size()]);
	}

	public Prescription getLatestPrescriptionScriptByPatientDrugId(int demographicNo, String drugId) {
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		List<Drug> drugs = dao.findByDemographicIdAndDrugId(demographicNo, Integer.parseInt(drugId));
		if (drugs.isEmpty()) return null;
		return toPrescription(drugs.get(0), demographicNo);
	}

	/*
	 * Limit returned prescriptions to those which have an entry in both drugs and prescription table
	 */
	public Prescription[] getPrescriptionScriptsByPatient(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Object[] pair : dao.findDrugsAndPrescriptions(demographicNo)) {
			Drug drug = (Drug) pair[0];
			org.oscarehr.common.model.Prescription rx = (org.oscarehr.common.model.Prescription) pair[1];
			MiscUtils.getLogger().debug("Looking at drug " + drug + " and rx " + rx);
			lst.add(toPrescription(demographicNo, drug, rx));
		}
		return lst.toArray(new Prescription[lst.size()]);
	}

	private Prescription toPrescription(int demographicNo, Drug drug, org.oscarehr.common.model.Prescription rx) {
		Prescription prescription = toPrescription(drug, demographicNo);
		if (!rx.isReprinted()) prescription.setNumPrints(1);
		else prescription.setNumPrints(rx.getReprintCount() + 1);

		prescription.setPrintDate(rx.getDatePrinted());
		prescription.setDatesReprinted(rx.getDatesReprinted());
		prescription.setDigitalSignatureId(rx.getDigitalSignatureId());
		return prescription;
	}

	public List<Prescription> getPrescriptionsByScriptNo(int script_no, int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);
		for (Object[] pair : dao.findDrugsAndPrescriptionsByScriptNumber(script_no)) {
			Drug drug = (Drug) pair[0];
			org.oscarehr.common.model.Prescription rx = (org.oscarehr.common.model.Prescription) pair[1];

			lst.add(toPrescription(demographicNo, drug, rx));
		}
		return lst;
	}

	public Prescription[] getPrescriptionsByPatientHideDeleted(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		for (Drug drug : dao.findByDemographicId(demographicNo)) {
			if ((drug.isCurrent() && !drug.isArchived() && !drug.isDeleted() && !drug.isDiscontinued()) || drug.isLongTerm()) {
				lst.add(toPrescription(drug, demographicNo));
			}
		}
		return lst.toArray(new Prescription[lst.size()]);
	}

	public Prescription[] getActivePrescriptionsByPatient(int demographicNo) {
		List<Prescription> lst = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		for (Drug drug : dao.findByDemographicId(demographicNo)) {
			Prescription p = toPrescription(drug, demographicNo);
			if (!p.isArchived() && !p.isDiscontinued() && p.isCurrent()) {
				lst.add(p);
			}
		}
		return lst.toArray(new Prescription[lst.size()]);
	}

	public Vector getCurrentATCCodesByPatient(int demographicNo) {
		List<String> result = new ArrayList<String>();

		Prescription[] p = getPrescriptionsByPatientHideDeleted(demographicNo);
		for (int i = 0; i < p.length; i++) {
			if (p[i].isCurrent()) {
				if (!result.contains(p[i].getAtcCode())) {
					if (p[i].isValidAtcCode()) result.add(p[i].getAtcCode());
				}
			}
		}
		return new Vector(result);
	}

	public List<String> getCurrentRegionalIdentifiersCodesByPatient(int demographicNo) {
		List<String> result = new ArrayList<String>();

		Prescription[] p = getPrescriptionsByPatientHideDeleted(demographicNo);
		for (int i = 0; i < p.length; i++) {
			if (p[i].isCurrent()) {
				if (!result.contains(p[i].getRegionalIdentifier())) {
					if (p[i].getRegionalIdentifier() != null && p[i].getRegionalIdentifier().trim().length() != 0) {
						result.add(p[i].getRegionalIdentifier());
					}
				}
			}
		}
		return result;
	}

	public Prescription[] getUniquePrescriptionsByPatient(int demographicNo) {
		List<Prescription> result = new ArrayList<Prescription>();
		DrugDao dao = SpringUtils.getBean(DrugDao.class);

		List<Drug> drugList = dao.findByDemographicId(demographicNo);

		Collections.sort(drugList, new Drug.ComparatorIdDesc());

		for (Drug drug : drugList) {

			if(drug.isDeleted())
				continue;
			
			boolean isCustomName = true;

			for (Prescription p : result) {
				if (p.getGCN_SEQNO() == drug.getGcnSeqNo()) {
					if (p.getGCN_SEQNO() != 0) // not custom - safe GCN
					isCustomName = false;
					else if (p.getCustomName() != null && drug.getCustomName() != null) // custom
					    isCustomName = !p.getCustomName().equals(drug.getCustomName());

				}
			}

			if (isCustomName) {
				logger.debug("ADDING PRESCRIPTION " + drug.getId());
				Prescription p = toPrescription(drug, demographicNo);

				IndivoDocsDao iDao = SpringUtils.getBean(IndivoDocsDao.class);
				IndivoDocs iDoc = iDao.findByOscarDocNo(drug.getId());
				if (iDoc != null) {
					p.setIndivoIdx(iDoc.getIndivoDocIdx());
					if (p.getIndivoIdx() != null && p.getIndivoIdx().length() > 0) p.setRegisterIndivo();
				}
				
				p.setPosition(drug.getPosition());
				result.add(p);
			}
		}
		return result.toArray(new Prescription[result.size()]);
	}

	public Favorite[] getFavorites(String providerNo) {
		FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);

		List<Favorite> result = new ArrayList<Favorite>();

		for (org.oscarehr.common.model.Favorite f : dao.findByProviderNo(providerNo))
			result.add(toFavorite(f));

		return result.toArray(new Favorite[result.size()]);
	}

	private Favorite toFavorite(org.oscarehr.common.model.Favorite f) {
		Favorite result = new Favorite(f.getId(), f.getProviderNo(), f.getName(), f.getBn(), (int) f.getGcnSeqno(), f.getCustomName(), f.getTakeMin(), f.getTakeMax(), f.getFrequencyCode(), f.getDuration(), f.getDurationUnit(), f.getQuantity(), f.getRepeat(), f.isNosubs(), f.isPrn(), f.getSpecial(), f.getGn(), f.getAtc(), f.getRegionalIdentifier(), f.getUnit(), f.getUnitName(), f.getMethod(), f.getRoute(), f.getDrugForm(), f.isCustomInstructions(), f.getDosage());
		return result;
	}

	public Favorite getFavorite(int favoriteId) {
		FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);
		org.oscarehr.common.model.Favorite result = dao.find(favoriteId);
		if (result == null) return null;
		return toFavorite(result);
	}

	public boolean deleteFavorite(int favoriteId) {
		FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);
		return dao.remove(favoriteId);
	}

	/**
	 * This function is used to save a set of prescribed drugs to as one prescription. This is for historical purposes
	 *
	 * @param bean This is the oscarRx session bean
	 * @return This returns the insert id of the script to be included the drugs table
	 */
	public String saveScript(LoggedInInfo loggedInInfo, oscar.oscarRx.pageUtil.RxSessionBean bean) {
		/*
		 * create table prescription ( script_no int(10) auto_increment primary key, provider_no varchar(6), demographic_no int(10), date_prescribed date, date_printed date, dates_reprinted text, textView text);
		 */
		String provider_no = bean.getProviderNo();
		int demographic_no = bean.getDemographicNo();

		Date today = oscar.oscarRx.util.RxUtil.Today();
		//String date_prescribed = oscar.oscarRx.util.RxUtil.DateToString(today, "yyyy/MM/dd");
		//String date_printed = date_prescribed;

		StringBuilder textView = new StringBuilder();

		// ///create full text view
		Patient patient = null;
		Provider provider = null;
		try {
			patient = RxPatientData.getPatient(loggedInInfo, demographic_no);
			provider = new oscar.oscarRx.data.RxProviderData().getProvider(provider_no);
		} catch (Exception e) {
			logger.error("unexpected error", e);
		}
		ProSignatureData sig = new ProSignatureData();
		boolean hasSig = sig.hasSignature(bean.getProviderNo());
		String doctorName = "";
		if (hasSig) {
			doctorName = sig.getSignature(bean.getProviderNo());
		} else {
			doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
		}

		textView.append(doctorName + "\n");
		textView.append(provider.getClinicName() + "\n");
		textView.append(provider.getClinicAddress() + "\n");
		textView.append(provider.getClinicCity() + "\n");
		textView.append(provider.getClinicPostal() + "\n");
		textView.append(provider.getClinicPhone() + "\n");
		textView.append(provider.getClinicFax() + "\n");
		textView.append(patient.getFirstName() + " " + patient.getSurname() + "\n");
		textView.append(patient.getAddress() + "\n");
		textView.append(patient.getCity() + " " + patient.getPostal() + "\n");
		textView.append(patient.getPhone() + "\n");
		textView.append(oscar.oscarRx.util.RxUtil.DateToString(today, "MMMM d, yyyy") + "\n");

		String txt;
		for (int i = 0; i < bean.getStashSize(); i++) {
			Prescription rx = bean.getStashItem(i);

			String fullOutLine = rx.getFullOutLine();
			if (fullOutLine == null || fullOutLine.length() < 6) {
				logger.warn("Drug full outline appears to be null or empty : " + fullOutLine);
			}

			txt = fullOutLine.replaceAll(";", "\n");
			textView.append("\n" + txt);
		}
		// textView.append();

		org.oscarehr.common.model.Prescription rx = new org.oscarehr.common.model.Prescription();
		rx.setProviderNo(provider_no);
		rx.setDemographicId(demographic_no);
		rx.setDatePrescribed(today);
		rx.setDatePrinted(today);
		rx.setTextView(textView.toString());

		PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
		dao.persist(rx);
		return rx.getId().toString();
	}

	public int setScriptComment(String scriptNo, String comment) {
		PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
		return dao.updatePrescriptionsByScriptNo(Integer.parseInt(scriptNo), comment);
	}

	public String getScriptComment(String scriptNo) {
		PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
		org.oscarehr.common.model.Prescription p = dao.find(ConversionUtils.fromIntString(scriptNo));
		if (p == null) return null;

		return p.getComments();
	}

	public static boolean addToFavorites(String providerNo, String favoriteName, Drug drug) {
		Favorite fav = new Favorite(0, providerNo, favoriteName, drug.getBrandName(), drug.getGcnSeqNo(), drug.getCustomName(), drug.getTakeMin(), drug.getTakeMax(), drug.getFreqCode(), drug.getDuration(), drug.getDurUnit(), drug.getQuantity(), drug.getRepeat(), drug.isNoSubs(), drug.isPrn(), drug.getSpecial(), drug.getGenericName(), drug.getAtc(), drug.getRegionalIdentifier(), drug.getUnit(), drug.getUnitName(), drug.getMethod(), drug.getRoute(), drug.getDrugForm(), drug.isCustomInstructions(),
		        drug.getDosage());
		fav.setDispenseInternal(drug.getDispenseInternal());
		return fav.Save();
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}

}
