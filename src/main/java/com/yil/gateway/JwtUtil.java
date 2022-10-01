/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.gateway;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return this.getClaims(token).getExpiration().before(new Date());
    }

    public Claims getClaims(final String token) {
        try {
            String authToken = token;
            if (token != null && token.startsWith("Bearer ")) {
                authToken = token.substring(7);
            }
            Claims body = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();
            return body;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature >" + token);
            throw ex;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token >" + token);
            throw ex;
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token >" + token);
            throw ex;
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token >" + token);
            throw ex;
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty >" + token);
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

}