//CHECKSTYLE:OFF

package ca.openosp.openo.commn.dao;

import java.util.List;

import ca.openosp.openo.commn.model.ConsultationServices;

public interface ConsultationServiceDao extends AbstractDao<ConsultationServices> {
    public String REFERRING_DOCTOR = "Referring Doctor";
    public String ACTIVE = "1";
    public String INACTIVE = "02";
    public boolean ACTIVE_ONLY = true;
    public boolean WITH_INACTIVE = false;

    public List<ConsultationServices> findAll();

    public List<ConsultationServices> findActive();

    public List<ConsultationServices> findActiveNames();

    public ConsultationServices findByDescription(String description);

    public ConsultationServices findReferringDoctorService(boolean activeOnly);

    public ConsultationServices findByExternalId(String externalId);

    public List<ConsultationServices> findByExternalIdNotNull();
}