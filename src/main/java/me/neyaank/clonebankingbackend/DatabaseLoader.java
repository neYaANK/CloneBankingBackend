package me.neyaank.clonebankingbackend;

import lombok.AllArgsConstructor;
import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setName("Name1");
        user.setSurname("Surname1");
        user.setPhoneNumber("+380961234567");
        user.setPassword(encoder.encode("testPassword12"));
        userRepository.save(user);
    }
}
