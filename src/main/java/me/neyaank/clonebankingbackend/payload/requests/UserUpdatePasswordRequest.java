package me.neyaank.clonebankingbackend.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserUpdatePasswordRequest extends CodeRequest {
    private String newPassword;
    private String oldPassword;
}
