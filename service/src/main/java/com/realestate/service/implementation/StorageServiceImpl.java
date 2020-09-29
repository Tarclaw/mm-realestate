package com.realestate.service.implementation;

import com.realestate.dto.StorageDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.StorageMapper;
import com.realestate.model.buildings.Storage;
import com.realestate.model.people.Client;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.StorageRepository;
import com.realestate.service.StorageService;
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
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final StorageRepository storageRepository;
    private final ClientRepository clientRepository;
    private final StorageMapper mapper;

    public StorageServiceImpl(StorageRepository storageRepository,
                              ClientRepository clientRepository ,
                              StorageMapper mapper) {
        this.storageRepository = storageRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        LOGGER.info("New instance of StorageServiceImpl created");
    }

    @Override
    public Storage getById(final Long id) {
        LOGGER.trace("Enter and execute 'StorageServiceImpl.getById(final Long id)' method");
        return storageRepository.findStoragesByIdWithClients(id)
                .orElseThrow(
                        () -> {
                            LOGGER.warn("We don't have storage with id= " + id);
                            return new NotFoundException("Please chose different Storage.");
                        }
                );
    }

    @Override
    public List<Storage> getStorages() {
        LOGGER.trace("Enter in 'StorageServiceImpl.getStorages()' method");

        List<Storage> storages = new ArrayList<>();
        storageRepository.findAll().iterator().forEachRemaining(storages :: add);
        LOGGER.debug("Find all Storages from DB and add them to HashSet");

        LOGGER.trace("'StorageServiceImpl.getStorages()' executed successfully.");
        return storages;
    }

    @Override
    @Transactional
    public StorageDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter and execute 'StorageServiceImpl.findCommandById(final Long id)' method");
        return mapper.storageToStorageDTO(getById(id));
    }

    @Override
    @Transactional
    public StorageDTO saveDTO(final StorageDTO storageDTO) {
        LOGGER.trace("Enter in 'StorageServiceImpl.saveDetached(final FacilityCommand command)' method");

        Client client = clientRepository.findById(storageDTO.getClientId()).orElseThrow(() -> {
            LOGGER.warn("We don't have Client with id= " + storageDTO.getClientId());
            return new NotFoundException("We don't have this Client. Please choose another one.");
        });

        Storage detachedStorage = mapper.storageDTOtoStorage(storageDTO);
        detachedStorage.getAddress().setFacility(detachedStorage);
        detachedStorage.setClient(client);
        Storage savedStorage = storageRepository.save(detachedStorage);

        LOGGER.debug("We save Storage with id= " + savedStorage.getId());
        LOGGER.trace("'StorageServiceImpl.saveDetached(final FacilityCommand command)' executed successfully.");
        return mapper.storageToStorageDTO(savedStorage);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter and execute 'StorageServiceImpl.deleteById(final Long id)' method");

        storageRepository.deleteById(id);
        LOGGER.debug("Delete Storage with id= " + id);

        LOGGER.trace("'StorageServiceImpl.deleteById(final Long id)' executed successfully.");
    }

    @Override
    @Transactional
    public void saveImage(Long id, MultipartFile multipartFile) {
        LOGGER.trace("Enter in 'StorageServiceImpl.saveImage(Long id, MultipartFile file)' method");

        try {
            Storage storage = storageRepository.findById(id).orElseThrow(() -> {
                LOGGER.warn("We don't have Storage with id= " + id);
                return new NotFoundException("We don't have this Storage. Please choose another one.");
            });
            storage.setImage(multipartFile.getBytes());
            storageRepository.save(storage);
            LOGGER.debug("We set Image and save Storage with id= " + id);
        } catch (IOException e) {
            LOGGER.error("Attention can't execute multipartFile.getBytes() method", e);
            throw new ImageCorruptedException(e.getMessage());
        }

        LOGGER.trace("'StorageServiceImpl.saveImage(Long id, MultipartFile file)' executed successfully.");
    }

    @Override
    public List<StorageDTO> getStoragesDTO() {
        return getStorages()
                .stream()
                .map(mapper::storageToStorageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StorageDTO patchStorage(Long id, StorageDTO storageDTO) {
        return storageRepository.findById(id).map(storage -> {
            if (storageDTO.getCommercialCapacity() != null) {
                storage.setCommercialCapacity(storageDTO.getCommercialCapacity());
            }

            if (storageDTO.getMonthRent() != null) {
                storage.setMonthRent(storageDTO.getMonthRent());
            }

            if (storageDTO.getPrice() != null) {
                storage.setPrice(storageDTO.getPrice());
            }

            if (storageDTO.getNumberOfRooms() != null) {
                storage.setNumberOfRooms(storageDTO.getNumberOfRooms());
            }

            if (storageDTO.getTotalArea() != null) {
                storage.setTotalArea(storageDTO.getTotalArea());
            }

            if (storageDTO.getDescription() != null) {
                storage.setDescription(storageDTO.getDescription());

            }

            if (storageDTO.getPublishedDateTime() != null) {
                storage.setPublishedDateTime(storageDTO.getPublishedDateTime());
            }

            if (storageDTO.getClosedDateTime() != null) {
                storage.setClosedDateTime(storageDTO.getClosedDateTime());
            }

            if (storageDTO.getStatus() != null) {
                storage.setStatus(storageDTO.getStatus());
            }

            if (storageDTO.getImage() != null) {
                storage.setImage(storageDTO.getImage());
            }

            storage.setHasCargoEquipment(storageDTO.isHasCargoEquipment());

            return mapper.storageToStorageDTO(storageRepository.save(storage));
        }).orElseThrow(RuntimeException::new);
    }
}
