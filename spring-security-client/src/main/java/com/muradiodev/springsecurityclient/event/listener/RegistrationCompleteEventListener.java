package com.muradiodev.springsecurityclient.event.listener;

import com.muradiodev.springsecurityclient.entity.User;
import com.muradiodev.springsecurityclient.event.RegistrationCompleteEvent;
import com.muradiodev.springsecurityclient.service.UserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.UUID;


@Slf4j
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the Verification Token for the User with Link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        // Send mail to User
        String url = event.getApplicationUrl()
                + "verifyRegistration?token="
                + token;

        // sendVerificationEmail()
        log.info("Click the link to verify your account: {}", url);
    }
}
