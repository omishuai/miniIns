package com.app.miniIns.security;

import com.app.miniIns.entities.ServerUser;
import com.app.miniIns.exceptions.MyAuthenticationException;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import static com.app.miniIns.security.SecurityConstants.*;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private MyAuthenticationProvider myAuthenticationProvider;

    public JWTAuthenticationFilter(MyAuthenticationProvider authenticationManager) {
        this.myAuthenticationProvider = authenticationManager;
    }
    //when a user is attempting to log in
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws MyAuthenticationException,AuthenticationException {
        try {
            //Need to evaluate getInputStream
            //Might not be able to map back to the user. coz the userinfo from user could be either email or username
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(req.getInputStream()));

            String line = in.readLine().replace("user=","").replace("password=","");
            System.out.println(line);

            String[] params = line.split("&");
            // Need to modify the parameter regarding how user login info is passed in from client
            return myAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            params[0].replace("%40", "@"),
                            params[1],
                            new ArrayList<>())
            );
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    @Override //when the user logs in successfully, and a JWT will be returned
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(((ServerUser) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        res.getWriter().write(((ServerUser) auth.getPrincipal()).toString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        if (failed instanceof MyAuthenticationException) {

            MyAuthenticationException ex = (MyAuthenticationException) failed;
            System.out.println("UNSUCCESS:" + ex.getMessage());
            response.sendError((HttpServletResponse.SC_UNAUTHORIZED), ex.getMessage());

        }
    }
}
