package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class BasementListDTO {

    private List<BasementDTO> basementDTOS = new ArrayList<>();

    public BasementListDTO() {
    }

    public BasementListDTO(List<BasementDTO> basementDTOS) {
        this.basementDTOS = basementDTOS;
    }

    public List<BasementDTO> getBasementDTOS() {
        return basementDTOS;
    }

    public void setBasementDTOS(List<BasementDTO> basementDTOS) {
        this.basementDTOS = basementDTOS;
    }
}
