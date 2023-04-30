package me.neyaank.clonebankingbackend.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.neyaank.clonebankingbackend.entity.ERole;
import me.neyaank.clonebankingbackend.entity.User;
import me.neyaank.clonebankingbackend.repository.UserRepository;
import me.neyaank.clonebankingbackend.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Autowired
    private JwtEncoder encoder;
    @Value("${neyaank.clonebanking.jwtSecret}")
    private String jwtSecret;

    @Value("${neyaank.clonebanking.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${neyaank.clonebanking.tempTokenMs}")
    public long tempTokenExpirationMs;
    @Autowired
    UserRepository userRepository;

    public String generateJwtToken(Authentication authentication, boolean authenticated) {
        User user;
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
            user = userRepository.findById(userPrincipal.getId()).stream().findFirst().get();
        } else {
            JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
            user = userRepository.findById(Long.valueOf(token.getName())).get();
        }
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (authenticated ? jwtExpirationMs : tempTokenExpirationMs));
        String scope = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(" "));
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now.toInstant())
                .expiresAt(expiryDate.toInstant())
                .subject(Long.toString(user.getId()))
                .claim("authorities", authenticated ? scope : ERole.NO_2FA.name())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}