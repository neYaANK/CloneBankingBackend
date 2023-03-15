package me.neyaank.clonebankingbackend;

import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public DatabaseLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setName("Artem");
        user.setSurname("Nersesian");
        user.setPhoneNumber("+380961234567");
        user.setPassword("testPassword");
        //userRepository.save(user);
    }
}
