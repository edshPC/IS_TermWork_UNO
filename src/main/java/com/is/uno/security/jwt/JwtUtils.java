package com.is.uno.security.jwt;

import com.is.uno.security.service.AuthUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long expirationMs = 1000 * 60 * 60 * 6; // Время действия токена: 6 час

    private final AuthUserDetailsService authUserDetailsService;

    public String generateJwtToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String getUserNameFromJwtToken(String authToken) {
        try {
            return getParsedToken(authToken).getBody().getSubject();
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage());

            return null;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            getParsedToken(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    private Jws<Claims> getParsedToken(String authToken) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
    }

    public String parseJwt(String authHeader) {
        //String headerAuth = request.getHeader("Authorization");
        String bearerPrefix = "Bearer ";
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(bearerPrefix)) {
            return authHeader.substring(bearerPrefix.length());
        }

        return null;
    }

    public UserDetails getUserDetails(String token) {
        if (token == null || !validateJwtToken(token))
            return null;

        String login = getUserNameFromJwtToken(token);
        return authUserDetailsService.loadUserByUsername(login);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        var userDetails = getUserDetails(token);
        if (userDetails == null) return null;
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}