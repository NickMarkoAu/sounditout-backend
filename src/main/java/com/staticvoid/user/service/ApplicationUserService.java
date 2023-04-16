package com.staticvoid.user.service;

import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.respository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder bcryptEncoder;

    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return user;
    }

    public ApplicationUser loadUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    public ApplicationUser save(ApplicationUserDto user) {
        //TODO when storing a new user we need to create s3 directories for them
        ApplicationUser newUser = new ApplicationUser();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }
}
