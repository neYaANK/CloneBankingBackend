package me.neyaank.clonebankingbackend.payload.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private List<String> roles;

    public JwtResponse(String token, Long id, String name, String surname, String phoneNumber, List<String> roles) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
}
