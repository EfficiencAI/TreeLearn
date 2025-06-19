package site.arookieofc.aichat.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JWTUtil {

    @Value("${jwt.issue}")
    private static String JWT_ISS;
    @Value("${jwt.secretKey}")
    private static String SECRET;
    @Value("${jwt.expiration}")
    public static int ACCESS_EXPIRE;
    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;
    public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String genAccessToken() {
        String uuid = UUID.randomUUID().toString();
        Date exprireDate = Date.from(Instant.now().plusSeconds(ACCESS_EXPIRE));
        return Jwts.builder()
                .id(uuid)
                .expiration(exprireDate)
                .issuedAt(new Date())
                .issuer(JWT_ISS)
                .signWith(KEY, ALGORITHM)
                .compact();
    }

    public static Jws<Claims> parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token);
    }
}
