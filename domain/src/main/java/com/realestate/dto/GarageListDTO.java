package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class GarageListDTO {

    private List<GarageDTO> garageDTOS = new ArrayList<>();

    public GarageListDTO() {
    }

    public GarageListDTO(List<GarageDTO> garageDTOS) {
        this.garageDTOS = garageDTOS;
    }

    public List<GarageDTO> getGarageDTOS() {
        return garageDTOS;
    }

    public void setGarageDTOS(List<GarageDTO> garageDTOS) {
        this.garageDTOS = garageDTOS;
    }
}
