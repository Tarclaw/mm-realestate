package com.realestate.controllers.rest;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.AddressListDTO;
import com.realestate.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AddressDtoController.BASE_URL)
public class AddressDtoController {

    public static final String BASE_URL = "/api/v2/addresses";

    private final AddressService service;

    public AddressDtoController(AddressService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AddressListDTO getListOfAddresses() {
        return new AddressListDTO(service.getAddressesDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDTO getAddressById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO createNewAddress(@RequestBody AddressDTO addressDTO) {
        return service.saveAddressDTO(addressDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDTO updateCustomer(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {
        addressDTO.setId(id);
        return service.saveAddressDTO(addressDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDTO patchAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {
        return service.patchAddress(addressDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAddress(@PathVariable Long id) {
        service.deleteById(id);
    }

}
