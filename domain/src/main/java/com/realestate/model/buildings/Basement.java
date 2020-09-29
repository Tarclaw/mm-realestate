package com.realestate.model.buildings;

import com.realestate.model.enums.Status;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "basements")
public class Basement extends Facility {

    private boolean itCommercial;

    public Basement() {}

    public Basement(Integer numberOfRooms, Integer totalArea, String description, LocalDateTime publishedDateTime,
                    BigInteger monthRent, BigInteger price, Status status, boolean itCommercial) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status);
        this.itCommercial = itCommercial;
    }

    public Basement(Integer numberOfRooms, Integer totalArea, String description, LocalDateTime publishedDateTime,
                    BigInteger monthRent, BigInteger price, Status status, Address address, boolean itCommercial) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status, address);
        this.itCommercial = itCommercial;
    }

    public Basement(Integer numberOfRooms, Integer totalArea, String description,
                    LocalDateTime publishedDateTime, BigInteger monthRent, BigInteger price,
                    Status status, byte[] image, Address address, boolean itCommercial) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status, image, address);
        this.itCommercial = itCommercial;
    }

    public boolean isItCommercial() {
        return itCommercial;
    }

    public void setItCommercial(boolean itCommercial) {
        this.itCommercial = itCommercial;
    }

    @Override
    public String toString() {
        return "Basement{" +
                "itCommercial=" + itCommercial +
                '}';
    }
}
