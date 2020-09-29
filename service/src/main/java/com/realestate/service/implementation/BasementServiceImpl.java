package com.realestate.service.implementation;

import com.realestate.dto.BasementDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.BasementMapper;
import com.realestate.model.buildings.Basement;
import com.realestate.model.people.Client;
import com.realestate.repositories.BasementRepository;
import com.realestate.repositories.ClientRepository;
import com.realestate.service.BasementService;
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
public class BasementServiceImpl implements BasementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasementServiceImpl.class);

    private final BasementRepository basementRepository;
    private final ClientRepository clientRepository;
    private final BasementMapper mapper;

    public BasementServiceImpl(BasementRepository basementRepository,
                               ClientRepository clientRepository,
                               BasementMapper mapper) {
        this.basementRepository = basementRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        LOGGER.info("New instance of BasementServiceImpl created");
    }

    @Override
    public Basement getById(final Long id) {
        LOGGER.trace("Enter and execute 'BasementServiceImpl.getById(final Long id)' method");
        return basementRepository.findBasementByIdWithClients(id)
                .orElseThrow(
                        () -> {
                            LOGGER.warn("We don't have basement with id= " + id);
                            return new NotFoundException("Please chose different Basement.");
                        }
                );
    }

    @Override
    public List<Basement> getBasements() {
        LOGGER.trace("Enter in 'BasementServiceImpl.getBasements()' method");

        List<Basement> basements = new ArrayList<>();
        basementRepository.findAll().iterator().forEachRemaining(basements :: add);
        LOGGER.debug("Find all Basements from DB and add them to HashSet");

        LOGGER.trace("'BasementServiceImpl.getBasements()' executed successfully.");
        return basements;
    }

    @Override
    public List<BasementDTO> getBasementsDTO() {
        return getBasements()
                .stream()
                .map(mapper::basementToBasementDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BasementDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter and execute 'BasementServiceImpl.findCommandById(final Long id)' method");
        return mapper.basementToBasementDTO(getById(id));
    }

    @Override
    @Transactional
    public BasementDTO saveDTO(final BasementDTO basementDTO) {
        LOGGER.trace("Enter in 'BasementServiceImpl.saveDetached(final FacilityCommand command)' method");

        Client client = clientRepository.findById(basementDTO.getClientId()).orElseThrow(() -> {
            LOGGER.warn("We don't have Client with id= " + basementDTO.getClientId());
            return new NotFoundException("We don't have this Client. Please choose another one.");
        });

        Basement detachedBasement = mapper.basementDTOtoBasement(basementDTO);
        detachedBasement.getAddress().setFacility(detachedBasement);
        detachedBasement.setClient(client);
        Basement savedBasement = basementRepository.save(detachedBasement);

        LOGGER.debug("We save Basement with id= " + savedBasement.getId());
        LOGGER.trace("'BasementServiceImpl.saveDetached(final FacilityCommand command)' executed successfully.");
        return mapper.basementToBasementDTO(savedBasement);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter and execute 'BasementServiceImpl.deleteById(final Long id)' method");

        basementRepository.deleteById(id);
        LOGGER.debug("Delete Basement with id= " + id);

        LOGGER.trace("'BasementServiceImpl.deleteById(final Long id)' executed successfully.");
    }

    @Override
    @Transactional
    public void saveImage(Long id, MultipartFile file) {
        LOGGER.trace("Enter in 'BasementServiceImpl.saveImage(Long id, MultipartFile file)' method");

        try {
            Basement basement = basementRepository.findById(id).orElseThrow(() -> {
                LOGGER.warn("We don't have Basement with id= " + id);
                return new NotFoundException("We don't have this Basement. Please choose another one.");
            });
            basement.setImage(file.getBytes());
            basementRepository.save(basement);
            LOGGER.debug("We set Image and save Basement with id= " + id);
        } catch (IOException e) {
            LOGGER.error("Attention can't execute multipartFile.getBytes() method", e);
            throw new ImageCorruptedException(e.getMessage());
        }

        LOGGER.trace("'BasementServiceImpl.saveImage(Long id, MultipartFile file)' executed successfully.");
    }

    @Override
    public BasementDTO patchBasement(Long id, BasementDTO basementDTO) {
        return basementRepository.findById(id).map(basement -> {

            if (basementDTO.getMonthRent() != null) {
                basement.setMonthRent(basementDTO.getMonthRent());
            }

            if (basementDTO.getPrice() != null) {
                basement.setPrice(basementDTO.getPrice());
            }

            if (basementDTO.getNumberOfRooms() != null) {
                basement.setNumberOfRooms(basementDTO.getNumberOfRooms());
            }

            if (basementDTO.getTotalArea() != null) {
                basement.setTotalArea(basementDTO.getTotalArea());
            }

            if (basementDTO.getDescription() != null) {
                basement.setDescription(basementDTO.getDescription());

            }

            if (basementDTO.getPublishedDateTime() != null) {
                basement.setPublishedDateTime(basementDTO.getPublishedDateTime());
            }

            if (basementDTO.getClosedDateTime() != null) {
                basement.setClosedDateTime(basementDTO.getClosedDateTime());
            }

            if (basementDTO.getStatus() != null) {
                basement.setStatus(basementDTO.getStatus());
            }

            if (basementDTO.getImage() != null) {
                basement.setImage(basementDTO.getImage());
            }

            basement.setItCommercial(basementDTO.isItCommercial());
            return mapper.basementToBasementDTO(basementRepository.save(basement));
        }).orElseThrow(RuntimeException::new);
    }
}
