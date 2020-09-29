package com.realestate.service.implementation;

import com.realestate.dto.HouseDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.HouseMapper;
import com.realestate.model.buildings.House;
import com.realestate.model.people.Client;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.HouseRepository;
import com.realestate.service.HouseService;
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
public class HouseServiceImpl implements HouseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HouseServiceImpl.class);

    private final HouseRepository houseRepository;
    private final ClientRepository clientRepository;
    private final HouseMapper mapper;

    public HouseServiceImpl(HouseRepository houseRepository,
                            ClientRepository clientRepository,
                            HouseMapper mapper) {
        this.houseRepository = houseRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        LOGGER.info("New instance of HouseServiceImpl created");
    }

    @Override
    public House getById(final Long id) {
        LOGGER.trace("Enter and execute 'HouseServiceImpl.getById(final Long id)' method");
        return houseRepository.findHousesByIdWithClients(id)
                .orElseThrow(
                        () -> {
                            LOGGER.warn("We don't have house with id= " + id);
                            return new NotFoundException("Please chose different House.");
                        }
                );
    }

    @Override
    public List<House> getHouses() {
        LOGGER.trace("Enter in 'HouseServiceImpl.getHouses()' method");

        List<House> houses = new ArrayList<>();
        houseRepository.findAll().iterator().forEachRemaining(houses :: add);
        LOGGER.debug("Find all Houses from DB and add them to HashSet");

        LOGGER.trace("'HouseServiceImpl.getHouses()' executed successfully.");
        return houses;
    }

    @Override
    @Transactional
    public HouseDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter and execute 'HouseServiceImpl.findCommandById(final Long id)' method");
        return mapper.houseToHouseDTO(getById(id));
    }

    @Override
    @Transactional
    public HouseDTO saveDTO(final HouseDTO houseDTO) {
        LOGGER.trace("Enter in 'HouseServiceImpl.saveDetached(final FacilityCommand command)' method");

        Client client = clientRepository.findById(houseDTO.getClientId()).orElseThrow(() -> {
            LOGGER.warn("We don't have Client with id= " + houseDTO.getClientId());
            return new NotFoundException("We don't have this Client. Please choose another one.");
        });

        House detachedHouse = mapper.houseDTOtoHouse(houseDTO);
        detachedHouse.getAddress().setFacility(detachedHouse);
        detachedHouse.setClient(client);
        House savedHouse = houseRepository.save(detachedHouse);

        LOGGER.debug("We save House with id= " + savedHouse.getId());
        LOGGER.trace("'HouseServiceImpl.saveDetached(final FacilityCommand command)' executed successfully.");
        return mapper.houseToHouseDTO(savedHouse);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter and execute 'HouseServiceImpl.deleteById(final Long id)' method");

        houseRepository.deleteById(id);
        LOGGER.debug("Delete House with id= " + id);

        LOGGER.trace("'HouseServiceImpl.deleteById(final Long id)' executed successfully.");
    }

    @Override
    public void saveImage(Long id, MultipartFile multipartFile) {
        LOGGER.trace("Enter in 'HouseServiceImpl.saveImage(Long id, MultipartFile file)' method");

        try {
            House house = houseRepository.findById(id).orElseThrow(() -> {
                LOGGER.warn("We don't have House with id= " + id);
                return new NotFoundException("We don't have this House. Please choose another one.");
            });
            house.setImage(multipartFile.getBytes());
            houseRepository.save(house);
            LOGGER.debug("We set Image and save House with id= " + id);
        } catch (IOException e) {
            LOGGER.error("Attention can't execute multipartFile.getBytes() method", e);
            throw new ImageCorruptedException(e.getMessage());
        }

        LOGGER.trace("'HouseServiceImpl.saveImage(Long id, MultipartFile file)' executed successfully.");
    }

    @Override
    public List<HouseDTO> getHousesDTO() {
        return getHouses()
                .stream()
                .map(mapper::houseToHouseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HouseDTO patchHouse(Long id, HouseDTO houseDTO) {
        return houseRepository.findById(id).map(house -> {
            if (houseDTO.getNumberOfStoreys() != null) {
                house.setNumberOfStoreys(houseDTO.getNumberOfStoreys());
            }

            if (houseDTO.getMonthRent() != null) {
                house.setMonthRent(houseDTO.getMonthRent());
            }

            if (houseDTO.getPrice() != null) {
                house.setPrice(houseDTO.getPrice());
            }

            if (houseDTO.getNumberOfRooms() != null) {
                house.setNumberOfRooms(houseDTO.getNumberOfRooms());
            }

            if (houseDTO.getTotalArea() != null) {
                house.setTotalArea(houseDTO.getTotalArea());
            }

            if (houseDTO.getDescription() != null) {
                house.setDescription(houseDTO.getDescription());

            }

            if (houseDTO.getPublishedDateTime() != null) {
                house.setPublishedDateTime(houseDTO.getPublishedDateTime());
            }

            if (houseDTO.getClosedDateTime() != null) {
                house.setClosedDateTime(houseDTO.getClosedDateTime());
            }

            if (houseDTO.getStatus() != null) {
                house.setStatus(houseDTO.getStatus());
            }

            if (houseDTO.getImage() != null) {
                house.setImage(houseDTO.getImage());
            }

            house.setHasBackyard(houseDTO.isHasBackyard());
            house.setHasGarden(houseDTO.isHasGarden());

            return mapper.houseToHouseDTO(houseRepository.save(house));
        }).orElseThrow(RuntimeException::new);
    }
}
