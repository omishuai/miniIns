package com.app.miniIns.security;

import com.app.miniIns.daos.UserService;
import com.app.miniIns.entities.ServerUser;
import com.app.miniIns.exceptions.MyAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import java.util.*;


@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = (String) authentication.getPrincipal();
        String password = authentication.getCredentials().toString();
        System.out.printf("principal: %s\ncredentials: %s\nname:%s\n",
                authentication.getPrincipal(),
                authentication.getCredentials(),
                authentication.getName()
        );
        ServerUser candidate = new ServerUser();
        if (name.contains("@")) candidate.setEmail(name);
        else candidate.setUsername(name);

        candidate.setPassword(password);
        ServerUser returnedUser;
        try {
            returnedUser = userService.verifyInfo(candidate);
        } catch (Exception e) { throw new MyAuthenticationException(e.getMessage()); }
        if (returnedUser != null)
            return new UsernamePasswordAuthenticationToken(
                    returnedUser, password, new ArrayList<>());
        return null;
    }
    @Override
    public boolean supports(Class<?> aClass) { return false; }

    private boolean checkAgainsDB(String user, String password){
        ServerUser record;
        if (user.contains("@")) record = userService.findByEmail(user);
        else record = userService.findByUsername(user);
        if (record == null) return false;
        String salt = record.getSalt();
        String expectedPassword = record.getPassword();
        String passwordCandidate = password + salt;
        String hashedPassword = userService.getHashedPassword(passwordCandidate, "SHA-256");
        if (hashedPassword.equals(expectedPassword)) return true;
        return false;
    }

}
