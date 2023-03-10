package com.staticvoid.security.domain;

import com.staticvoid.user.domain.ApplicationUserDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
    private final ApplicationUserDto user;
    private final String error;

    public JwtResponse(String jwtToken, ApplicationUserDto user) {
        this.jwtToken = jwtToken;
        this.user = user;
        error = null;
    }

    public JwtResponse(String error) {
        this.error = error;
        this.jwtToken = null;
        this.user = null;
    }
}
