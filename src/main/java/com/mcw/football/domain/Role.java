package com.mcw.football.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, STUDENT, TEAM_LEADER;

    @Override
    public String getAuthority() {
        return name();
    }

    public String valueOf(Role role) {
        return role.valueOf(role);
    }
}
