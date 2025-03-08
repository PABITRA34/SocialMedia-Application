package com.example.SocialMedia.security;

import com.example.SocialMedia.entities.Role;
import com.example.SocialMedia.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    private static final String secretKey = "LouTBHuneWvUFHrHRlwEbIJrrLOZH6SgynUSyUSv85Jynrpxu1qyn2DJh6upqLoubeUOtbrqRfT0dhurLkiy";

    public JWTService(){

//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
    }

    // original
//    public String generateToken(String username){
//        Map<String , Object> claims = new HashMap<>();
//        claims.put("username", username);  // Add user-specific claim
//        return Jwts.builder()
//                .addClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis()*60*60*40))
//                .signWith(getKey())
//                .compact();
//    }
    //testing below
//    public String generateToken(String username, List<String> roles) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", username);  // Add user-specific claim
//        claims.put("roles", roles);  // Add roles to the token
//
//        System.out.println("USER NAME " + username);
//        String compact = Jwts.builder()
//                .addClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // Token expiration
//                .signWith(getKey())
//                .compact();
//
//        System.out.println("THE TOAKEN IS " + compact);
//        return compact;
//    }


    public String generateToken(User user, List<Role> roles) {
        // Get the username and role names
        List<String> roleList = roles.stream()
                .map(Role::getName)
                .toList();

        System.out.println("THE ROLES ARE " + roleList);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUserName());
        claims.put("roles", roleList);

        System.out.println("USER NAME " + user.getPassword());
        String compact = Jwts.builder()
                .addClaims(claims)
                .setSubject(user.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // Token expiration
                .signWith(getKey())
                .compact();

        System.out.println("THE TOKEN IS " + compact);
        return compact;
    }

//    private Key getKey(){
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);  // Decode the secret key
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Get the roles claim and cast it to List<String>
        return (List<String>) claims.get("roles");
    }


    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }



    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
