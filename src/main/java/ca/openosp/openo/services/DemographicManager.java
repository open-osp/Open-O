//CHECKSTYLE:OFF
package ca.openosp.openo.services;

import ca.openosp.openo.commn.model.Demographic;

import java.util.List;

public interface DemographicManager {
    Demographic getDemographic(String demographic_no);

    List getDemographics();

    List getProgramIdByDemoNo(String demoNo);

    List getDemoProgram(Integer demoNo);
}
