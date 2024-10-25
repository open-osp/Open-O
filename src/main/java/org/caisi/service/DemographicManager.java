package org.caisi.service;

import org.oscarehr.common.model.Demographic;
import java.util.List;

public interface DemographicManager {
    Demographic getDemographic(String demographic_no);
    List getDemographics();
    List getProgramIdByDemoNo(String demoNo);
    List getDemoProgram(Integer demoNo);
}
