package me.neyaank.clonebankingbackend.payload.requests.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdatePhoneRequest {
    private String phoneNumber;
}
