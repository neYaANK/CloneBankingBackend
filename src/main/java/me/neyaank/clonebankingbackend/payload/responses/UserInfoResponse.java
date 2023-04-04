package me.neyaank.clonebankingbackend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.User;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    private Long id;
    private String name;
    private String surname;
    private String thirdName;
    private LocalDate birthday;
    private String phoneNumber;
    private String email;

    public UserInfoResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        thirdName = user.getThirdName();
        birthday = user.getBirthday();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
    }

}
