# JAD Properties

`jad.properties` jad 。

-  jad，`~/.jad/lib/3.x.x/jad/`
- ， jad 

## 

::: warning
``， spring boot `-`。spring boot ``  `-`。
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

-  `jad.telnetPort` -1 ， listen telnet 。`jad.httpPort`。
-  `jad.telnetPort` 0 ， telnet ，`~/logs/jad/jad.log`。`jad.httpPort`。

:::tip
 jad 。， -1， tunnel server  jad。
:::

### 

::: tip
since 3.5.2
:::

：

```
jad.disabledCommands=stop,dump
```

： `--disabled-commands stop,dump` 。

::: tip
，jad-spring-boot-starter `stop`。
:::

## 

： > System Env > System Properties > jad.properties 。

：

- `./jad.sh --telnet-port 9999` `jad.properties``jad.telnetPort=3658`。
-  system properties `jad.telnetPort=8888`，`jad.properties``jad.telnetPort=3658`。

 `jad.properties`， `jad.config.overrideAll=true` 。
