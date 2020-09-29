package com.realestate.service.implementation;

import com.realestate.dto.GarageDTO;
import com.realestate.dto.GarageListDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.GarageMapper;
import com.realestate.model.buildings.Garage;
import com.realestate.model.people.Client;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.GarageRepository;
import com.realestate.service.GarageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GarageServiceImpl implements GarageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarageServiceImpl.class);

    private final GarageRepository garageRepository;
    private final ClientRepository clientRepository;
    private final GarageMapper mapper;

    public GarageServiceImpl(GarageRepository garageRepository,
                             ClientRepository clientRepository, GarageMapper mapper) {
        this.garageRepository = garageRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        LOGGER.info("New instance of GarageServiceImpl created");
    }

    @Override
    public Garage getById(final Long id) {
        LOGGER.trace("Enter and execute 'GarageServiceImpl.getById(final Long id)' method");
        return garageRepository.findGaragesByIdWithClients(id)
                .orElseThrow(
                        () -> {
                            LOGGER.warn("We don't have garage with id= " + id);
                            return new NotFoundException("Please chose different Garage.");
                        }
                );
    }

    @Override
    public List<Garage> getGarages() {
        LOGGER.trace("Enter in 'GarageServiceImpl.getGarages()' method");

        List<Garage> garages = new ArrayList<>();
        garageRepository.findAll().iterator().forEachRemaining(garages :: add);
        LOGGER.debug("Find all Garages from DB and add them to HashSet");

        LOGGER.trace("'GarageServiceImpl.getGarages()' executed successfully.");
        return garages;
    }

    @Override
    @Transactional
    public GarageDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter and execute 'GarageServiceImpl.findCommandById(final Long id)' method");
        return mapper.garageToGarageDTO(getById(id));
    }

    @Override
    @Transactional
    public GarageDTO saveDTO(final GarageDTO garageDTO) {
        LOGGER.trace("Enter in 'GarageServiceImpl.saveDetached(final FacilityCommand command)' method");

        Client client = clientRepository.findById(garageDTO.getClientId()).orElseThrow(() -> {
            LOGGER.warn("We don't have Client with id= " + garageDTO.getClientId());
            return new NotFoundException("We don't have this Client. Please choose another one.");
        });

        Garage detachedGarage = mapper.garageDTOtoGarage(garageDTO);
        detachedGarage.getAddress().setFacility(detachedGarage);
        detachedGarage.setClient(client);
        Garage savedGarage = garageRepository.save(detachedGarage);

        LOGGER.debug("We save Garage with id= " + savedGarage.getId());
        LOGGER.trace("'GarageServiceImpl.saveDetached(final FacilityCommand command)' executed successfully.");
        return mapper.garageToGarageDTO(savedGarage);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter and execute 'GarageServiceImpl.deleteById(final Long id)' method");

        garageRepository.deleteById(id);
        LOGGER.debug("Delete Garage with id= " + id);

        LOGGER.trace("'GarageServiceImpl.deleteById(final Long id)' executed successfully.");
    }

    @Override
    @Transactional
    public void saveImage(Long id, MultipartFile multipartFile) {
        LOGGER.trace("Enter in 'GarageServiceImpl.saveImage(Long id, MultipartFile file)' method");

        try {
            Garage garage = garageRepository.findById(id).orElseThrow(() -> {
                LOGGER.warn("We don't have Garage with id= " + id);
                return new NotFoundException("We don't have this Garage. Please choose another one.");
            });
            garage.setImage(multipartFile.getBytes());
            garageRepository.save(garage);
            LOGGER.debug("We set Image and save Garage with id= " + id);
        } catch (IOException e) {
            LOGGER.error("Attention can't execute multipartFile.getBytes() method", e);
            throw new ImageCorruptedException(e.getMessage());
        }

        LOGGER.trace("'GarageServiceImpl.saveImage(Long id, MultipartFile file)' executed successfully.");
    }

    @Override
    public List<GarageDTO> getGaragesDTO() {
        return getGarages()
                .stream()
                .map(mapper::garageToGarageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GarageDTO patchGarage(Long id, GarageDTO garageDTO) {
        return garageRepository.findById(id).map(garage -> {
            if (garageDTO.getMonthRent() != null) {
                garage.setMonthRent(garageDTO.getMonthRent());
            }

            if (garageDTO.getPrice() != null) {
                garage.setPrice(garageDTO.getPrice());
            }

            if (garageDTO.getNumberOfRooms() != null) {
                garage.setNumberOfRooms(garageDTO.getNumberOfRooms());
            }

            if (garageDTO.getTotalArea() != null) {
                garage.setTotalArea(garageDTO.getTotalArea());
            }

            if (garageDTO.getDescription() != null) {
                garage.setDescription(garageDTO.getDescription());

            }

            if (garageDTO.getPublishedDateTime() != null) {
                garage.setPublishedDateTime(garageDTO.getPublishedDateTime());
            }

            if (garageDTO.getClosedDateTime() != null) {
                garage.setClosedDateTime(garageDTO.getClosedDateTime());
            }

            if (garageDTO.getStatus() != null) {
                garage.setStatus(garageDTO.getStatus());
            }

            if (garageDTO.getImage() != null) {
                garage.setImage(garageDTO.getImage());
            }

            garage.setHasPit(garageDTO.isHasPit());
            garage.setHasEquipment(garageDTO.isHasEquipment());

            return mapper.garageToGarageDTO(garageRepository.save(garage));
        }).orElseThrow(RuntimeException::new);
    }
}
