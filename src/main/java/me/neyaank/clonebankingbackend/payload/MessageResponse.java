package me.neyaank.clonebankingbackend.payload;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
