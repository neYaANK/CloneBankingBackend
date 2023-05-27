package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "credits")
@Getter
@Setter
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_type_id", referencedColumnName = "id")
    private CreditType creditType;
    private double balance;
    private double baseBalance;
    private LocalDate issuedAt;
    private LocalDate lastIncreased;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status creditStatus;

    public Credit() {
        issuedAt = LocalDate.now();
        lastIncreased = LocalDate.now();

    }
}
