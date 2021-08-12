package com.ushkov.security.util;

public interface SecuredRoles {
    String ADMIN = "hasRole('ROLE_ADMIN')";
    String USER = "hasRole('ROLE_USER')";
    String SUPERADMIN = "hasRole('ROLE_SUPERADMIN')";
    String MANAGER = "hasRole('ROLE_MANAGER')";
    String ALL = SUPERADMIN + " or " + ADMIN + " or " + MANAGER + " or " + USER;
    String ALLEXCEPTUSER =  SUPERADMIN + " or " + ADMIN + " or " + MANAGER;
    String ONLYADMINS = SUPERADMIN + " or " + ADMIN;
    String WITHOUTAUTHENTICATION = "permitAll()";
}
