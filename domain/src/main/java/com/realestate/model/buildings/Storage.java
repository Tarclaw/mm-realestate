package com.realestate.model.buildings;

import com.realestate.model.enums.Status;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "storages")
public class Storage extends Facility {

    private Integer commercialCapacity;
    private boolean hasCargoEquipment;

    public Storage() {}

    public Storage(Integer numberOfRooms, Integer totalArea, String description,
                   LocalDateTime publishedDateTime, BigInteger monthRent, BigInteger price,
                   Status status, Integer commercialCapacity, boolean hasCargoEquipment) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status);
        this.commercialCapacity = commercialCapacity;
        this.hasCargoEquipment = hasCargoEquipment;
    }

    public Storage(Integer numberOfRooms, Integer totalArea, String description,
                   LocalDateTime publishedDateTime, BigInteger monthRent, BigInteger price,
                   Status status, Address address, Integer commercialCapacity, boolean hasCargoEquipment) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status, address);
        this.commercialCapacity = commercialCapacity;
        this.hasCargoEquipment = hasCargoEquipment;
    }

    public Storage(Integer numberOfRooms, Integer totalArea, String description,
                   LocalDateTime publishedDateTime, BigInteger monthRent,
                   BigInteger price, Status status, byte[] image,
                   Address address, Integer commercialCapacity, boolean hasCargoEquipment) {
        super(numberOfRooms, totalArea, description, publishedDateTime, monthRent, price, status, image, address);
        this.commercialCapacity = commercialCapacity;
        this.hasCargoEquipment = hasCargoEquipment;
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

    @Override
    public String toString() {
        return "Storage{" +
                "commercialCapacity=" + commercialCapacity +
                ", hasCargoEquipment=" + hasCargoEquipment +
                '}';
    }
}
