# JAD Properties

The `jad.properties` file is in the jad directory.

- If it is automatically downloaded jad, the directory is under `~/.jad/lib/3.x.x/jad/`
- If it is a downloaded complete package, under the decompression directory of jad

## Supported configuration items

::: warning
Note that the configuration must be `camel case`, which is different from the `-` style of spring boot. Only the spring boot application supports both `camel case` and `-` style configuration.
:::

```
#jad.config.overrideAll=true
jad.telnetPort=3658
jad.httpPort=8563
jad.ip=127.0.0.1

# seconds
jad.sessionTimeout=1800

#jad.appName=demoapp
#jad.tunnelServer=ws://127.0.0.1:7777/ws
#jad.agentId=mmmmmmyiddddd
```

- If the configuration of `jad.telnetPort` is -1, the telnet port will not be listened. `jad.httpPort` is similar.
- If you configure `jad.telnetPort` to 0, then random listen telnet port, you can find the random port log in `~/logs/jad/jad.log`. `jad.httpPort` is similar.

::: tip
If you want to prevent multiple jad port conflicts on a machine. It can be configured as a random port, or configured as -1, and use jad through the tunnel server.
:::

### disable specify commands

::: tip
since 3.5.2
:::

Such as configuration:

```
jad.disabledCommands=stop,dump
```

It can also be configured on the command line: `--disabled-commands stop,dump`.

::: tip
By default, jad-spring-boot-starter will disable the `stop` command.
:::

## Configured order

The order of configuration is: command line parameters > System Env > System Properties > jad.properties.

such as:

- `./jad.sh --telnet-port 9999` command line configuration will overwrite the default value `jad.telnetPort=3658` in `jad.properties`.
- If the application itself sets system properties `jad.telnetPort=8888`, it will override the default value `jad.telnetPort=3658` in `jad.properties`.

If you want `jad.properties` to have the highest order, you can configure `jad.config.overrideAll=true`.
