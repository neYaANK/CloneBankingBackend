package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@NoArgsConstructor
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
    @NotNull
    private LocalDate birthday;
    private String phoneNumber;
    private String email;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
