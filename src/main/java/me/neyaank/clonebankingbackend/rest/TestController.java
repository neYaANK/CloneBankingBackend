package me.neyaank.clonebankingbackend.rest;

import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/test")
public class TestController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/testtest")
    public String testAccess() {
        return "Test";
    }
}
