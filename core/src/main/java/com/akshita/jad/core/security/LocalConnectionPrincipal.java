package com.akshita.jad.core.security;

import java.security.Principal;

/**
 *  {@link Principal}.
 * 
 * @author hengyunabc 2021-09-01
 */
public final class LocalConnectionPrincipal implements Principal {

    public LocalConnectionPrincipal() {
    }

    @Override
    public String getName() {
        return null;
    }

    public String getUsername() {
        return null;
    }

    public String getPassword() {
        return null;
    }
}