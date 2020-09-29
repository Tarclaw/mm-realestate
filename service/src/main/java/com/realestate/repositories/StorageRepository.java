package com.realestate.repositories;

import com.realestate.model.buildings.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {

    Optional<Storage> findStoragesByCommercialCapacityGreaterThan(Integer commercialCapacity);

    @Query("select s from Storage s join fetch s.client where s.id = ?1")
    Optional<Storage> findStoragesByIdWithClients(Long id);

}
