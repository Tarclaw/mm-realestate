package com.realestate.dto;

public class GarageDTO extends FacilityDTO {

    private boolean hasPit;
    private boolean hasEquipment;

    public GarageDTO() {
    }

    public boolean isHasPit() {
        return hasPit;
    }

    public void setHasPit(boolean hasPit) {
        this.hasPit = hasPit;
    }

    public boolean isHasEquipment() {
        return hasEquipment;
    }

    public void setHasEquipment(boolean hasEquipment) {
        this.hasEquipment = hasEquipment;
    }
}
