package com.melhamra.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melhamra.api.ApiApplication;
import com.melhamra.api.SpringApplicationContext;
import com.melhamra.api.dtos.UserDto;
import com.melhamra.api.entities.UserEntity;
import com.melhamra.api.requests.UserLoginRequest;
import com.melhamra.api.services.UserService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            UserLoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword(), new ArrayList<>())
            );
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((User) authResult.getPrincipal()).getUsername();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        UserDto userEntity = userService.getUserByEmail(email);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("id", userEntity.getUserID())
                .claim("name", userEntity.getFirstName() + " " + userEntity.getLastName())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();

        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("user_id", userEntity.getUserID());

        response.getWriter().write("{\"token\": \""+token+"\", \"id\": \""+userEntity.getUserID()+"\"}");
    }
}
