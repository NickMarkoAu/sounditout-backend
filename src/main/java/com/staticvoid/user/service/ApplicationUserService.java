package com.staticvoid.user.service;

import com.staticvoid.user.domain.ConfirmationToken;
import com.staticvoid.user.domain.PasswordResetToken;
import com.staticvoid.user.respository.ConfirmationTokenRepository;
import com.staticvoid.email.service.EmailService;
import com.staticvoid.security.jwt.JwtTokenUtil;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.respository.ApplicationUserRepository;
import com.staticvoid.user.respository.PasswordResetTokenRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private PasswordResetTokenRespository passwordResetTokenRespository;

    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmailIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return user;
    }

    public ApplicationUser loadUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public ApplicationUser saveNew(ApplicationUserDto user) {
        //TODO when storing a new user we need to create s3 directories for them.
        // Do we though? I'm pretty sure S3 will create the directories for us.
        ApplicationUser newUser = new ApplicationUser();
        ApplicationUser existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if(existingUser != null) {
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
        newUser.setEmail(user.getEmail());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        emailService.sendConfirmEmail(newUser);
        return userRepository.save(newUser);
    }

    public ApplicationUser updatePassword(ApplicationUser user, String newPassword) {
        user.setPassword(bcryptEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public ApplicationUser confirmUser(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            ApplicationUser user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            return userRepository.save(user);
        }

        throw new RuntimeException("Invalid confirmation token");
    }

    public void forgotPassword(ApplicationUser user) {
        ApplicationUser existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if(existingUser != null) {
            emailService.sendForgotEmail(user);
        }
        //If user doesn't exist, don't do anything, don't want to give away that a user exists.
    }

    public ApplicationUser save(ApplicationUser user) {
        return userRepository.save(user);
    }

    public Page<ApplicationUser> search(String query, Pageable pageable) {
        return userRepository.search(query, pageable);
    }

    public ApplicationUser getUserByToken(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return loadUserByUsername(username);
    }

    public PasswordResetToken validatePasswordResetToken(String token, ApplicationUser user) {
        final PasswordResetToken passToken = passwordResetTokenRespository.findByPasswordResetToken(token);
        if(!isTokenFound(passToken)) {
            throw new RuntimeException("Token not found");
        }
        //if token user doesn't match the user we're trying to reset, throw an error
        ApplicationUser tokenUser = passToken.getUser();
        if(!user.getEmail().equals(tokenUser.getEmail())) {
            throw new RuntimeException("Invalid token");
        }
        if(!isTokenExpired(passToken)) {
            throw new RuntimeException("Token expired");
        }
        return passToken;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Date date = new Date();
        return passToken.getExpiryDate().before(date);
    }
}
