//CHECKSTYLE:OFF

package ca.openosp.openo.commn.dao;

import java.util.List;

import ca.openosp.openo.commn.model.ProfessionalSpecialist;

public interface ProfessionalSpecialistDao extends AbstractDao<ProfessionalSpecialist> {

    List<ProfessionalSpecialist> findAll();

    List<ProfessionalSpecialist> findByEDataUrlNotNull();

    List<ProfessionalSpecialist> findByFullName(String lastName, String firstName);

    List<ProfessionalSpecialist> findByLastName(String lastName);

    List<ProfessionalSpecialist> findBySpecialty(String specialty);

    List<ProfessionalSpecialist> findByReferralNo(String referralNo);

    ProfessionalSpecialist getByReferralNo(String referralNo);

    boolean hasRemoteCapableProfessionalSpecialists();

    List<ProfessionalSpecialist> search(String keyword);

    List<ProfessionalSpecialist> findByFullNameAndSpecialtyAndAddress(String lastName, String firstName, String specialty, String address, Boolean showHidden);

    List<ProfessionalSpecialist> findByService(String serviceName);

    List<ProfessionalSpecialist> findByServiceId(Integer serviceId);

    List<ProfessionalSpecialist> findByEDataOscarKey(String eDataOscarKey);

    List<ProfessionalSpecialist> findByEDataOscarKeyNotNull();
}
