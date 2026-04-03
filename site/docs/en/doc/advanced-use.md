# Other features

## JAD Async Jobs

If you need to investigate an issue, but you are unsure about the exact time it occurs, you can run the monitoring command in the background and save the output to a log file.

- [JAD Async Jobs](async.md)

## Log the output

All execution records are fully saved in the log file for subsequent analysis.

- [log the output](save-log.md)

## Docker

JAD configuration reference for using in Docker containers.

- [Docker](docker.md)

## Web Console

JAD supports living inside a browser. The communication between jad and browser is via websocket.

- [Web Console](web-console.md)

## JAD Tunnel

JAD Tunnel Server/Client enables remote management/connection to Java services across multiple servers.

- [JAD Tunnel](tunnel.md)

## How to use ognl

- [Basic ognl example](https://github.com/akshita-sahu/jad/issues/11)
- [Ognl special uses](https://github.com/akshita-sahu/jad/issues/71)

## IDEA Plugin

Build jad commands more efficiently in the IntelliJ IDEA compiler.

- [IDEA Plugin](idea-plugin.md)

## JAD Properties

JAD supports configuration options reference.

- [JAD Properties](jad-properties.md)

## Start as a Java Agent

- [Start as a Java Agent](agent.md)

## JAD Spring Boot Starter

Starting with the application.

- [JAD Spring Boot Starter](spring-boot-starter.md)

## HTTP API

The Http API provides structured data and supports more complex interactive functions, making it easier to integrate JAD into custom interfaces.

- [HTTP API](http-api.md)

## Batch Processing

It is convenient for running multiple commands in bulk with custom scripts. It can be used in conjunction with the `--select` parameter to specify the process name.

- [Batch Processing](batch-support.md)

## jad.sh and jad-boot tips

- Select the process to be attached via the `select` option.

Normally, `jad.sh`/`jad-boot.jar` needs to a pid, bacause the pid will change.

For example, with `math-game.jar` already started, use the `jps` command to see.

```bash
$ jps
58883 math-game.jar
58884 Jps
```

The `select` option allows you to specify a process name, which is very convenient.

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

## User data report

After the `3.1.4` version, jad support user data report.

At startup, use the `stat-url` option, such as: `./jad.sh --stat-url 'http://192.168.10.11:8080/api/stat'`

There is a sample data report in the tunnel server that users can implement on their own.

[StatController.java](https://github.com/akshita-sahu/jad/blob/master/tunnel-server/src/main/java/com/akshita-sahu/jad/tunnel/server/app/web/StatController.java)
