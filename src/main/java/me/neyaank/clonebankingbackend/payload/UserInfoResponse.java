package me.neyaank.clonebankingbackend.payload;

import lombok.AllArgsConstructor;
import me.neyaank.clonebankingbackend.entity.User;

import java.time.LocalDate;

@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String name;
    private String surname;
    private String thirdName;
    private LocalDate birthday;
    private String phoneNumber;

    public UserInfoResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        thirdName = user.getThirdName();
        birthday = user.getBirthday();
        phoneNumber = user.getPhoneNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
