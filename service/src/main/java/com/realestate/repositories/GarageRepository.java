package com.realestate.repositories;

import com.realestate.model.buildings.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface GarageRepository extends JpaRepository<Garage, Long> {

    Optional<Garage> findGaragesByHasEquipment(Boolean hasEquipment);

    @Query("select g from Garage g join fetch g.client c where g.id = ?1")
    Optional<Garage> findGaragesByIdWithClients(Long id);

}
