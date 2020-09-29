package com.realestate.service.implementation;

import com.realestate.dto.FacilityDTO;
import com.realestate.mapper.FacilityMapper;
import com.realestate.model.buildings.*;
import com.realestate.repositories.*;
import com.realestate.service.FacilityService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityServiceImpl.class);

    private final FacilityRepository repository;
    private final ApartmentRepository apartmentRepository;
    private final BasementRepository basementRepository;
    private final GarageRepository garageRepository;
    private final HouseRepository houseRepository;
    private final StorageRepository storageRepository;

    public FacilityServiceImpl(FacilityRepository repository, ApartmentRepository apartmentRepository,
                               BasementRepository basementRepository, GarageRepository garageRepository,
                               HouseRepository houseRepository, StorageRepository storageRepository) {
        this.repository = repository;
        this.apartmentRepository = apartmentRepository;
        this.basementRepository = basementRepository;
        this.garageRepository = garageRepository;
        this.houseRepository = houseRepository;
        this.storageRepository = storageRepository;
        LOGGER.info("New instance of FacilityServiceImpl created");
    }

    @Override
    public List<Facility> getFacilities() {
        LOGGER.trace("Enter in 'FacilityServiceImpl.getFacilities()' method");

        List<Facility> facilities = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(facilities :: add);
        LOGGER.debug("Find all Facilities from DB and add them to ArrayList");

        LOGGER.trace("'FacilityServiceImpl.getFacilities()' executed successfully.");
        return facilities;
    }

    @Override
    public List<Facility> getFacilitiesByIds(final Long clientId) {
        LOGGER.trace("Enter and execute 'FacilityServiceImpl.getFacilitiesByIds(final Long clientId)' method");
        return repository.findFacilitiesByClientId(clientId);
    }

    @Override
    public Facility saveRawFacility(FacilityDTO facilityDTO) {
        LOGGER.trace("Enter in 'FacilityServiceImpl.saveRawFacility(FacilityCommand command)' method");

        Facility facility = Mappers.getMapper(FacilityMapper.class).facilityDTOtoFacility(facilityDTO);

        if (facility instanceof Apartment) {
            Apartment apartment = (Apartment) facility;
            LOGGER.trace("'FacilityServiceImpl.getFacilities()' executed successfully. Saved as Apartment.");
            return apartmentRepository.save(apartment);
        }

        if (facility instanceof Basement) {
            Basement basement = (Basement) facility;
            LOGGER.trace("'FacilityServiceImpl.getFacilities()' executed successfully. Saved as Basement.");
            return basementRepository.save(basement);
        }

        if (facility instanceof Garage) {
            Garage garage = (Garage) facility;
            LOGGER.trace("'FacilityServiceImpl.getFacilities()' executed successfully. Saved as Garage.");
            return garageRepository.save(garage);
        }

        if (facility instanceof House) {
            House house = (House) facility;
            LOGGER.trace("'FacilityServiceImpl.getFacilities()' executed successfully. Saved as House.");
            return houseRepository.save(house);
        }

        if (facility instanceof Storage) {
            Storage storage = (Storage) facility;
            LOGGER.trace("'FacilityServiceImpl.getFacilities()' executed successfully. Saved as Storage.");
            return storageRepository.save(storage);
        }

        LOGGER.trace("'FacilityServiceImpl.getFacilities()' executed successfully. Saved as Facility.");
        return repository.save(facility);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter in 'FacilityServiceImpl.deleteById(final Long id)' method");

        repository.deleteById(id);
        LOGGER.debug("Delete Facility with id= " + id);

        LOGGER.trace("'FacilityServiceImpl.deleteById(final Long id)' executed successfully.");
    }
}
