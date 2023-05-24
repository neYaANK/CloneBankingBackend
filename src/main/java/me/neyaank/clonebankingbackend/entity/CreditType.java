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
    private ECurrency ECurrency;

    public CreditType(double percentPerMonth, ECurrency ECurrency) {
        this.percentPerMonth = percentPerMonth;
        this.ECurrency = ECurrency;
    }
}
