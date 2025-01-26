package org.app.config;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    // todo

    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return value;
    }
}
