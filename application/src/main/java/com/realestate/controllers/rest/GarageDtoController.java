package com.realestate.controllers.rest;

import com.realestate.dto.GarageDTO;
import com.realestate.dto.GarageListDTO;
import com.realestate.service.GarageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/garages")
public class GarageDtoController {

    private final GarageService service;

    public GarageDtoController(GarageService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GarageListDTO getListOfGarages() {
        return new GarageListDTO(service.getGaragesDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GarageDTO getGarageById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GarageDTO createNewGarage(@RequestBody GarageDTO garageDTO) {
        return service.saveDTO(garageDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GarageDTO updateGarage(@PathVariable Long id, @RequestBody GarageDTO garageDTO) {
        garageDTO.setId(id);
        return service.saveDTO(garageDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GarageDTO patchGarage(@PathVariable Long id, @RequestBody GarageDTO garageDTO) {
        return service.patchGarage(id, garageDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGarage(@PathVariable Long id) {
        service.deleteById(id);
    }
}
