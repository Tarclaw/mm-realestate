package com.realestate.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContactDTO {

    @Email(message = "Please enter valid email address")
    private String email;

    @NotNull(message = "Please enter value")
    @Size(min = 2, max = 50, message = "Please enter value between 2 and 50 characters")
    private String skype;

    @NotNull(message = "Please enter value")
    @Size(min = 10, max = 17, message = "Please provide valid mobile")
    private String mobileNumber;

    public ContactDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
