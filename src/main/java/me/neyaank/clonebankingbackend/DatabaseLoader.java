package me.neyaank.clonebankingbackend;

import me.neyaank.clonebankingbackend.entity.*;
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

        Set<Card> cards = Set.of(new Card(bin + "7890001234", LocalDate.of(2023, 10, 1), "123", "0000", Currency.UAH, CardType.DEBIT, PaymentSystem.MASTERCARD));
        user.setCards(cards);

        userRepository.save(user);
    }
}
