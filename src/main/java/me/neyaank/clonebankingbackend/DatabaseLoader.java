package me.neyaank.clonebankingbackend;

import me.neyaank.clonebankingbackend.entity.*;
import me.neyaank.clonebankingbackend.repository.CardRepository;
import me.neyaank.clonebankingbackend.repository.RoleRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DatabaseLoader implements CommandLineRunner {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Value("${neyaank.clonebanking.BIN}")
    private String bin;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role(ERole.NO_2FA));
        roleRepository.save(new Role(ERole.WITH_2FA));
        User user = new User();
        user.setName("Name1");
        user.setSurname("Surname1");
        user.setThirdName("Thirdname1");
        user.setBirthday(LocalDate.of(2000, 7, 6));
        user.setPhoneNumber("+380961234567");
        user.setEmail("admin@gmail.com");
        user.setRoles(Set.of(roleRepository.findByName(ERole.WITH_2FA).get()));
        user.setPassword(encoder.encode("testPassword12"));
        var c1 = new Card(bin + "7890001234", LocalDate.of(2023, 10, 1), "123", "0000", Currency.UAH, CardType.DEBIT, PaymentSystem.MASTERCARD);
        c1.setBalance(500);
        c1.setUser(user);
        //List<Card> cards = List.of(c1);
        //user.setCards(cards);
        userRepository.save(user);
        cardRepository.save(c1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setSurname("Surname2");
        user2.setThirdName("Thirdname2");
        user2.setBirthday(LocalDate.of(1980, 9, 1));
        user2.setPhoneNumber("+380960000000");
        user2.setEmail("notadmin@gmail.com");
        user2.setRoles(Set.of(roleRepository.findByName(ERole.WITH_2FA).get()));
        user2.setPassword(encoder.encode("testPassword12"));
        var c = new Card(bin + "7890001111", LocalDate.of(2024, 8, 1), "321", "0000", Currency.UAH, CardType.DEBIT, PaymentSystem.MASTERCARD);
        c.setBalance(150);
        c.setUser(user2);
//        List<Card> cards2 = List.of(c);
//        user2.setCards(cards2);
        userRepository.save(user2);
        cardRepository.save(c);
    }
}
