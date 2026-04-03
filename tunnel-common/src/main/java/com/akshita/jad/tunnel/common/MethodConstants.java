package com.akshita.jad.tunnel.common;

/**
 * tunnel clientserver URI，URI method，
 * 
 * @author hengyunabc 2020-10-22
 *
 */
public class MethodConstants {

    /**
     * 
     * <pre>
     * tunnel client method
     * 
     * ws://192.168.1.10:7777/ws?method=agentRegister
     * 
     * tunnel server：
     * 
     * response:/?method=agentRegister&id=bvDOe8XbTM2pQWjF4cfw
     * 
     * id，
     * </pre>
     */
    public static final String AGENT_REGISTER = "agentRegister";

    /**
     * <pre>
     * tunnel server  tunnel client
     * 
     * response:/?method=startTunnel&id=bvDOe8XbTM2pQWjF4cfw&clientConnectionId=AMku9EFz2gxeL2gedGOC
     * </pre>
     */
    public static final String START_TUNNEL = "startTunnel";
    /**
     * <pre>
     * browser tunnel server tunnel client
     * 
     * ws://192.168.1.10:7777/ws?method=connectJAD&id=bvDOe8XbTM2pQWjF4cfw
     * </pre>
     */
    public static final String CONNECT_JAD = "connectJAD";

    /**
     * <pre>
     * tunnel client startTunnel ， URI：
     * 
     * ws://127.0.0.1:7777/ws/?method=openTunnel&clientConnectionId=AMku9EFz2gxeL2gedGOC&id=bvDOe8XbTM2pQWjF4cfw
     * </pre>
     */
    public static final String OPEN_TUNNEL = "openTunnel";
    
    /**
     * <pre>
     * tunnel server tunnel client http， http://localhost:3658/jad-output/xxx.html
     * </pre>
     */
    public static final String HTTP_PROXY = "httpProxy";

}
