package com.realestate.controllers.rest;

import com.realestate.dto.BasementDTO;
import com.realestate.dto.BasementListDTO;
import com.realestate.service.BasementService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/basements")
public class BasementDtoController {

    private final BasementService service;

    public BasementDtoController(BasementService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BasementListDTO getListOfBasements() {
        return new BasementListDTO(service.getBasementsDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BasementDTO getBasementById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BasementDTO createNewBasement(@RequestBody BasementDTO basementDTO) {
        return service.saveDTO(basementDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BasementDTO updateBasement(@PathVariable Long id, @RequestBody BasementDTO basementDTO) {
        basementDTO.setId(id);
        return service.saveDTO(basementDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BasementDTO patchBasement(@PathVariable Long id, @RequestBody BasementDTO basementDTO) {
        return service.patchBasement(id, basementDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBasement(@PathVariable Long id) {
        service.deleteById(id);
    }
}
