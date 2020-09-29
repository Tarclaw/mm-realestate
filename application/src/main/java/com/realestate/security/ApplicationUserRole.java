package com.realestate.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.realestate.security.ApplicationUserPermission.*;


public enum ApplicationUserRole {

    USER(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(WEB_GET_FACILITY, WEB_CHANGE_FACILITY, WEB_GET_AGENT, WEB_CHANGE_AGENT, WEB_CLIENT_ALL, REST_ALL)),
    AGENT(Sets.newHashSet(WEB_GET_FACILITY, WEB_CHANGE_FACILITY, WEB_CLIENT_ALL)),
    CLIENT(Sets.newHashSet(WEB_GET_FACILITY, WEB_GET_AGENT));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
