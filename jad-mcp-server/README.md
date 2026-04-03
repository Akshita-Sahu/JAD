# jad-mcp-server

## 

`jad-mcp-server`  [JAD](https://github.com/akshita-sahu/jad) ， MCP（Model Context Protocol）（ 2025-03-26）。 HTTP/Netty  JSON-RPC 2.0 ， AI  jad 。

JAD MCP  26 ，：

### JVM 

• **dashboard** -  JVM/，

• **heapdump** -  JVM heap dump ， --live 

• **jvm** -  JVM 

• **mbean** -  MBean ，

• **memory** -  JVM 

• **thread** - ，

• **sysprop** - ， JVM 

• **sysenv** - 

• **vmoption** -  VM ， JVM 

• **perfcounter** -  Perf Counter ， JVM 

• **vmtool** - ， GC、、

• **getstatic** - 

• **ognl** -  OGNL ，

### Class/ClassLoader 

• **sc** -  JVM ，

• **sm** - ，

• **jad** - ， Java 

• **classloader** - ClassLoader ，、、URLs

• **mc** - ， Java 

• **redefine** - ， class  JVM 

• **retransform** - ，

• **dump** -  JVM  class 

### 

• **monitor** - 

• **stack** - ，

• **trace** - ，，

• **tt** - ，，

• **watch** - ，、，


## 

 jad.properties  mcp  path：

```JSON
# MCP (Model Context Protocol) configuration
jad.mcpEndpoint=/mcp
```

，8563， cherry-studio/cline  ai ：

 MCP ：

```JSON
{
  "mcpServers": {
    "jad-mcp": {
      "type": "streamableHttp",
      "url": "http://localhost:8563/mcp"
    }
  }
}
```


 headers， token  password：

```java
"jad-mcp-streamable-server": {
      "type": "streamableHttp",
      "url": "http://localhost:8563/mcp",
      "headers": {
        "Authorization": "Bearer password"
      }
    }
```