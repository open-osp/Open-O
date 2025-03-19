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

import org.oscarehr.common.dao.FavoriteDao;
import org.oscarehr.util.SpringUtils;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;

public class Favorite {

    int favoriteId;
    String providerNo;
    String favoriteName;
    String BN;
    int GCN_SEQNO;
    String customName;
    float takeMin;
    float takeMax;
    String frequencyCode;
    String duration;
    String durationUnit;
    String quantity;
    int repeat;
    boolean nosubs;
    boolean prn;
    boolean customInstr;
    String special;
    String GN;
    String atcCode;
    String regionalIdentifier;
    String unit;
    String unitName;
    String method;
    String route;
    String drugForm;
    String dosage;
    Boolean dispenseInternal;

    public Favorite(int favoriteId, String providerNo, String favoriteName, String BN, int GCN_SEQNO, String customName, float takeMin, float takeMax, String frequencyCode, String duration, String durationUnit, String quantity, int repeat, int nosubs, int prn, String special, String GN, String atc, String regionalIdentifier, String unit, String unitName, String method, String route, String drugForm, boolean customInstr, String dosage) {
        this.favoriteId = favoriteId;
        this.providerNo = providerNo;
        this.favoriteName = favoriteName;
        this.BN = BN;
        this.GCN_SEQNO = GCN_SEQNO;
        this.customName = customName;
        this.takeMin = takeMin;
        this.takeMax = takeMax;
        this.frequencyCode = frequencyCode;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.quantity = quantity;
        this.repeat = repeat;
        this.nosubs = RxUtil.IntToBool(nosubs);
        this.prn = RxUtil.IntToBool(prn);
        this.special = special;
        this.GN = GN;
        this.atcCode = atc;
        this.regionalIdentifier = regionalIdentifier;
        this.unit = unit;
        this.unitName = unitName;
        this.method = method;
        this.route = route;
        this.drugForm = drugForm;
        this.customInstr = customInstr;
        this.dosage = dosage;
    }

    public Favorite(int favoriteId, String providerNo, String favoriteName, String BN, int GCN_SEQNO, String customName, float takeMin, float takeMax, String frequencyCode, String duration, String durationUnit, String quantity, int repeat, boolean nosubs, boolean prn, String special, String GN, String atc, String regionalIdentifier, String unit, String unitName, String method, String route, String drugForm, boolean customInstr, String dosage) {
        this.favoriteId = favoriteId;
        this.providerNo = providerNo;
        this.favoriteName = favoriteName;
        this.BN = BN;
        this.GCN_SEQNO = GCN_SEQNO;
        this.customName = customName;
        this.takeMin = takeMin;
        this.takeMax = takeMax;
        this.frequencyCode = frequencyCode;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.quantity = quantity;
        this.repeat = repeat;
        this.nosubs = nosubs;
        this.prn = prn;
        this.special = special;
        this.GN = GN;
        this.atcCode = atc;
        this.regionalIdentifier = regionalIdentifier;
        this.unit = unit;
        this.unitName = unitName;
        this.method = method;
        this.route = route;
        this.drugForm = drugForm;
        this.customInstr = customInstr;
        this.dosage = dosage;
    }

    public String getGN() {
        return this.GN;
    }

    public void setGN(String RHS) {
        this.GN = RHS;
    }

    public int getFavoriteId() {
        return this.favoriteId;
    }

    public String getProviderNo() {
        return this.providerNo;
    }

    public String getFavoriteName() {
        return this.favoriteName;
    }

    public void setFavoriteName(String RHS) {
        this.favoriteName = RHS;
    }

    public String getBN() {
        return this.BN;
    }

    public void setBN(String RHS) {
        this.BN = RHS;
    }

    public int getGCN_SEQNO() {
        return this.GCN_SEQNO;
    }

    public void setGCN_SEQNO(int RHS) {
        this.GCN_SEQNO = RHS;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String RHS) {
        this.customName = RHS;
    }

    public float getTakeMin() {
        return this.takeMin;
    }

    public void setTakeMin(float RHS) {
        this.takeMin = RHS;
    }

    public String getTakeMinString() {
        return RxUtil.FloatToString(this.takeMin);
    }

    public float getTakeMax() {
        return this.takeMax;
    }

    public void setTakeMax(float RHS) {
        this.takeMax = RHS;
    }

    public String getTakeMaxString() {
        return RxUtil.FloatToString(this.takeMax);
    }

    public String getFrequencyCode() {
        return this.frequencyCode;
    }

    public void setFrequencyCode(String RHS) {
        this.frequencyCode = RHS;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String RHS) {
        this.duration = RHS;
    }

    public String getDurationUnit() {
        return this.durationUnit;
    }

    public void setDurationUnit(String RHS) {
        this.durationUnit = RHS;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String RHS) {
        this.quantity = RHS;
    }

    public int getRepeat() {
        return this.repeat;
    }

    public void setRepeat(int RHS) {
        this.repeat = RHS;
    }

    public boolean getNosubs() {
        return this.nosubs;
    }

    public void setNosubs(boolean RHS) {
        this.nosubs = RHS;
    }

    public int getNosubsInt() {
        if (this.getNosubs() == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean getPrn() {
        return this.prn;
    }

    public void setPrn(boolean RHS) {
        this.prn = RHS;
    }

    public int getPrnInt() {
        if (this.getPrn() == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getSpecial() {
        return this.special;
    }

    public void setSpecial(String RHS) {
        this.special = RHS;
    }

    public boolean getCustomInstr() {
        return this.customInstr;
    }

    public void setCustomInstr(boolean customInstr) {
        this.customInstr = customInstr;
    }

    public Boolean getDispenseInternal() {
        return dispenseInternal;
    }

    public void setDispenseInternal(Boolean dispenseInternal) {
        this.dispenseInternal = dispenseInternal;
    }

    public boolean Save() {
        boolean b = false;

        // clean up fields
        if (this.takeMin > this.takeMax) {
            this.takeMax = this.takeMin;
        }
        if (getSpecial() == null || getSpecial().length() < 4) {
            //if (getSpecial() == null || getSpecial().length() < 6) {
            RxPrescriptionData.logger.warn("drug special appears to be null or empty : " + getSpecial());
        }
        String parsedSpecial = RxUtil.replace(this.getSpecial(), "'", "");
        //if (parsedSpecial == null || parsedSpecial.length() < 6) {
        if (parsedSpecial == null || parsedSpecial.length() < 4) {
            RxPrescriptionData.logger.warn("drug special after parsing appears to be null or empty : " + parsedSpecial);
        }

        FavoriteDao dao = SpringUtils.getBean(FavoriteDao.class);
        org.oscarehr.common.model.Favorite favorite = dao.findByEverything(this.getProviderNo(), this.getFavoriteName(), this.getBN(), this.getGCN_SEQNO(), this.getCustomName(), this.getTakeMin(), this.getTakeMax(), this.getFrequencyCode(), this.getDuration(), this.getDurationUnit(), this.getQuantity(), this.getRepeat(), this.getNosubs(), this.getPrn(), parsedSpecial, this.getGN(), this.getUnitName(), this.getCustomInstr());

        if (this.getFavoriteId() == 0) {

            if (favorite != null) this.favoriteId = favorite.getId();

            b = true;

            if (this.getFavoriteId() == 0) {
                favorite = new org.oscarehr.common.model.Favorite();
                favorite = syncFavorite(favorite);

                dao.persist(favorite);
                this.favoriteId = favorite.getId();

                b = true;
            }

        } else {
            if (favorite == null) {
                //we never found it..try by id
                favorite = dao.find(this.getFavoriteId());
            }
            favorite = syncFavorite(favorite);
            dao.merge(favorite);

            b = true;
        }

        return b;
    }

    private org.oscarehr.common.model.Favorite syncFavorite(org.oscarehr.common.model.Favorite f) {
        f.setProviderNo(this.getProviderNo());
        f.setName(this.getFavoriteName());
        f.setBn(this.getBN());
        f.setGcnSeqno(this.getGCN_SEQNO());
        f.setCustomName(this.getCustomName());
        f.setTakeMin(this.getTakeMin());
        f.setTakeMax(this.getTakeMax());
        f.setFrequencyCode(this.getFrequencyCode());
        f.setDuration(this.getDuration());
        f.setDurationUnit(this.getDurationUnit());
        f.setQuantity(this.getQuantity());
        f.setRepeat(this.getRepeat());
        f.setNosubs(this.getNosubsInt() != 0);
        f.setPrn(this.getPrnInt() != 0);
        f.setSpecial(this.getSpecial());
        f.setGn(this.getGN());
        f.setAtc(this.getAtcCode());
        f.setRegionalIdentifier(this.getRegionalIdentifier());
        f.setUnit(this.getUnit());
        f.setUnitName(this.getUnitName());
        f.setMethod(this.getMethod());
        f.setRoute(this.getRoute());
        f.setDrugForm(this.getDrugForm());
        f.setCustomInstructions(this.getCustomInstr());
        f.setDosage(this.getDosage());
        return f;
    }

    /**
     * Getter for property atcCode.
     *
     * @return Value of property atcCode.
     */
    public String getAtcCode() {
        return atcCode;
    }

    /**
     * Setter for property atcCode.
     *
     * @param atcCode New value of property atcCode.
     */
    public void setAtcCode(String atcCode) {
        this.atcCode = atcCode;
    }

    /**
     * Getter for property regionalIdentifier.
     *
     * @return Value of property regionalIdentifier.
     */
    public String getRegionalIdentifier() {
        return regionalIdentifier;
    }

    /**
     * Setter for property regionalIdentifier.
     *
     * @param regionalIdentifier New value of property regionalIdentifier.
     */
    public void setRegionalIdentifier(String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }

    /**
     * Getter for property unit.
     *
     * @return Value of property unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Setter for property unit.
     *
     * @param unit New value of property unit.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Getter for property unitName.
     *
     * @return Value of property unitName.
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Setter for property unitName.
     *
     * @param unitName New value of property unitName.
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * Getter for property method.
     *
     * @return Value of property method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Setter for property method.
     *
     * @param method New value of property method.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Getter for property route.
     *
     * @return Value of property route.
     */
    public String getRoute() {
        return route;
    }

    /**
     * Setter for property route.
     *
     * @param route New value of property route.
     */
    public void setRoute(String route) {
        this.route = route;
    }

    public String getDrugForm() {
        return drugForm;
    }

    public void setDrugForm(String drugForm) {
        this.drugForm = drugForm;
    }

    /**
     * Getter for property dosage.
     *
     * @return Value of property dosage.
     */
    public String getDosage() {
        return dosage;
    }

    /**
     * Setter for property dosage.
     *
     * @param dosage New value of property dosage.
     */
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

}
