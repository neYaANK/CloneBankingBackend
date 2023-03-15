package me.neyaank.clonebankingbackend.rest.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/test")
public class TestController {
    @GetMapping("/testtest")
    public String testAccess() {
        return "Aboba aboba";
    }
}
