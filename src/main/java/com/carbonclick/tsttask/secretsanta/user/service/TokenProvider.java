package com.carbonclick.tsttask.secretsanta.user.service;

import com.carbonclick.tsttask.secretsanta.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class TokenProvider {

    private final String SECRET = "SECRET";

    @Autowired
    private UserService userService;

    public String createToken(String username) {

        UserDetails user = userService.loadUserByUsername(username);

        Claims claims = Jwts.claims().setSubject(username);
        /* No roles so far
        ArrayList<String> rolesList = new ArrayList<String>();

        for (Role role : user.getUser().getUserRoles()) {
            rolesList.add(role.getRole());
        }

        claims.put("roles", rolesList);
        */

        String token = Jwts.builder().setClaims(claims)
                // TODO: Add token refresh
                .setExpiration(getExpiratinDate()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        return token;
    }

    public Authentication getAuthentication(String token) {
        String username = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        UserDetails userDetails = this.userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Date getExpiratinDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }
}