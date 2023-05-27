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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    public CreditType(double percentPerMonth, Currency currency) {
        this.percentPerMonth = percentPerMonth;
        this.currency = currency;
    }
}
