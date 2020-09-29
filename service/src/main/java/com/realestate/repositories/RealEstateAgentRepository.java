package com.realestate.repositories;

import com.realestate.model.people.RealEstateAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RealEstateAgentRepository extends JpaRepository<RealEstateAgent, Long> {

    Optional<RealEstateAgent> findRealEstateAgentsByFirstNameAndLastName(String firstName, String lastName);

    @Query("select r from RealEstateAgent r join fetch r.clients where r.id = ?1")
    Optional<RealEstateAgent> findRealEstateAgentsByIdWithEntities(Long id);

}
