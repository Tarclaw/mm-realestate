package com.realestate.service;

import com.realestate.model.buildings.Facility;

import java.util.List;
import java.util.Map;

public interface MappingService {

    Map<Long, String> buildMapping(List<Facility> facilities);

}
