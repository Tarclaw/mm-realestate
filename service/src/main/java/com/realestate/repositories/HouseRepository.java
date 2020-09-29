package com.realestate.repositories;

import com.realestate.model.buildings.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findHousesByHasGarden(Boolean hasGarden);

    @Query("select h from House h join fetch h.client c where h.id = ?1")
    Optional<House> findHousesByIdWithClients(Long id);

}
