package me.neyaank.clonebankingbackend.rest;

import lombok.AllArgsConstructor;
import me.neyaank.clonebankingbackend.payload.UserInfoResponse;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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

        var userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) return ResponseEntity.notFound().build();
        var user = userOptional.get();
        user.setIcon(multipartImage.getBytes());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/image")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();

        var userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) return ResponseEntity.notFound().build();
        var user = userOptional.get();
        user.setIcon(null);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getImage(@PathVariable Long id) {
        var auth = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!auth.getId().equals(id)) return ResponseEntity.badRequest().build();

        var userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) return ResponseEntity.notFound().build();
        var user = userOptional.get();
        var iconBytes = user.getIcon();
        return ResponseEntity.ok()
                .contentLength(iconBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(iconBytes)));
    }
}

