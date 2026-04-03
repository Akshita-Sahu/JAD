package com.akshita.jad.core.security;

import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.common.JADConstants;
import com.akshita.jad.core.util.StringUtils;

/**
 * TODO ，command？
 * 
 * @author hengyunabc 2021-03-03
 *
 */
public class SecurityAuthenticatorImpl implements SecurityAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuthenticatorImpl.class);
    private String username;
    private String password;
    private Subject subject;

    public SecurityAuthenticatorImpl(String username, String password) {
        if (username != null && password == null) {
            password = StringUtils.randomString(32);
            logger.info("\nUsing generated security password: {}\n", password);
        }
        if (username == null && password != null) {
            username = JADConstants.DEFAULT_USERNAME;
        }

        this.username = username;
        this.password = password;

        subject = new Subject();
    }

    @Override
    public void setName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRoleClassNames(String names) {
        // TODO Auto-generated method stub

    }

    @Override
    public Subject login(Principal principal) throws LoginException {
        if (principal == null) {
            return null;
        }
        if (principal instanceof BasicPrincipal) {
            BasicPrincipal basicPrincipal = (BasicPrincipal) principal;
            if (basicPrincipal.getName().equals(username) && basicPrincipal.getPassword().equals(this.password)) {
                return subject;
            }
        }
        if (principal instanceof BearerPrincipal) {
            BearerPrincipal bearerPrincipal = (BearerPrincipal) principal;
            // Bearer Token：tokenpassword
            if (bearerPrincipal.getToken().equals(this.password)) {
                return subject;
            }
        }
        if (principal instanceof LocalConnectionPrincipal) {
            return subject;
        }

        return null;
    }

    @Override
    public void logout(Subject subject) throws LoginException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getUserRoles(Subject subject) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean needLogin() {
        return username != null && password != null;
    }

}
