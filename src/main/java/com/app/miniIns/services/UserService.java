package com.app.miniIns.services;

import com.app.miniIns.entities.*;
import com.app.miniIns.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class UserService {

    public UserRepository getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @Autowired
    private UserRepository userRepo;


    public ServerUser findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public ServerUser findByUsername(String username) {
        return userRepo.findByUsername(username);
    }


    public ServerUser addUser(@Valid ServerUser user) throws Exception {
        if (findByEmail(user.getEmail()) != null) throw new DuplicateDataException("Existing Email");
        if (findByUsername(user.getUsername()) != null) throw new DuplicateDataException("Existing Username");

        String salt = BCrypt.gensalt();
        user.setSalt(salt);

        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(hashedPassword);
        return userRepo.save(user);
    }

    public ServerUser verifyInfo(String account, String password) throws Exception {
        if (account == null || (account.equals(""))) throw new EmptyInputException("Please Enter Username or Email");

        String email = account.contains("@") ? account : "";
        String username =  account.contains("@") ? "": account;

        if (password == null || password.equals("")) throw new EmptyInputException("Please Enter Password");

        ServerUser savedUser;
        if (!email.equals("")) {
            savedUser = findByEmail(email);
            if (savedUser == null) throw new VerificationFailureException("Unregistered " + email);
        } else {
            savedUser = findByUsername(username);
            if (savedUser == null) throw new VerificationFailureException("Unregistered " + username);
        }

        if (savedUser.getPassword().equals(BCrypt.hashpw(password, savedUser.getSalt()))) return savedUser;
        throw new VerificationFailureException("Incorrect Password");
    }

}
