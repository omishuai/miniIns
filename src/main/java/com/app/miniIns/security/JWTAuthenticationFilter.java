package com.app.miniIns.security;

import com.app.miniIns.entities.ServerUser;
import com.app.miniIns.exceptions.MyAuthenticationException;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import static com.app.miniIns.security.SecurityConstants.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private MyAuthenticationProvider myAuthenticationProvider;
    public JWTAuthenticationFilter(MyAuthenticationProvider authenticationManager) {
        this.myAuthenticationProvider = authenticationManager;
    }

    //When a user attempts to log in
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String line = in.readLine().replace("username=","").replace("password=","");
            line = line.replace("%40", "@");
            int index = line.indexOf('&');
            return myAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            line.substring(0,index),
                            line.substring(index + 1),
                            new ArrayList<>())
            );
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    //When the user logs in, and a JWT will be returned
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((ServerUser) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        res.getWriter().write(auth.getPrincipal().toString());
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        if (failed instanceof MyAuthenticationException) {

            MyAuthenticationException ex = (MyAuthenticationException) failed;

            Map<String, Object> data = new HashMap<>();
            data.put("message", ex.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getOutputStream().write((objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8)));

        }
    }
}
