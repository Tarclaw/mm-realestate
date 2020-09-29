package com.realestate.repositories;

import com.realestate.model.people.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientsByLastName(String lastName);

    @Query("select c from Client c join fetch c.facilities f join fetch c.realEstateAgents a where c.id = ?1")
    Optional<Client> findClientByIdWithFacilitiesAndAgents(Long id);

}
