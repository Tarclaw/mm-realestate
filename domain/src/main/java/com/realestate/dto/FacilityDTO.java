package com.realestate.dto;

import com.realestate.model.enums.Status;

import javax.validation.constraints.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class FacilityDTO {

    private Long id;
    private Long clientId;

    @NotNull(message = "Please enter value")
    private BigInteger monthRent;

    @NotNull(message = "Please enter value")
    private BigInteger price;

    @NotNull(message = "Please enter value")
    @Min(value = 1, message = "Impossible to have less then one room")
    @Max(value = 100, message = "Sorry we don't work with such amount of rooms")
    private Integer numberOfRooms;

    @NotNull(message = "Please enter value")
    @Min(value = 20, message = "Can't be less then 20 square meters")
    @Max(value = 1000, message = "Can't be bigger then 1000 square meters")
    private Integer totalArea;

    @NotNull(message = "Please enter value")
    @Size(min = 10, max = 1000, message = "Please provide description between 10 and 1000 characters")
    private String description;

    @PastOrPresent
    private LocalDateTime publishedDateTime;

    @FutureOrPresent
    private LocalDateTime closedDateTime;

    private Status status;

    private byte[] image;

    private AddressDTO addressDTO;

    public FacilityDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public BigInteger getMonthRent() {
        return monthRent;
    }

    public void setMonthRent(BigInteger monthRent) {
        this.monthRent = monthRent;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Integer totalArea) {
        this.totalArea = totalArea;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPublishedDateTime() {
        return publishedDateTime;
    }

    public void setPublishedDateTime(LocalDateTime publishedDateTime) {
        this.publishedDateTime = publishedDateTime;
    }

    public LocalDateTime getClosedDateTime() {
        return closedDateTime;
    }

    public void setClosedDateTime(LocalDateTime closedDateTime) {
        this.closedDateTime = closedDateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
    }
}
