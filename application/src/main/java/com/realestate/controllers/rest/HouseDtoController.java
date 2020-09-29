package com.realestate.controllers.rest;

import com.realestate.dto.HouseDTO;
import com.realestate.dto.HouseListDTO;
import com.realestate.service.HouseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/houses")
public class HouseDtoController {

    private final HouseService service;

    public HouseDtoController(HouseService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HouseListDTO getListOfHouses() {
        return new HouseListDTO(service.getHousesDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HouseDTO getHouseById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HouseDTO createNewHouse(@RequestBody HouseDTO houseDTO) {
        return service.saveDTO(houseDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HouseDTO updateHouse(@PathVariable Long id, @RequestBody HouseDTO houseDTO) {
        houseDTO.setId(id);
        return service.saveDTO(houseDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HouseDTO patchHouse(@PathVariable Long id, @RequestBody HouseDTO houseDTO) {
        return service.patchHouse(id, houseDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteHouse(@PathVariable Long id) {
        service.deleteById(id);
    }
}
