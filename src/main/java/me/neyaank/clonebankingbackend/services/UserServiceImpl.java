package me.neyaank.clonebankingbackend.services;

import jakarta.annotation.PostConstruct;
import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${neyaank.clonebanking.profilePictureDirectory}")
    private String profileDirectory;
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

    @PostConstruct
    public void ensureDirectoryExists() throws IOException {
        if (!Files.exists(Path.of(this.profileDirectory))) {
            Files.createDirectories(Path.of(this.profileDirectory));
        }
    }

    @Override
    public Optional<User> findUserByToken(JwtAuthenticationToken token) {
        return userRepository.findById(Long.valueOf(token.getName()));
    }

    @Override
    public User updatePhoneNumber(Long id, String phoneNumber) {
        var userOpt = userRepository.findById(id);
        var user = userOpt.get();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        return user;
    }

    @Override
    public User updatePassword(Long id, String password) {
        var userOpt = userRepository.findById(id);
        var user = userOpt.get();
        user.setPassword(password);
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean comparePasswords(Long id, String password) {
        var user = userRepository.findById(id).get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean setImage(Long id, MultipartFile image) {
        try {
            var user = userRepository.findById(id).get();
            var imageRemove = user.getImagePath();
            ByteArrayInputStream bis = new ByteArrayInputStream(image.getBytes());
            BufferedImage bImage2 = ImageIO.read(bis);
            String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + "." + extension;
            String path = profileDirectory + "/" + filename;
            ImageIO.write(bImage2, extension, new File(path));

            user.setImagePath(filename);
            user = userRepository.save(user);

            if (imageRemove != null) Files.delete(Path.of(path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteImage(Long id) {
        try {
            var userOptional = userRepository.findById(id);
            var user = userOptional.get();
            if (user.getImagePath() != null) {
                Files.delete(Path.of(profileDirectory + "/" + user.getImagePath()));
                user.setImagePath(null);
                userRepository.save(user);

            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public byte[] getImage(Long id) {
        try {
            var user = userRepository.findById(id).get();
            byte[] imageBytes;
            if (user.getImagePath() == null) {
                imageBytes = NO_IMAGE;
            } else {
                var file = new File(profileDirectory + "/" + user.getImagePath());
                BufferedImage bImage = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bImage, FilenameUtils.getExtension(user.getImagePath()), baos);
                imageBytes = baos.toByteArray();
            }
            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public MediaType getImageType(Long id) {
        var userOptional = userRepository.findById(id);
        var user = userOptional.get();

        HttpHeaders responseHeaders = new HttpHeaders();
        if (user.getImagePath() == null) {
            return MediaType.IMAGE_PNG;
        } else {
            var file = new File(profileDirectory + "/" + user.getImagePath());
            return MediaType.valueOf(URLConnection.guessContentTypeFromName(file.getName()));
        }
    }

}
