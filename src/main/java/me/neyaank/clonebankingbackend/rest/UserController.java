package me.neyaank.clonebankingbackend.rest;

import jakarta.annotation.PostConstruct;
import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.payload.requests.UserUpdateRequest;
import me.neyaank.clonebankingbackend.payload.responses.UserInfoResponse;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.security.services.UserDetailsImpl;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Value("${neyaank.clonebanking.profilePictureDirectory}")
    private String profileDirectory;

    @PostConstruct
    public void ensureDirectoryExists() throws IOException {
        if (!Files.exists(Path.of(this.profileDirectory))) {
            Files.createDirectories(Path.of(this.profileDirectory));
        }
    }

    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final List<String> VALID_MIME = List.of(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE);


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoResponse> userById(@PathVariable Long id) {
        if (!isAuthenticatedUser(id)) return ResponseEntity.badRequest().build();

        var user = userRepository.findById(id);
        var toReturn = user.get();
        toReturn.setPassword(null);
        return ResponseEntity.ok(new UserInfoResponse(user.get()));
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        if (!isAuthenticatedUser(id)) return ResponseEntity.badRequest().build();
        var user = userRepository.findById(id).get();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/image")
    public ResponseEntity uploadImage(@PathVariable Long id, MultipartFile image) throws IOException {
        if (!isAuthenticatedUser(id)) return ResponseEntity.badRequest().build();


        if (!VALID_MIME.contains(image.getContentType())) return ResponseEntity.badRequest().body("Not valid image");
        var userOptional = userRepository.findById(id);
        var user = userOptional.get();

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
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/image")
    public ResponseEntity deleteImage(@PathVariable Long id) throws IOException {
        if (!isAuthenticatedUser(id)) return ResponseEntity.badRequest().build();


        var userOptional = userRepository.findById(id);
        var user = userOptional.get();
        if (user.getImagePath() != null) {
            Files.delete(Path.of(profileDirectory + "/" + user.getImagePath()));
            user.setImagePath(null);
            userRepository.save(user);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity getImage(@PathVariable Long id) throws IOException {

        if (!isAuthenticatedUser(id)) return ResponseEntity.badRequest().build();
        var userOptional = userRepository.findById(id);
        var user = userOptional.get();

        HttpHeaders responseHeaders = new HttpHeaders();

        byte[] imageBytes;
        if (user.getImagePath() == null) {
            responseHeaders.setContentType(MediaType.IMAGE_PNG);
            responseHeaders.setContentLength(User.NO_IMAGE.length);
            imageBytes = User.NO_IMAGE;
        } else {
            var file = new File(profileDirectory + "/" + user.getImagePath());
            BufferedImage bImage = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bImage, FilenameUtils.getExtension(user.getImagePath()), baos);

            imageBytes = baos.toByteArray();
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            responseHeaders.setContentType(MediaType.valueOf(mimeType));
            responseHeaders.setContentLength(imageBytes.length);
        }
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new InputStreamResource(new ByteArrayInputStream(imageBytes)));
    }

    private boolean isAuthenticatedUser(Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return false;
        return true;
    }
}