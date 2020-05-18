package com.app.miniIns.services;

import com.app.miniIns.entities.*;
import com.app.miniIns.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    public void followUser(String follower, String followed) {
        User user1 = findByUsername(follower);
        User user2 = findByUsername(followed);
        user1.getFollows().add(user2);
        userRepo.save(user1);
    }

    public void stopFollowUser(String follower, String followed) {
        User user1 = findByUsername(follower);
        User user2 = findByUsername(followed);
        user1.getFollows().remove(user2);
        userRepo.save(user1);
    }


    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    public List<User> findAll() {
        Iterator<User> itr = userRepo.findAll().iterator();
        List<User> res = new ArrayList<>();
        while (itr.hasNext()) res.add(itr.next());
        return res;
    }

    public UserForHome findByUsernameProjection(String username) {
        return userRepo.findByUsernameProjection(username);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username, User.class);
    }


    synchronized public User addUser(@Valid User user) throws Exception {
        if (findByEmail(user.getEmail()) != null) throw new DuplicateDataException("Existing Email");
        if (findByUsername(user.getUsername()) != null) throw new DuplicateDataException("Existing Username");

        String salt = BCrypt.gensalt();
        user.setSalt(salt);

        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(hashedPassword);
        return userRepo.save(user);
    }

    public UserTemplate verifyInfo(String account, String password) throws Exception {
        if (account == null || (account.equals(""))) throw new EmptyInputException("Please Enter Username or Email");

        String email = account.contains("@") ? account : "";
        String username =  account.contains("@") ? "": account;

        if (password == null || password.equals("")) throw new EmptyInputException("Please Enter Password");

        UserTemplate userTemplate;
        if (!email.equals("")) {
            userTemplate = userRepo.findByEmailByProjection(email);
            if (userTemplate == null) throw new VerificationFailureException("Unregistered " + email);
        } else {
            userTemplate = userRepo.findByUsernameByProjection(username);
            if (userTemplate == null) throw new VerificationFailureException("Unregistered " + username);
        }

        if (userTemplate.getPassword().equals(BCrypt.hashpw(password, userTemplate.getSalt()))) return userTemplate;
        throw new VerificationFailureException("Incorrect Password");
    }

}
