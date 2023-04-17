package me.neyaank.clonebankingbackend.payload.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;
    @NotBlank
    @Size(max = 14)
    private String phoneNumber;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

