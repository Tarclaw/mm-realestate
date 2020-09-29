package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class AddressListDTO {

    private List<AddressDTO> addresses = new ArrayList<>();

    public AddressListDTO() {
    }

    public AddressListDTO(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }
}
