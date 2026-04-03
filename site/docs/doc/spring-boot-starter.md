# JAD Spring Boot Starter

::: tip
jad 3.7.2 springboot 2/3
:::

：[](https://search.maven.org/search?q=jad-spring-boot-starter)

 maven ：

```xml
        <dependency>
            <groupId>com.github.akshita-sahu</groupId>
            <artifactId>jad-spring-boot-starter</artifactId>
            <version>${jad.version}</version>
        </dependency>
```

，spring  jad， attach 。

::: tip
 JAD Spring Boot Starter ：<a href="https://start.akshita-sahu.com/bootstrap.html/#!dependencies=jad" target="_blank"></a>
:::

## 

， tunnel server ：

```
jad.agent-id=hsehdfsfghhwertyfad
jad.tunnel-server=ws://47.75.156.201:7777/ws
```

：[](https://github.com/akshita-sahu/jad/blob/master/jad-spring-boot-starter/src/main/java/com/akshita-sahu/jad/spring/JADProperties.java)

::: tip
，jad-spring-boot-starter `stop`。
:::

：[JAD Properties](jad-properties.md)

##  Endpoint 

::: tip
 spring boot  endpoint：[](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints)
:::

 endpoint  8080， url ：

http://localhost:8080/actuator/jad

```
{
    "jadConfigMap": {
        "agent-id": "hsehdfsfghhwertyfad",
        "tunnel-server": "ws://47.75.156.201:7777/ws",
    }
}
```

##  spring boot 

 Spring Boot ，：

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

：

```java
        HashMap<String, String> configMap = new HashMap<String, String>();
        configMap.put("jad.appName", "demo");
        configMap.put("jad.tunnelServer", "ws://127.0.0.1:7777/ws");
        JADAgent.attach(configMap);
```

::: warning
``， spring boot `-`。spring boot ``  `-`。
:::
