package com.akshita.jad.core.command.basic1000;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.common.JADConstants;
import com.akshita.jad.core.command.Constants;
import com.akshita.jad.core.security.BasicPrincipal;
import com.akshita.jad.core.security.SecurityAuthenticator;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.shell.cli.Completion;
import com.akshita.jad.core.shell.cli.CompletionUtils;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.session.Session;
import com.akshita_sahu.middleware.cli.annotations.Argument;
import com.akshita_sahu.middleware.cli.annotations.DefaultValue;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Option;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * TODO 。 username/password
 * 
 * @author hengyunabc 2021-03-03
 *
 */
// @formatter:off
@Name(JADConstants.AUTH)
@Summary("Authenticates the current session")
@Description(Constants.EXAMPLE +
        "  auth\n" +
        "  auth <password>\n" +
        "  auth --username <username> <password>\n"
        + Constants.WIKI + Constants.WIKI_HOME + JADConstants.AUTH)
//@formatter:on
public class AuthCommand extends AnnotatedCommand {
    private static final Logger logger = LoggerFactory.getLogger(AuthCommand.class);

    private String username;
    private String password;
    private SecurityAuthenticator authenticator = JADBootstrap.getInstance().getSecurityAuthenticator();

    @Argument(argName = "password", index = 0, required = false)
    @Description("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Option(shortName = "n", longName = "username")
    @Description("username, default value 'jad'")
    @DefaultValue(JADConstants.DEFAULT_USERNAME)
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void process(CommandProcess process) {
        int status = 0;
        String message = "";
        try {
            Session session = process.session();
            if (username == null) {
                status = 1;
                message = "username can not be empty!";
                return;
            }
            if (password == null) { // password，
                boolean authenticated = session.get(JADConstants.SUBJECT_KEY) != null;
                boolean needLogin = this.authenticator.needLogin();

                message = "Authentication result: " + authenticated + ", Need authentication: " + needLogin;
                if (needLogin && !authenticated) {
                    status = 1;
                }
                return;
            } else {
                // 
                BasicPrincipal principal = new BasicPrincipal(username, password);
                try {
                    Subject subject = authenticator.login(principal);
                    if (subject != null) {
                        // subject  session，
                        session.put(JADConstants.SUBJECT_KEY, subject);
                        message = "Authentication result: " + true + ", username: " + username;
                    } else {
                        status = 1;
                        message = "Authentication result: " + false + ", username: " + username;
                    }
                } catch (LoginException e) {
                    logger.error("Authentication error, username: {}", username, e);
                }
            }
        } finally {
            process.end(status, message);
        }
    }

    @Override
    public void complete(Completion completion) {
        if (!CompletionUtils.completeFilePath(completion)) {
            super.complete(completion);
        }
    }

}
