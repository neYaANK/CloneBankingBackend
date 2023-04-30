package me.neyaank.clonebankingbackend.payload.requests.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PincodeRequest {
    private String pincode;
}
