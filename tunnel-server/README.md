## How it works

Tunnel server/client use websocket protocol.

For example:

1. JAD tunnel server listen at `192.168.1.10:7777`

2. JAD tunnel client register to the tunnel server

   tunnel client connect to tunnel server with URL: `ws://192.168.1.10:7777/ws?method=agentRegister`

   tunnel server response a text frame message: `response:/?method=agentRegister&id=bvDOe8XbTM2pQWjF4cfw`

   This connection is `control connection`.

3. The browser try connect to remote jad agent

   start connect to tunnel server with URL: `'ws://192.168.1.10:7777/ws?method=connectJAD&id=bvDOe8XbTM2pQWjF4cfw`

4. JAD tunnel server find the `control connection` with the id `bvDOe8XbTM2pQWjF4cfw`
   
   then send a text frame to jad tunnel client: `response:/?method=startTunnel&id=bvDOe8XbTM2pQWjF4cfw&clientConnectionId=AMku9EFz2gxeL2gedGOC`

5. JAD tunnel client open a new connection to tunnel server

   URL: `ws://192.168.1.10:7777/ws/?method=openTunnel&clientConnectionId=AMku9EFz2gxeL2gedGOC&id=bvDOe8XbTM2pQWjF4cfw`
   
   This connection is `tunnel connection`

6. JAD tunnel client start connect to local jad agent, URL: `ws://127.0.0.1:3658/ws`

   This connection is `local connection`

7. Forward websocket frame between `tunnel connection` and `local connection`.

```
+---------+     +----------------------+       +----------------------+     +--------------+
| browser <-----> jad tunnel server | <-----> jad tunnel client <--- -> jad agent |
|---------+     +----------------------+       +----------------------+     +--------------+
```
