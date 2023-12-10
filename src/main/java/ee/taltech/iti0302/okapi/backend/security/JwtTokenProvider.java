package ee.taltech.iti0302.okapi.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
//    @Value("${app.secret}")
    private String secret = "9a02115a835ee03d5fb83cd8a468ea33e4090aaaec87f53c9fa54512bbef4db8dc656c82a315fa0c785c08b0134716b81ddcd0153d2a7556f2e154912cf5675f";
//    @Value("${app.jwtExpirationRate}")
    private int expirationRate = 604800000;

    private final byte[] secretBytes = Decoders.BASE64.decode(secret);
    Key key = Keys.hmacShaKeyFor(secretBytes);

    public String generateToken(String nickname) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", nickname);
        return Jwts.builder()
                .setSubject("domain")
                .addClaims(claims)
                .setIssuedAt(new Date())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
