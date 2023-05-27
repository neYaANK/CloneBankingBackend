package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

//TODO: move enumerated to separate table
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
    private double balance = 0;
    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "card_type_id", referencedColumnName = "id")
    private CardType type;
    @ManyToOne
    @JoinColumn(name = "payment_system_id", referencedColumnName = "id")
    private PaymentSystem paymentSystem;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Card(String cardNumber, LocalDate expireDate, String cv2, String pinCode, Currency currency, CardType type, PaymentSystem paymentSystem) {
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        this.cv2 = cv2;
        this.currency = currency;
        this.type = type;
        this.paymentSystem = paymentSystem;
    }
}
