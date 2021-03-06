package com.muradiodev.springsecurityclient.service;

import com.muradiodev.springsecurityclient.entity.User;
import com.muradiodev.springsecurityclient.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);
}
