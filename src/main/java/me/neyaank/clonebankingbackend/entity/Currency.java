package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "currencies")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ECurrency name;

    public Currency(ECurrency name) {
        this.name = name;
    }
}
