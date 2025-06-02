package com.example.pasir_lipior_michal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Component
//public class JwtUtil {
//
//    // Tworzy silny klucz do podpisywania tokenów
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//
//    public String generateToken(String email) {
//            // Czas ważności tokena: 1 godzina
//            long expirationMs = 3600000;
//        return Jwts.builder()
//                .setSubject(email) // 'sub': kto jest właścicielem tokena
//                .setIssuedAt(new Date()) // 'iat': kiedy został wydany
//                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // 'exp': kiedy wygasa
//                .signWith(key) // podpisanie kluczem
//                .compact(); // zakończ budowanie i zwróć token jako String
//    }
//    public String extractUsername(String token) {
//
//        return Jwts.parserBuilder() // JwtParserBuilder
//                .setSigningKey(key) // ustawiamy klucz do weryfikacji podpisu
//                .build() // JwtParser
//                .parseClaimsJws(token) // parsujemy token jako JWS (JSON Web Signature)
//                .getBody() // pobieramy claims (ładunek)
//                .getSubject(); // odczytujemy 'sub' (czyli email)
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            // jeśli parsowanie się uda - token poprawny
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            // nieprawidłowy token (np. podpis, wygasł, błędny format)
//            return false;
//        }
//    }
//}

@Component
public class JwtUtil {

    // 🔑 stabilny klucz, nie zmienia się po każdym uruchomieniu (ważne!)
    private static final Key key = Keys.hmacShaKeyFor("mojaMegaBezpiecznaTajnaSekretnaFrazaDoJwtTokenu1234567890ABCDEF@#".getBytes()); // 2 usages

    /**
     * ✅ Generowanie tokena na podstawie użytkownika
     */
    public String generateToken(com.example.pasir_lipior_michal.model.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        long expirationMs = 3600000; // 1 godzina

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // dla kompatybilności
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // jeżeli parsowanie się uda, to OK
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}