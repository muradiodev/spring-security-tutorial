package com.muradiodev.springsecurityclient.service;

import com.muradiodev.springsecurityclient.entity.User;
import com.muradiodev.springsecurityclient.entity.VerificationToken;
import com.muradiodev.springsecurityclient.model.UserModel;
import com.muradiodev.springsecurityclient.repository.UserRepository;
import com.muradiodev.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {

        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if (verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "Token is expired";
        }
        user.setEnabled(true);
        userRepository.save(user);

        return "valid";
    }
}
