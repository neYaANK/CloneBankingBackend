package me.neyaank.clonebankingbackend.rest;

import lombok.AllArgsConstructor;
import me.neyaank.clonebankingbackend.entity.Image;
import me.neyaank.clonebankingbackend.payload.UserInfoResponse;
import me.neyaank.clonebankingbackend.repository.ImageRepository;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoResponse> userById(@PathVariable Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();
        var user = userRepository.findById(id);
        var toReturn = user.get();
        toReturn.setPassword(null);
        return ResponseEntity.ok(new UserInfoResponse(user.get()));
    }

    @PostMapping(value = "/{id}/image")
    public ResponseEntity uploadImage(@PathVariable Long id, MultipartFile image) throws IOException {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();

        List<String> validMIME = List.of(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE);
        if (!validMIME.contains(image.getContentType())) return ResponseEntity.badRequest().body("Not valid image");
        var userOptional = userRepository.findById(id);
        var user = userOptional.get();
        //Add validation

        var imageRemove = user.getImage();

        var newImage = new Image();
        newImage.setType(image.getContentType());
        newImage.setImage(image.getBytes());
        user.setImage(newImage);
        user = userRepository.save(user);
        newImage = imageRepository.save(newImage);

        if (imageRemove != null) imageRepository.deleteById(imageRemove.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/image")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();

        var userOptional = userRepository.findById(id);
        var user = userOptional.get();
        if (user.getImage() != null) {
            imageRepository.deleteById(user.getImage().getId());
            user.setImage(null);
            userRepository.save(user);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity getImage(@PathVariable Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();

        var userOptional = userRepository.findById(id);
        var user = userOptional.get();

        HttpHeaders responseHeaders = new HttpHeaders();

        byte[] imageBytes;
        if (user.getImage() == null) {
            responseHeaders.setContentType(MediaType.valueOf(Image.NO_IMAGE.getType()));
            responseHeaders.setContentLength(Image.NO_IMAGE.getImage().length);
            imageBytes = Image.NO_IMAGE.getImage();
        } else {
            responseHeaders.setContentType(MediaType.valueOf(user.getImage().getType()));
            responseHeaders.setContentLength(user.getImage().getImage().length);
            imageBytes = user.getImage().getImage();
        }
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new InputStreamResource(new ByteArrayInputStream(imageBytes)));
    }
}