package me.neyaank.clonebankingbackend.services;

import me.neyaank.clonebankingbackend.entity.User;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByToken(JwtAuthenticationToken token);

    User updatePhoneNumber(Long id, String phoneNumber);

    User updatePassword(Long id, String password);

    boolean comparePasswords(Long id, String password);

    boolean setImage(Long id, MultipartFile image);

    boolean deleteImage(Long id);

    byte[] getImage(Long id);

    MediaType getImageType(Long id);
}
