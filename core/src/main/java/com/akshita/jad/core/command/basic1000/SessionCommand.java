package com.akshita.jad.core.command.basic1000;

import com.akshita.jad.core.command.model.SessionModel;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.session.Session;
import com.akshita.jad.core.util.UserStatUtil;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

import com.akshita.jad.tunnel.client.TunnelClient;

/**
 * 
 *
 * @author vlinux on 15/5/3.
 */
@Name("session")
@Summary("Display current session information")
public class SessionCommand extends AnnotatedCommand {

    @Override
    public void process(CommandProcess process) {
        SessionModel result = new SessionModel();
        Session session = process.session();
        result.setJavaPid(session.getPid());
        result.setSessionId(session.getSessionId());

        //tunnel
        TunnelClient tunnelClient = JADBootstrap.getInstance().getTunnelClient();
        if (tunnelClient != null) {
            String id = tunnelClient.getId();
            if (id != null) {
                result.setAgentId(id);
            }
            result.setTunnelServer(tunnelClient.getTunnelServerUrl());
            result.setTunnelConnected(tunnelClient.isConnected());
        }

        //statUrl
        String statUrl = UserStatUtil.getStatUrl();
        result.setStatUrl(statUrl);

        //userId
        String userId = session.getUserId();
        result.setUserId(userId);

        process.appendResult(result);
        process.end();
    }

}
