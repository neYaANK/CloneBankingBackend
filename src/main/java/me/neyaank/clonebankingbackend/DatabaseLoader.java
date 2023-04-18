package me.neyaank.clonebankingbackend;

import lombok.AllArgsConstructor;
import me.neyaank.clonebankingbackend.entity.Card;
import me.neyaank.clonebankingbackend.entity.ERole;
import me.neyaank.clonebankingbackend.entity.Role;
import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.repository.RoleRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role(ERole.ROLE_NO_2FA));
        roleRepository.save(new Role(ERole.ROLE_2FA));

        User user = new User();
        user.setName("Name1");
        user.setSurname("Surname1");
        user.setThirdName("Thirdname1");
        user.setBirthday(LocalDate.of(2000, 7, 6));
        user.setPhoneNumber("+380961234567");
        user.setEmail("admin@gmail.com");
        user.setRoles(Set.of(roleRepository.findByName(ERole.ROLE_2FA).get()));
        user.setPassword(encoder.encode("testPassword12"));

        Set<Card> cards = Set.of(new Card("1234 5678 9000 1234", LocalDate.of(2023, 10, 1), "123", "0000"));
        user.setCards(cards);

        userRepository.save(user);
    }
}
