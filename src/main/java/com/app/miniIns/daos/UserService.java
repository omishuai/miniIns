package com.app.miniIns.daos;

import com.app.miniIns.entities.ClientUser;
import com.app.miniIns.entities.*;
import com.app.miniIns.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.Random;

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


//    public User addUser(String username, String password, String email, int age, String gender) {
//        try {
//            User n = new User(username, password, email, age, gender);
//            userRepo.save(n);
//            return n;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public ServerUser findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public ServerUser findByUsername(String username) {
        return userRepo.findByUsername(username);
    }


    private String getHashedPassword(String saltedPassword, String algorithm) {
        try {
            //MessageDigest classes Static getInstance method is called with MD5 hashing
            MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
            byte[] inputDigest = msgDigest.digest(saltedPassword.getBytes());

            // Convert byte array into signum representation
            // BigInteger class is used, to convert the resultant byte array into its signum representation
            BigInteger inputDigestBigInt = new BigInteger(1, inputDigest);

            // Convert the input digest into hex value
            String hashtext = inputDigestBigInt.toString(16);

            //Add preceding 0's to pad the hashtext to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // Catch block to handle the scenarios when an unsupported message digest algorithm is provided.
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public ServerUser addUser(@Valid ServerUser user) throws Exception {
        if (findByEmail(user.getEmail()) != null) throw new DuplicateDataException("Existing Email");
        if (findByUsername(user.getUsername()) != null) throw new DuplicateDataException("Existing Username");

        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String salt = new String(array, Charset.forName("UTF-8"));

        user.setSalt(salt);
        String saltedPassword = user.getPassword() + salt ;
        user.setPassword(getHashedPassword(saltedPassword, "SHA-256"));

        return userRepo.save(user);
    }

    public ServerUser verifyInfo(ServerUser user) throws Exception {
        String email =  user.getEmail();
        String password =  user.getPassword();
        String username =  user.getUsername();

        System.out.println("email: " + email + "  username: " +username + "  password: " +password);
        if ((email == null ||email.equals("")) && (username == null || username.equals(""))) throw new EmptyInputException("Please Enter Username or Email");
        if (password == null || password.equals("")) throw new EmptyInputException("Please Enter Password");

        ServerUser savedUser;
        if (email != null && !email.equals("")) {
            savedUser = findByEmail(email);
            if (savedUser == null) throw new VerificationFailureException("Unregistered " + email);

        } else {
            savedUser = findByUsername(username);
            if (savedUser == null) throw new VerificationFailureException("Unregistered " + username);
        }

       System.out.println(savedUser.getPassword() +  "   " + password + ":"+getHashedPassword(password+ savedUser.getSalt(), "SHA-256"));
        if (savedUser.getPassword().equals(getHashedPassword(password + savedUser.getSalt(), "SHA-256"))) return savedUser;
        throw new VerificationFailureException("Incorrect Password");
    }

}
