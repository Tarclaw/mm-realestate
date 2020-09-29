package com.realestate.repositories;

import com.realestate.model.buildings.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findAddressesByStreet(String street);

}
