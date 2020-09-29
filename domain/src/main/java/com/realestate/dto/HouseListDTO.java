package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class HouseListDTO {

    private List<HouseDTO> houseDTOS = new ArrayList<>();

    public HouseListDTO() {
    }

    public HouseListDTO(List<HouseDTO> houseDTOS) {
        this.houseDTOS = houseDTOS;
    }

    public List<HouseDTO> getHouseDTOS() {
        return houseDTOS;
    }

    public void setHouseDTOS(List<HouseDTO> houseDTOS) {
        this.houseDTOS = houseDTOS;
    }
}
