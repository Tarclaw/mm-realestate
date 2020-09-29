package com.realestate.service.implementation;

import com.realestate.dto.ApartmentDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.buildings.Apartment;
import com.realestate.model.people.Client;
import com.realestate.repositories.ApartmentRepository;
import com.realestate.repositories.ClientRepository;
import com.realestate.service.ApartmentService;
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
public class ApartmentServiceImpl implements ApartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentServiceImpl.class);

    private final ApartmentRepository apartmentRepository;
    private final ClientRepository clientRepository;
    private final ApartmentMapper mapper;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository,
                                ClientRepository clientRepository,
                                ApartmentMapper mapper) {
        this.apartmentRepository = apartmentRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        LOGGER.info("New instance of ApartmentServiceImpl created");
    }

    @Override
    public Apartment getById(final Long id) {
        LOGGER.trace("Enter and execute 'ApartmentServiceImpl.getById(final Long id)' method");
        return apartmentRepository.findApartmentsByIdWithClients(id)
                .orElseThrow(
                        () -> {
                            LOGGER.warn("We don't have apartment with id= " + id);
                            return new NotFoundException("Please chose different Apartment");
                        }
                );
    }

    @Override
    public List<Apartment> getApartments() {
        LOGGER.trace("Enter in 'ApartmentServiceImpl.getApartments()' method");

        List<Apartment> apartments = new ArrayList<>();
        apartmentRepository.findAll().iterator().forEachRemaining(apartments :: add);
        LOGGER.debug("Find all Apartments from DB and add them to HashSet");

        LOGGER.trace("'ApartmentServiceImpl.getApartments()' executed successfully.");
        return apartments;
    }

    @Override
    public List<ApartmentDTO> getApartmentsDTO() {
        return getApartments()
                .stream()
                .map(mapper::apartmentToApartmentDTO)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public ApartmentDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter and execute 'ApartmentServiceImpl.findCommandById(final Long id)' method");
        return mapper.apartmentToApartmentDTO(getById(id));
    }

    @Override
    @Transactional
    public ApartmentDTO saveDTO(final ApartmentDTO apartmentDTO) {
        LOGGER.trace("Enter in 'ApartmentServiceImpl.saveDetached(final FacilityCommand command)' method");

        Client client = clientRepository.findById(apartmentDTO.getClientId()).orElseThrow(() -> {
            LOGGER.warn("We don't have Client with id= " + apartmentDTO.getClientId());
            return new NotFoundException("We don't have this Client. Please choose another one.");
        });

        Apartment detachedApartment = mapper.apartmentDTOtoApartment(apartmentDTO);
        detachedApartment.getAddress().setFacility(detachedApartment);
        detachedApartment.setClient(client);
        Apartment savedApartment = apartmentRepository.save(detachedApartment);

        LOGGER.debug("We save Apartment with id= " + savedApartment.getId());
        LOGGER.trace("'ApartmentServiceImpl.saveDetached(final FacilityCommand command)' executed successfully.");
        return mapper.apartmentToApartmentDTO(savedApartment);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter and execute 'ApartmentServiceImpl.deleteById(final Long id)' method");

        apartmentRepository.deleteById(id);

        LOGGER.debug("Delete Apartment with id= " + id);
        LOGGER.trace("'ApartmentServiceImpl.deleteById(final Long id)' executed successfully.");
    }

    @Override
    @Transactional
    public void saveImage(Long id, MultipartFile multipartFile) {
        LOGGER.trace("Enter in 'ApartmentServiceImpl.saveImage(Long id, MultipartFile multipartFile)' method");

        try {
            Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> {
                LOGGER.warn("We don't have Apartment with id= " + id);
                return new NotFoundException("We don't have this Apartment. Please choose another one.");
            });
            apartment.setImage(multipartFile.getBytes());
            apartmentRepository.save(apartment);
            LOGGER.debug("We set Image and save Apartment with id= " + id);
        } catch (IOException e) {
            LOGGER.error("Attention can't execute multipartFile.getBytes() method", e);
            throw new ImageCorruptedException(e.getMessage());
        }

        LOGGER.trace("'ApartmentServiceImpl.saveImage(Long id, MultipartFile multipartFile)' executed successfully.");
    }

    @Override
    public ApartmentDTO patchApartment(ApartmentDTO apartmentDTO, Long id) {
        return apartmentRepository.findById(id).map(apartment -> {
            if (apartmentDTO.getApartmentNumber() != null) {
                apartment.setApartmentNumber(apartmentDTO.getApartmentNumber());
            }

            if (apartmentDTO.getFloor() != null) {
                apartment.setFloor(apartmentDTO.getFloor());
            }

            if (apartmentDTO.getMonthRent() != null) {
                apartment.setMonthRent(apartmentDTO.getMonthRent());
            }

            if (apartmentDTO.getPrice() != null) {
                apartment.setPrice(apartmentDTO.getPrice());
            }

            if (apartmentDTO.getNumberOfRooms() != null) {
                apartment.setNumberOfRooms(apartmentDTO.getNumberOfRooms());
            }

            if (apartmentDTO.getTotalArea() != null) {
                apartment.setTotalArea(apartmentDTO.getTotalArea());
            }

            if (apartmentDTO.getDescription() != null) {
                apartment.setDescription(apartmentDTO.getDescription());

            }

            if (apartmentDTO.getPublishedDateTime() != null) {
                apartment.setPublishedDateTime(apartmentDTO.getPublishedDateTime());
            }

            if (apartmentDTO.getClosedDateTime() != null) {
                apartment.setClosedDateTime(apartmentDTO.getClosedDateTime());
            }

            if (apartmentDTO.getStatus() != null) {
                apartment.setStatus(apartmentDTO.getStatus());
            }

            if (apartmentDTO.getImage() != null) {
                apartment.setImage(apartmentDTO.getImage());
            }
            return mapper.apartmentToApartmentDTO(apartmentRepository.save(apartment));
        }).orElseThrow(RuntimeException::new);
    }
}
