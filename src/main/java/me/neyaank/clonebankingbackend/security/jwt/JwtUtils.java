package me.neyaank.clonebankingbackend.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.neyaank.clonebankingbackend.entity.ERole;
import me.neyaank.clonebankingbackend.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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

    public String generateJwtToken(Authentication authentication, boolean authenticated) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (authenticated ? jwtExpirationMs : tempTokenExpirationMs));
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now.toInstant())
                .expiresAt(expiryDate.toInstant())
                .subject(Long.toString(userPrincipal.getId()))
                .claim("authorities", authenticated ? scope : ERole.NO_2FA.name())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}