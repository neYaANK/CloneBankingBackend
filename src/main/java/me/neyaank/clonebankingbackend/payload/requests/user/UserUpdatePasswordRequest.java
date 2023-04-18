package me.neyaank.clonebankingbackend.payload.requests.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.neyaank.clonebankingbackend.payload.requests.auth.CodeRequest;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserUpdatePasswordRequest extends CodeRequest {
    private String newPassword;
    private String oldPassword;
}
