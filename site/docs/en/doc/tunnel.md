# JAD Tunnel

Manage/connect multiple Agents remotely via JAD Tunnel Server/Client.

For example, in streaming computing, Java processes can be started on different machines, and it can be difficult to use JAD to diagnose them, because the user usually does not have access to the machine.

In this case, JAD Tunnel Server/Client can be used.

Reference:

- 1: [Web Console](web-console.md)
- 2: [JAD Spring Boot Starter](spring-boot-starter.md)

## Download and deploy jad tunnel server

[https://github.com/akshita-sahu/jad/releases](https://github.com/akshita-sahu/jad/releases)

JAD tunnel server is a spring boot fat jar application, start with the `java -jar` command:

```bash
java -jar  jad-tunnel-server.jar
```

By default, the web port of the jad tunnel server is `8080`, and the port connected by the jad agent is `7777`.

Once started, you can go to [http://127.0.0.1:8080/](http://127.0.0.1:8080/) and connect to the registered jad agent via `agentId`.

Through Spring Boot's Endpoint, you can view the specific connection information: [http://127.0.0.1:8080/actuator/jad](http://127.0.0.1:8080/actuator/jad), the login user name is `jad`, and the password can be found in the log of jad tunnel server, for example:

```
32851 [main] INFO o.s.b.a.s.s.UserDetailsServiceAutoConfiguration

Using generated security password: f1dca050-3777-48f4-a577-6367e55a78a2
```

## Connecting to the tunnel server when starting jad

When starting jad, you can use the `--tunnel-server` parameter, for example:

```bash
jad.sh --tunnel-server 'ws://127.0.0.1:7777/ws'
```

You can also use the following test address (not guaranteed to be available all the time):

```bash
jad.sh --tunnel-server 'ws://47.75.156.201:80/ws'
```

- You can specify the agentId by the `--agent-id` parameter. By default, a random ID is generated.

After JAD attach succeeds, the agentId will be printed, such as:

```bash
  ,---.  ,------. ,--------.,--.  ,--.  ,---.   ,---.
 /  O  \ |  .--. ''--.  .--'|  '--'  | /  O  \ '   .-'
|  .-.  ||  '--'.'   |  |   |  .--.  ||  .-.  |`.  `-.
|  | |  ||  |\  \    |  |   |  |  |  ||  | |  |.-'    |
`--' `--'`--' '--'   `--'   `--'  `--'`--' `--'`-----'


wiki      https://github.com/Akshita-Sahu/JAD/doc
tutorials https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html
version   3.1.2
pid       86183
time      2019-08-30 15:40:53
id        URJZ5L48RPBR2ALI5K4V
```

If the connection is not connected to the tunnel server at startup, you can also obtain the agentId through the `session` command after reconnection succeeds:

```bash
[jad@86183]$ session
 Name           Value
-----------------------------------------------------
 JAVA_PID       86183
 SESSION_ID     f7273eb5-e7b0-4a00-bc5b-3fe55d741882
 AGENT_ID       URJZ5L48RPBR2ALI5K4V
 TUNNEL_SERVER  ws://47.75.156.201:80/ws
```

For the above example, go to [http://47.75.156.201/jad/?port=80](http://47.75.156.201/jad/?port=80) in the browser and input the `agentId` to connect to jad on remote machine.

![](/images/jad-tunnel-server.png)

## Best practices

::: warning
Note that the agentId must be unique, otherwise it will conflict on the tunnel server and not work properly.
:::

If the jad agent is configured with `appName`, the generated agentId will be prefixed with `appName`.

For example, if you add the startup parameter `jad.sh --tunnel-server 'ws://127.0.0.1:7777/ws' --app-name demoapp`, the generated agentId might be `demoapp_URJZ5L48RPBR2ALI5K4V`.

Tunnel server will use `_` as a delimiter to extract `appName`, which is convenient to manage by application.

::: tip
Alternatively, you can configure `appName` in `jad.properties` in the unzipped jad directory, or in `application.properties` of the spring boot application.
:::

## Tunnel Server Management Page

::: tip
You need to configure `jad.enable-detail-pages=true` in `application.properties` of tunnel-server, or you can specify it with command line parameters: `java -Djad.enable-detail-pages=true -jar jad-tunnel-server.jar`

Supported configuration: [tunnel-server application.properties](https://github.com/akshita-sahu/jad/blob/master/tunnel-server/src/main/resources/application.properties)

**Attention, opening the management page is risky! The management page is not authenticated, so you must add security measures yourself, and do not open it to the public network. **
:::

Start the tunnel-server locally, then use `jad.sh` attach, and specify the application name `--app-name test`:

```
$ jad.sh --tunnel-server 'ws://127.0.0.1:7777/ws' --app-name test
telnet connecting to jad server... current timestamp is 1627539688
Trying 127.0.0.1...
Connected to 127.0.0.1.
Escape character is '^]'.
  ,---.  ,------. ,--------.,--.  ,--.  ,---.   ,---.
 /  O  \ |  .--. ''--.  .--'|  '--'  | /  O  \ '   .-'
|  .-.  ||  '--'.'   |  |   |  .--.  ||  .-.  |`.  `-.
|  | |  ||  |\  \    |  |   |  |  |  ||  | |  |.-'    |
`--' `--'`--' '--'   `--'   `--'  `--'`--' `--'`-----'


wiki       https://github.com/Akshita-Sahu/JAD/doc
tutorials  https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html
version    3.5.3
main_class demo.MathGame
pid        65825
time       2021-07-29 14:21:29
id         test_PE3LZO9NA9ENJYTPGL9L
```

Then visit tunnel-server, you can see a list of all connected applications:

[http://localhost:8080/apps.html](http://localhost:8080/apps.html)

![](/images/tunnel-server-apps.png)

Then open the details, you can see a list of all connected agents:

[http://localhost:8080/agents.html?app=test](http://localhost:8080/agents.html?app=test)

![](/images/tunnel-server-agents.png)

## Security and Privilege Management

::: tip
**It is strongly recommended not to expose the tunnel server directly to the public network.**
:::

Currently tunnel server does not have special permission management

1. Users need to develop by themselves and authenticate the app name.
2. If the management page is opened, security measures need to be added.

## Cluster Management

If you want to deploy multiple tunnel servers, you can use nginx for forwarding and redis to store agent information.

Nginx needs to configure sticky session to ensure that the user's web socket is connected to the same back-end tunnel server. The simple configuration method is to use `ip_hash`.

## How jad tunnel server works

```
browser <-> jad tunnel server <-> jad tunnel client <-> jad agent
```

[tunnel-server/README.md](https://github.com/akshita-sahu/jad/blob/master/tunnel-server/README.md#)
