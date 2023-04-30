package me.neyaank.clonebankingbackend.payload.requests.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserUpdatePasswordRequest {
    private String newPassword;
    private String oldPassword;
}
