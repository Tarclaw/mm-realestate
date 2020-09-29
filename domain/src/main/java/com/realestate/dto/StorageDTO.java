package com.realestate.dto;

public class StorageDTO extends FacilityDTO {

    private Integer commercialCapacity;
    private boolean hasCargoEquipment;

    public StorageDTO() {
    }

    public Integer getCommercialCapacity() {
        return commercialCapacity;
    }

    public void setCommercialCapacity(Integer commercialCapacity) {
        this.commercialCapacity = commercialCapacity;
    }

    public boolean isHasCargoEquipment() {
        return hasCargoEquipment;
    }

    public void setHasCargoEquipment(boolean hasCargoEquipment) {
        this.hasCargoEquipment = hasCargoEquipment;
    }
}
