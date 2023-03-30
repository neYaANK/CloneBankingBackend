package me.neyaank.clonebankingbackend;

import lombok.AllArgsConstructor;
import me.neyaank.clonebankingbackend.entity.Image;
import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.repository.ImageRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ImageRepository imageRepository;

    @Override
    public void run(String... args) throws Exception {
        BufferedImage image = ImageIO.read(new File(getClass().getClassLoader().getResource("images/no_user.png").getPath()));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);
        Image userImg = new Image();
        userImg.setType(MediaType.IMAGE_PNG_VALUE);
        userImg.setImage(bos.toByteArray());

        User user = new User();
        user.setName("Name1");
        user.setSurname("Surname1");
        user.setThirdName("Thirdname1");
        user.setBirthday(LocalDate.of(2000, 7, 6));
        user.setPhoneNumber("+380961234567");
        user.setPassword(encoder.encode("testPassword12"));

        userImg.setUser(user);
        user.setImage(userImg);

        imageRepository.save(userImg);
        userRepository.save(user);
    }
}
