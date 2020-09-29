package com.realestate.service;

import com.realestate.dto.FacilityDTO;
import com.realestate.model.buildings.Facility;

import java.util.List;

public interface FacilityService {

    List<Facility> getFacilities();

    List<Facility> getFacilitiesByIds(Long id);

    Facility saveRawFacility(FacilityDTO facilityDTO);

    void deleteById(Long id);

}
