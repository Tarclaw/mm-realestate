package com.realestate.security;

public enum ApplicationUserPermission {

    WEB_GET_FACILITY("web:get_facility"),
    WEB_GET_AGENT("web:get_agent"),
    WEB_CHANGE_FACILITY("web:change_facility"),
    WEB_CLIENT_ALL("web:client_all"),
    WEB_CHANGE_AGENT("web:change_agent"),
    REST_ALL("rest:all");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
