package com.akshita.jad.core.shell.term.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.akshita.jad.common.JADConstants;
import com.akshita.jad.core.shell.session.Session;

import io.termd.core.readline.Function;
import io.termd.core.readline.Readline;
import io.termd.core.readline.Readline.Interaction;

/**
 *  Function  apply 
 * 
 * @author hengyunabc 2023-08-24
 *
 */
public class FunctionInvocationHandler implements InvocationHandler {

    private TermImpl termImpl;

    private Function target;

    public FunctionInvocationHandler(TermImpl termImpl, Function target) {
        this.termImpl = termImpl;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String name = method.getName();

        if (name.equals("apply")) {
            Session session = termImpl.getSession();
            if (session != null) {
                boolean authenticated = session.get(JADConstants.SUBJECT_KEY) != null;
                if (authenticated) {
                    return method.invoke(target, args);
                } else {
                    Readline.Interaction interaction = (Interaction) args[0];
                    // 
                    interaction.resume();
                    return null;
                }
            }
        }

        return method.invoke(target, args);
    }

}
