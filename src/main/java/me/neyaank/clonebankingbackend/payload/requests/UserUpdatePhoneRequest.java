package me.neyaank.clonebankingbackend.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdatePhoneRequest extends CodeRequest {
    private String phoneNumber;
}
