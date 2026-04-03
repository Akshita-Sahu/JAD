# JAD Spring Boot Starter

::: tip
Support spring boot 2
:::

Latest Version: [View](https://search.maven.org/search?q=jad-spring-boot-starter)

Add maven dependency:

```xml
        <dependency>
            <groupId>com.github.akshita-sahu</groupId>
            <artifactId>jad-spring-boot-starter</artifactId>
            <version>${jad.version}</version>
        </dependency>
```

When the application is started, spring will start jad and attach its own process.

## Configuration properties

For example, by configuring the tunnel server for remote management.

```
jad.agent-id=hsehdfsfghhwertyfad
jad.tunnel-server=ws://47.75.156.201:7777/ws
```

All supported configuration: [Reference](https://github.com/akshita-sahu/jad/blob/master/jad-spring-boot-starter/src/main/java/com/akshita-sahu/jad/spring/JADProperties.java)

::: tip
By default, jad-spring-boot-starter will disable the `stop` command.
:::

Reference: [JAD Properties](jad-properties.md)

## View Endpoint Information

::: tip
Need to configure spring boot to expose endpoint: [Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints).
:::

Assuming the endpoint port is 8080, it can be viewed via the following url.

http://localhost:8080/actuator/jad

```js
{
    "jadConfigMap": {
        "agent-id": "hsehdfsfghhwertyfad",
        "tunnel-server": "ws://47.75.156.201:7777/ws",
    }
}
```

## Non-spring boot application usage

Non-Spring Boot applications can be used in the following ways.

```xml
        <dependency>
            <groupId>com.github.akshita-sahu</groupId>
            <artifactId>jad-agent-attach</artifactId>
            <version>${jad.version}</version>
        </dependency>
        <dependency>
            <groupId>com.github.akshita-sahu</groupId>
            <artifactId>jad-packaging</artifactId>
            <version>${jad.version}</version>
        </dependency>
```

```java
import com.akshita.jad.agent.attach.JADAgent;

public class JADAttachExample {

	public static void main(String[] args) {
		JADAgent.attach();
	}

}
```

You can also configure properties:

```java
        HashMap<String, String> configMap = new HashMap<String, String>();
        configMap.put("jad.appName", "demo");
        configMap.put("jad.tunnelServer", "ws://127.0.0.1:7777/ws");
        JADAgent.attach(configMap);
```

::: warning
Note that the configuration must be `camel case`, which is different from the `-` style of spring boot. Only the spring boot application supports both `camel case` and `-` style configuration.
:::
