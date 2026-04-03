# 

## JAD 

，，，。

- [JAD ](async.md)

## 

，。

- [](save-log.md)

## Docker

JAD  docker 。

- [Docker](docker.md)

## Web Console

 websocket  JAD。

- [Web Console](web-console.md)

## JAD Tunnel

 JAD Tunnel Server/Client /Java。

- [JAD Tunnel](tunnel.md)

## ognl 

- [ognl ](https://github.com/akshita-sahu/jad/issues/11)
- [ ognl ](https://github.com/akshita-sahu/jad/issues/71)

## IDEA Plugin

IntelliJ IDEA  arhtas 。

- [IDEA Plugin](idea-plugin.md)

## JAD Properties

JAD 。

- [JAD Properties](jad-properties.md)

##  java agent 

- [ java agent ](agent.md)

## JAD Spring Boot Starter

。

- [JAD Spring Boot Starter](spring-boot-starter.md)

## HTTP API

Http API ，， jad。

- [HTTP API](http-api.md)

## 

， `--select` 。

- [](batch-support.md)

## jad.sh  jad-boot 

- `select` attach 。

，`jad.sh`/`jad-boot.jar`， PID。，， PID 。

，`math-game.jar`，`jps`：

```bash
$ jps
58883 math-game.jar
58884 Jps
```

`select`，。

```bash
$ ./jad.sh --select math-game
JAD script version: 3.3.6
[INFO] JAVA_HOME: /tmp/java/8.0.222-zulu
JAD home: /Users/admin/.jad/lib/3.3.6/jad
Calculating attach execution time...
Attaching to 59161 using version /Users/admin/.jad/lib/3.3.6/jad...

real	0m0.572s
user	0m0.281s
sys	0m0.039s
Attach success.
telnet connecting to jad server... current timestamp is 1594280799
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
  ,---.  ,------. ,--------.,--.  ,--.  ,---.   ,---.
 /  O  \ |  .--. ''--.  .--'|  '--'  | /  O  \ '   .-'
|  .-.  ||  '--'.'   |  |   |  .--.  ||  .-.  |`.  `-.
|  | |  ||  |\  \    |  |   |  |  |  ||  | |  |.-'    |
`--' `--'`--' '--'   `--'   `--'  `--'`--' `--'`-----'


wiki      https://github.com/Akshita-Sahu/JAD/doc
tutorials https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html
version   3.3.6
pid       58883
```

## 

`3.1.4`，，。

，`stat-url`，，： `./jad.sh --stat-url 'http://192.168.10.11:8080/api/stat'`

 tunnel server ，。

[StatController.java](https://github.com/akshita-sahu/jad/blob/master/tunnel-server/src/main/java/com/akshita-sahu/jad/tunnel/server/app/web/StatController.java)
