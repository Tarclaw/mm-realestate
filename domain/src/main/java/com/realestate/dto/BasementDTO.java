package com.realestate.dto;

public class BasementDTO extends FacilityDTO {

    private boolean itCommercial;

    public BasementDTO() {
    }

    public boolean isItCommercial() {
        return itCommercial;
    }

    public void setItCommercial(boolean itCommercial) {
        this.itCommercial = itCommercial;
    }
}
