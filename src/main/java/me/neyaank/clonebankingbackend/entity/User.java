package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String thirdName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @NotNull
    private LocalDate birthday;
    private String phoneNumber;
    private String email;

    private boolean using2FA;
    private String secret;
    @NotBlank
    private String password;
    private String imagePath;

    public User(String name, String surname, String thirdName, LocalDate birthday, String phoneNumber, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.thirdName = thirdName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
    }

    public User(String name, String surname, String thirdName, LocalDate birthday, String phoneNumber, String email, String password, String imagePath) {
        this.name = name;
        this.surname = surname;
        this.thirdName = thirdName;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
    }

    public static byte[] NO_IMAGE;

    static {
        try {
            BufferedImage bImage = ImageIO.read(User.class.getClassLoader().getResource("images/no_user.png"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", baos);
            NO_IMAGE = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
