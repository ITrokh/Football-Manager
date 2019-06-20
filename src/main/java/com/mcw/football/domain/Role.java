package com.mcw.football.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, PLAYER, TEAM_LEADER;

    @Override
    public String getAuthority() {
        return name();
    }
}
