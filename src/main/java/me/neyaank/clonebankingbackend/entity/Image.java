package me.neyaank.clonebankingbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Entity
@Table(name = "images")
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] image;
    @NotBlank
    private String type;


    @OneToOne(mappedBy = "image", cascade = CascadeType.MERGE)
    private User user;


    public static Image NO_IMAGE;

    static {
        try {
            BufferedImage image = null;
            image = ImageIO.read(new File(Image.class.getClassLoader().getResource("images/no_user.png").getPath()));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);
            NO_IMAGE = new Image();
            NO_IMAGE.setType(MediaType.IMAGE_PNG_VALUE);
            NO_IMAGE.setImage(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
