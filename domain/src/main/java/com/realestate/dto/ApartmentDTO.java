package com.realestate.dto;

public class ApartmentDTO extends FacilityDTO {

    private Integer apartmentNumber;
    private Integer floor;

    public ApartmentDTO() {
    }

    public Integer getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(Integer apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}
