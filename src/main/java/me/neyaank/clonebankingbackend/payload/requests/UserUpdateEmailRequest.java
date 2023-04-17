package me.neyaank.clonebankingbackend.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateEmailRequest {
    private String email;
}
