package com.staticvoid.user.service;

import com.staticvoid.security.jwt.JwtTokenUtil;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.respository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmail(username);
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
        newUser.setEmail(user.getEmail());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
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
}
