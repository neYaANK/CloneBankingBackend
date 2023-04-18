package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String cardNumber;
    @NotNull
    private LocalDate expireDate;
    @NotBlank
    private String cv2;
    @NotBlank
    private String pinCode;

    public Card(String cardNumber, LocalDate expireDate, String cv2, String pinCode) {
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        this.cv2 = cv2;
        this.pinCode = pinCode;
    }
}
