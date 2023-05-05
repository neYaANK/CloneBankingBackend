package me.neyaank.clonebankingbackend.rest.auth;

import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import me.neyaank.clonebankingbackend.payload.requests.auth.CodeRequest;
import me.neyaank.clonebankingbackend.payload.requests.auth.LoginRequest;
import me.neyaank.clonebankingbackend.payload.responses.JwtResponse;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.security.jwt.JwtUtils;
import me.neyaank.clonebankingbackend.services.TwilioSMSService;
import me.neyaank.clonebankingbackend.services.UserDetailsImpl;
import me.neyaank.clonebankingbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;
import static me.neyaank.clonebankingbackend.security.utils.TOTPUtility.generateSecretKey;
import static me.neyaank.clonebankingbackend.security.utils.TOTPUtility.getTOTPCode;
import static me.neyaank.clonebankingbackend.security.utils.Util.generateSecureCode;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    TwilioSMSService smsService;
    @Value("${neyaank.clonebanking.usesTwilio}")
    boolean usesTwilio;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws QrGenerationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword()));
        var details = (UserDetailsImpl) authentication.getPrincipal();
        var user = userService.findUserById(details.getId()).get();
        List<String> roles = user.getRoles().stream()
                .map(item -> item.getName().name())
                .collect(Collectors.toList());

        if (!usesTwilio) {
            //If 2FA is not set (so mostly is first start)
            if (!user.isUsing2FA()) {
                user.setUsing2FA(true);
                user.setSecret(generateSecretKey());
                userRepository.save(user);

                QrData data = new QrData.Builder()
                        .label(user.getEmail())
                        .secret(user.getSecret())
                        .issuer("neYaANK")
                        .build();
                // Generate the QR code image data as a base64 string which can
                // be used in an <img> tag:
                QrGenerator generator = new ZxingPngQrGenerator();
                String qrCodeImage = getDataUriForImage(generator.generate(data), generator.getImageMimeType());
                String jwt = jwtUtils.generateJwtToken(authentication, false);
                return ResponseEntity.ok(new JwtResponse(jwt,
                        user.getId(),
                        user.getName(),
                        user.getSurname(),
                        user.getPhoneNumber(), roles, false, qrCodeImage));
            }
        } else {
            smsService.sendCode(user.getPhoneNumber(), generateSecureCode(9999, 4));
        }
        String jwt = jwtUtils.generateJwtToken(authentication, false);

        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPhoneNumber(), roles, true, null));
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('NO_2FA')")
    public ResponseEntity<?> verifyCode(@NotEmpty @RequestBody CodeRequest code) {
        var auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var user = userService.findUserByToken(auth).get();
        if (!usesTwilio) {
            String secretKey = user.getSecret();
            String realCode = getTOTPCode(secretKey);
            if (!realCode.equals(code.getCode())) {
                return ResponseEntity.badRequest().body("Code is invalid!");
            }
        } else {
            if (!smsService.verifyCode(user.getPhoneNumber(), code.getCode()))
                return ResponseEntity.badRequest().body("Code is invalid!");
        }
        String jwt = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().getAuthentication(), true);
        List<String> roles = user.getRoles().stream()
                .map(item -> item.getName().name())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new

                JwtResponse(jwt,
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPhoneNumber(), roles, true, null));
    }

//For testing purposes will leave it commented here

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Phone number is already taken!"));
//        }
////
////        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
////            return ResponseEntity
////                    .badRequest()
////                    .body(new MessageResponse("Error: Email is already in use!"));
////        }
//
//        // Create new user's account
//        User user = new User(signUpRequest.getName(),
//                signUpRequest.getSurname(),
//                signUpRequest.getPhoneNumber(),
//                encoder.encode(signUpRequest.getPassword()));
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }
}