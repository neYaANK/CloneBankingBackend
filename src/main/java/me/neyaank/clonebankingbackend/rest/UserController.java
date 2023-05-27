package me.neyaank.clonebankingbackend.rest;

import me.neyaank.clonebankingbackend.exception.InvalidCodeException;
import me.neyaank.clonebankingbackend.payload.requests.user.UserUpdateEmailRequest;
import me.neyaank.clonebankingbackend.payload.requests.user.UserUpdatePasswordRequest;
import me.neyaank.clonebankingbackend.payload.requests.user.UserUpdatePhoneRequest;
import me.neyaank.clonebankingbackend.payload.responses.UserInfoResponse;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    private final List<String> VALID_MIME = List.of(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE);

    /*I am really sorry for this workaround, but user id is in the subject of JwtAuthorizationToken
    The type is String and I need to convert id parameter to String and this is the solution I found :(
    */
    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoResponse> userById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserInfoResponse(userRepository.findById(id).get()));
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}/email")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody UserUpdateEmailRequest request) {
        var user = userRepository.findById(id).get();
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}/phone")
    public ResponseEntity updateUserPhone(@PathVariable Long id, @RequestBody UserUpdatePhoneRequest request) {
        userService.updatePhoneNumber(id, request.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}/password")
    public ResponseEntity updateUserPassword(@PathVariable Long id, @RequestBody UserUpdatePasswordRequest request) {
        if (!userService.comparePasswords(id, request.getOldPassword()))
            throw new InvalidCodeException("Wrong password");
        userService.updatePassword(id, request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @PostMapping(value = "/{id}/image")
    public ResponseEntity uploadImage(@PathVariable Long id, MultipartFile image) {
        if (!VALID_MIME.contains(image.getContentType())) return ResponseEntity.badRequest().body("Not valid image");
        if (!userService.setImage(id, image))
            return ResponseEntity.internalServerError().body("Error with setting user image");
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @DeleteMapping(value = "/{id}/image")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        if (!userService.deleteImage(id)) return ResponseEntity.internalServerError().body("Error deleting image");
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("(#id+'') == authentication.getToken().getSubject()")
    @GetMapping(value = "/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable Long id) throws IOException {
        var imageBytes = userService.getImage(id);
        var imageType = userService.getImageType(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(imageType);
        responseHeaders.setContentLength(imageBytes.length);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new InputStreamResource(new ByteArrayInputStream(imageBytes)));
    }


}