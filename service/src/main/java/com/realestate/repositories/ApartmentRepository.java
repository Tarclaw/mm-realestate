package com.realestate.repositories;

import com.realestate.model.buildings.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    Optional<Apartment> findApartmentsByTotalArea(Integer totalArea);

    @Query("select a from Apartment a join fetch a.client c where a.id = ?1")
    Optional<Apartment> findApartmentsByIdWithClients(Long id);

}
