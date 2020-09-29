package com.realestate.repositories;

import com.realestate.model.buildings.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select f.id, f.description from Facility f")
    List<Facility> findAllWithIdAndDescription();

    List<Facility> findFacilitiesByClientId(Long clientId);

}
