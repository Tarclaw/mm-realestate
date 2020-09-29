package com.realestate.model.buildings;

import com.realestate.model.enums.Status;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "apartments")
public class Apartment extends Facility {

    private Integer apartmentNumber;
    private Integer floor;

    public Apartment() {}

    public Apartment(Integer numberOfRooms, Integer totalArea, String description, LocalDateTime publishedDateTime,
                     BigInteger monthRent, BigInteger price, Status status, Integer apartmentNumber, Integer floor) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status);
        this.apartmentNumber = apartmentNumber;
        this.floor = floor;
    }

    public Apartment(Integer numberOfRooms, Integer totalArea, String description,
                     LocalDateTime publishedDateTime, BigInteger monthRent, BigInteger price,
                     Status status, Address address, Integer apartmentNumber, Integer floor) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status, address);
        this.apartmentNumber = apartmentNumber;
        this.floor = floor;
    }

    public Apartment(Integer numberOfRooms, Integer totalArea, String description,
                     LocalDateTime publishedDateTime, BigInteger monthRent, BigInteger price,
                     Status status, byte[] image, Address address,
                     Integer apartmentNumber, Integer floor) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status, image, address);
        this.apartmentNumber = apartmentNumber;
        this.floor = floor;
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

    @Override
    public String toString() {
        return "Apartment{" +
                "apartmentNumber=" + apartmentNumber +
                ", floor=" + floor +
                '}';
    }
}
