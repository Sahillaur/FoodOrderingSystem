package com.foodOrdering.FoodOrderingSystem.security;

import com.foodOrdering.FoodOrderingSystem.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")  // secretkey ka path jo application properties mai likhi hui h
    private String secretKey; // secretKey ki valur ism assign ho jayegi

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)); // secretkey ko bytes mai convert krega
    }

    //JWT genrate token code
    public String generateToken(String username, Role role) {
        return Jwts.builder()  // token create start
                .subject(username)  //Token ke andar username store karta hai.
                .claim("role",role.name())  //JWT mai role add karega
                .issuedAt(new Date())  //Token kab generate hua, wo time store karta hai.
                .expiration(new Date(System.currentTimeMillis() +1000 * 60 * 30)) //Token ki expiry set karta hai.
                .signWith(getSigningKey())  //Yahi signature token ko verify karne me later kaam aayega.
                .compact();  //Saari JWT information ko ek actual JWT String/token me convert karta hai.
    }
    // refresh token ke liye
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))                .compact();
    }
    // JWT Filter token verify
    public String extractUsername(String token) {
        return Jwts.parser()  //JWT ko read/parse karna start karta hai.
                .verifyWith(getSigningKey())  //Token ki signature ko hamari secret key se verify karta hai.
                .build()  //Parser ready karta hai.
                .parseSignedClaims(token)  //JWT token ko parse karta hai aur uske claims/payload ko read karta hai.
                .getPayload()  //Token ka payload nikalta hai.
                .getSubject();  //Hamne token generate karte waqt:me username store kiya tha.Isliye yahan getSubject() se wahi username mil jayega.
    }
    // Role add ke liye
    public String extractRole(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);  // Payload me se "role" claim nikalta hai
        // String.class batata hai ki role String format me hai
    }
}