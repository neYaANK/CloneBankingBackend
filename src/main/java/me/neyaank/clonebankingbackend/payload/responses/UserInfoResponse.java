package me.neyaank.clonebankingbackend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.neyaank.clonebankingbackend.entity.User;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<String> roles;


    public UserInfoResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        thirdName = user.getThirdName();
        birthday = user.getBirthday();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        roles = user.getRoles().stream().map(c -> c.getName()).map(c -> c.name()).collect(Collectors.toSet());
    }

}
