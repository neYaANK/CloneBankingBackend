package me.neyaank.clonebankingbackend.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateRequest {
    private String phoneNumber;
    private String email;
}
