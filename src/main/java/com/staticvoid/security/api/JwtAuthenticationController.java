package com.staticvoid.security.api;

import com.staticvoid.security.domain.JwtRequest;
import com.staticvoid.security.domain.JwtResponse;
import com.staticvoid.security.jwt.JwtTokenUtil;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.PasswordResetToken;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.service.ApplicationUserService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ApplicationUserService userDetailsService;

    @PostMapping(value = "/api/auth/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new JwtResponse(e.getMessage()));
        }

        final ApplicationUser userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token, ApplicationUserDto.toDto(userDetails)));
    }

    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<?> saveUser(@RequestBody ApplicationUserDto user) throws Exception {
        return ResponseEntity.ok(userDetailsService.saveNew(user));
    }

    @PostMapping(value = "/api/auth/request-reset/")
    public ResponseEntity<?> requestPasswordReset(@RequestBody ApplicationUserDto user) throws Exception {
        try {
            userDetailsService.forgotPassword(user.toEntity());
            return ResponseEntity.ok("If a user with that email exists, a password reset email has been sent.");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(value = "/api/auth/validate-reset/{token}")
    public ResponseEntity<?> validatePasswordResetToken(@PathVariable("token") String token, @RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            ApplicationUser user = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            if(user == null) {
                return ResponseEntity.badRequest().body("Invalid token");
            }
            PasswordResetToken resetToken = userDetailsService.validatePasswordResetToken(token, user);
            if(resetToken != null) {
                user = userDetailsService.updatePassword(user, authenticationRequest.getPassword());
            }
            return ResponseEntity.ok(user);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping(value = "/api/auth/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());

        final ApplicationUser userDetails = userDetailsService
                .loadUserByUsername(expectedMap.get("sub").toString());

        return ResponseEntity.ok(new JwtResponse(token, ApplicationUserDto.toDto(userDetails)));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        return new HashMap<>(claims);
    }
}
