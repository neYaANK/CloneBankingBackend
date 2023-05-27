package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "payment_systems")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class PaymentSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EPaymentSystem name;

    public PaymentSystem(EPaymentSystem name) {
        this.name = name;
    }
}