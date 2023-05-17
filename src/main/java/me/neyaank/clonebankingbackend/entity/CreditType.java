package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credit_types")
@Getter
@Setter
@NoArgsConstructor
public class CreditType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double percentPerMonth;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public CreditType(double percentPerMonth, Currency currency) {
        this.percentPerMonth = percentPerMonth;
        this.currency = currency;
    }
}
