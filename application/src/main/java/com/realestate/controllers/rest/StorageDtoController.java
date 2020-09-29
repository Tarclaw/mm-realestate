package com.realestate.controllers.rest;

import com.realestate.dto.StorageDTO;
import com.realestate.dto.StorageListDTO;
import com.realestate.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/storages")
public class StorageDtoController {

    private final StorageService service;

    public StorageDtoController(StorageService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StorageListDTO getListOfStorages() {
        return new StorageListDTO(service.getStoragesDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StorageDTO getStorageById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StorageDTO saveNewStorage(@RequestBody StorageDTO storageDTO) {
        return service.saveDTO(storageDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StorageDTO updateStorage(@PathVariable Long id, @RequestBody StorageDTO storageDTO) {
        storageDTO.setId(id);
        return service.saveDTO(storageDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StorageDTO patchStorage(@PathVariable Long id, @RequestBody StorageDTO storageDTO) {
        return service.patchStorage(id, storageDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStorage(@PathVariable Long id) {
        service.deleteById(id);
    }
}
