package com.realestate.repositories;

import com.realestate.model.buildings.Basement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BasementRepository extends JpaRepository<Basement, Long> {

    Optional<Basement> findBasementsByItCommercial(Boolean itCommercial);

    @Query("select b from Basement b join fetch b.client c where b.id = ?1")
    Optional<Basement> findBasementByIdWithClients(Long id);

}
