package com.realestate.controllers.rest;

import com.realestate.dto.ApartmentDTO;
import com.realestate.dto.ApartmentListDTO;
import com.realestate.service.ApartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/apartments")
public class ApartmentDtoController {

    private final ApartmentService service;

    public ApartmentDtoController(ApartmentService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApartmentListDTO getListOfApartments() {
        return new ApartmentListDTO(service.getApartmentsDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApartmentDTO getApartmentById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApartmentDTO createNewApartment(@RequestBody ApartmentDTO apartmentDTO) {
        return service.saveDTO(apartmentDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApartmentDTO updateApartment(@PathVariable Long id, @RequestBody ApartmentDTO apartmentDTO) {
        apartmentDTO.setId(id);
        return service.saveDTO(apartmentDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApartmentDTO patchApartment(@PathVariable Long id, @RequestBody ApartmentDTO apartmentDTO) {
        return service.patchApartment(apartmentDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteApartment(@PathVariable Long id) {
        service.deleteById(id);
    }
}
