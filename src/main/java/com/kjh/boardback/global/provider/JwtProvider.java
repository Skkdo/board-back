package com.kjh.boardback.global.provider;

import com.kjh.boardback.global.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisService redisService;

    @Value("${access.token.secret.key}")
    private String accessToken_secretKey;

    @Value("${refresh.token.secret.key}")
    private String refreshToken_secretKey;

    @Value("${access.token.expiration.time}")
    private int accessToken_expirationTime;

    @Value("${refresh.token.expiration.time}")
    private int refreshToken_expirationTime;

    private final String ACCESS_HEADER = "AccessToken";
    private final String REFRESH_HEADER = "RefreshToken";
    private final String BEARER = "Bearer ";

    public String createAccessToken(String email) {

        Key key = Keys.hmacShaKeyFor(accessToken_secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(email).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessToken_expirationTime))
                .compact();
    }

    public String createRefreshToken(String email) {

        Key key = Keys.hmacShaKeyFor(refreshToken_secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(email).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshToken_expirationTime))
                .compact();
    }

    public String validateAccessToken(String accessToken) {
        Claims claims = null;
        Key key = Keys.hmacShaKeyFor(accessToken_secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return claims.getSubject();
    }

    public String validateRefreshToken(String refreshToken) {
        Claims claims = null;
        Key key = Keys.hmacShaKeyFor(refreshToken_secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        String email = claims.getSubject();
        return redisService.getRefreshTokenByEmail(email);
    }

    public String getAccessToken(HttpServletRequest request) {
        return getToken(request, ACCESS_HEADER);
    }

    public String getRefreshToken(HttpServletRequest request) {
        return getToken(request, REFRESH_HEADER);
    }

    private String getToken(HttpServletRequest request, String headerName) {
        String header = request.getHeader(headerName);

        boolean hasText = StringUtils.hasText(header);
        if (!hasText) {
            return null;
        }

        boolean isBearer = header.startsWith(BEARER);
        if (!isBearer) {
            return null;
        }

        return header.substring(7);
    }
}
