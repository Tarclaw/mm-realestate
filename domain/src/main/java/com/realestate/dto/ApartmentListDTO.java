package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class ApartmentListDTO {

    private List<ApartmentDTO> apartmentDTOS = new ArrayList<>();

    public ApartmentListDTO() {
    }

    public ApartmentListDTO(List<ApartmentDTO> apartmentDTOS) {
        this.apartmentDTOS = apartmentDTOS;
    }

    public List<ApartmentDTO> getApartmentDTOS() {
        return apartmentDTOS;
    }

    public void setApartmentDTOS(List<ApartmentDTO> apartmentDTOS) {
        this.apartmentDTOS = apartmentDTOS;
    }
}
