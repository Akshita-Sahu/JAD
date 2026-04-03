# JAD Tunnel

 JAD Tunnel Server/Client / Agent。

，，Java ， JAD ，， Java 。

， JAD Tunnel Server/Client。

:

- 1: [Web Console](web-console.md)
- 2: [JAD Spring Boot Starter](spring-boot-starter.md)

##  jad tunnel server

[https://github.com/akshita-sahu/jad/releases](https://github.com/akshita-sahu/jad/releases)

-  Maven ：[![](https://img.shields.io/maven-central/v/com.akshita.jad/jad-packaging.svg?style=flat-square "JAD")](https://github.com/Akshita-Sahu/JAD/download/jad-tunnel-server/latest_version?mirror=akshita-sahu)

-  Github Releases ： [https://github.com/akshita-sahu/jad/releases](https://github.com/akshita-sahu/jad/releases)

JAD tunnel server  spring boot fat jar ，`java -jar`：

```bash
java -jar  jad-tunnel-server.jar
```

，jad tunnel server  web `8080`，jad agent `7777`。

， [http://127.0.0.1:8080/](http://127.0.0.1:8080/) ，`agentId` jad agent 。

 Spring Boot  Endpoint，： [http://127.0.0.1:8080/actuator/jad](http://127.0.0.1:8080/actuator/jad) ，`jad`， jad tunnel server ，：

```
32851 [main] INFO  o.s.b.a.s.s.UserDetailsServiceAutoConfiguration

Using generated security password: f1dca050-3777-48f4-a577-6367e55a78a2
```

##  jad  tunnel server

 jad，`--tunnel-server`，：

```bash
jad.sh --tunnel-server 'ws://127.0.0.1:7777/ws'
```

（）：

```bash
jad.sh --tunnel-server 'ws://47.75.156.201:80/ws'
```

- ，`--agent-id` agentId。， ID。

attach ， agentId，：

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

 tunnel server，， session  agentId：

```bash
[jad@86183]$ session
 Name           Value
-----------------------------------------------------
 JAVA_PID       86183
 SESSION_ID     f7273eb5-e7b0-4a00-bc5b-3fe55d741882
 AGENT_ID       URJZ5L48RPBR2ALI5K4V
 TUNNEL_SERVER  ws://47.75.156.201:80/ws
```

， [http://47.75.156.201/jad/?port=80](http://47.75.156.201/jad/?port=80) ， `agentId`， jad 。

![](/images/jad-tunnel-server.png)

## 

::: tip
，agentId ， tunnel server ，。
:::

 jad agent  `appName`， agentId `appName`。

：`jad.sh --tunnel-server 'ws://127.0.0.1:7777/ws' --app-name demoapp` ， agentId `demoapp_URJZ5L48RPBR2ALI5K4V`。

Tunnel server `_`，`appName`，。

::: tip
， jad  `jad.properties`， spring boot `application.properties``appName`。
:::

## Tunnel Server 

::: tip
 tunnel-server `application.properties` `jad.enable-detail-pages=true`，： `java -Djad.enable-detail-pages=true -jar jad-tunnel-server.jar`

： [tunnel-server application.properties](https://github.com/akshita-sahu/jad/blob/master/tunnel-server/src/main/resources/application.properties)

**，！，，。**
:::

 tunnel-server，`jad.sh` attach，`--app-name test`：

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

 tunnel-server，：

[http://localhost:8080/apps.html](http://localhost:8080/apps.html)

![](/images/tunnel-server-apps.png)

， agent ：

[http://localhost:8080/agents.html?app=test](http://localhost:8080/agents.html?app=test)

![](/images/tunnel-server-agents.png)

## 

::: tip
** tunnel server 。**
:::

 tunnel server 

1. ， app name 。
2. ，。

## 

 tunnel server， nginx ，redis  agent 。

- nginx  sticky session， web socket  tunnel server 。`ip_hash`。

## JAD tunnel server 

```
browser <-> jad tunnel server <-> jad tunnel client <-> jad agent
```

[tunnel-server/README.md](https://github.com/akshita-sahu/jad/blob/master/tunnel-server/README.md#)
