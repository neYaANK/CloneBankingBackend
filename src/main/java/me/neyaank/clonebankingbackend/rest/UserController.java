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
        if (user.isPresent()) {
            var toReturn = user.get();
            toReturn.setPassword(null);
            return ResponseEntity.ok(new UserInfoResponse(user.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/{id}/image")
    public ResponseEntity uploadImage(@PathVariable Long id, MultipartFile multipartImage) throws IOException {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();
        //if(multipartImage.getContentType() ==
        var userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) return ResponseEntity.notFound().build();
        var user = userOptional.get();
        //Add validation

        var imageRemove = user.getImage();

        var newImage = new Image();
        newImage.setType(multipartImage.getContentType());
        newImage.setImage(multipartImage.getBytes());
        user.setImage(newImage);
        newImage = imageRepository.save(newImage);
        user = userRepository.save(user);
        if (imageRemove != null) imageRepository.deleteById(imageRemove.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/image")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();

        var userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) return ResponseEntity.notFound().build();
        var user = userOptional.get();
        if (user.getImage() != null) {
            imageRepository.deleteById(user.getImage().getId());
            userRepository.save(user);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity getImage(@PathVariable Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();

        var userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) return ResponseEntity.notFound().build();
        var user = userOptional.get();


        var iconBytes = user.getImage().getImage();


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.valueOf(user.getImage().getType()));
        return ResponseEntity.ok()
                .contentLength(iconBytes.length)
                .headers(responseHeaders)
                .body(new InputStreamResource(new ByteArrayInputStream(iconBytes)));
    }
}

