# JAD MCP Server

## 

JAD MCP Server  JAD ， [MCP（Model Context Protocol）](https://modelcontextprotocol.io/) 。 HTTP/Netty  JSON-RPC 2.0 ， AI  JAD 。

MCP（Model Context Protocol） Anthropic ， AI 。 JAD MCP Server，AI  Java ，。

### 

- **AI **： AI （Claude Desktop、Cherry Studio、Cline ）
- ****： MCP （ 2025-06-18）， Streamable Http 
- **29 **： JVM 、、
- ****： Bearer Token 

## 

JAD MCP Server  29 ，：

### JVM （13 ）

|             |                                                   |
| --------------- | --------------------------------------------------------- |
| **dashboard**   |  JVM/，       |
| **heapdump**    |  JVM heap dump ， `--live`  |
| **jvm**         |  JVM                              |
| **mbean**       |  MBean ，         |
| **memory**      |  JVM                                    |
| **thread**      | ，            |
| **sysprop**     | ， JVM              |
| **sysenv**      |                                           |
| **vmoption**    |  VM ， JVM                  |
| **perfcounter** |  JVM Perf Counter                       |
| **vmtool**      | ， GC、、         |
| **getstatic**   | ， ClassLoader  OGNL    |
| **ognl**        |  OGNL ，                  |

### Class/ClassLoader （8 ）

|             |                                                                                                |
| --------------- | ------------------------------------------------------------------------------------------------------ |
| **sc**          |  JVM ，，（、、、）          |
| **sm**          | ，，、、                       |
| **jad**         | ， JVM  class  Java                            |
| **classloader** | ClassLoader ，、、URLs，； sc  |
| **mc**          | ， `.java`  `.class`                                               |
| **redefine**    | ， JVM                                             |
| **retransform** | ，                                                 |
| **dump**        |  JVM  class ，                           |

### （6 ）

|          |                                                                          |
| ------------ | -------------------------------------------------------------------------------- |
| **monitor**  | ，、、 RT      |
| **stack**    | ，                             |
| **trace**    | ，，                       |
| **tt**       | ，， |
| **watch**    | ，、，       |
| **profiler** | Async Profiler ， CPU/alloc/lock 、JFR   |

### JAD （2 ）

|          |                                                                                             |
| ------------ | --------------------------------------------------------------------------------------------------- |
| **viewfile** | （）， cursor/offset ， |
| **options**  |  JAD                                                                       |

## 

### 1.  MCP 

 `jad.properties`  MCP ：

```properties
# MCP (Model Context Protocol) configuration
jad.mcpEndpoint=/mcp
```

### 2. 

 JAD  JAD  Java 。，MCP  HTTP  8563 。

 MCP ：

```bash
curl http://localhost:8563/mcp
```

 MCP ，。

### 3.  AI 

 AI ：

#### Cherry Studio / Cline

 MCP ：

```json
{
  "mcpServers": {
    "jad-mcp": {
      "type": "streamableHttp",
      "url": "http://localhost:8563/mcp"
    }
  }
}
```

## 

### JAD 

|                |                                                          |              |
| -------------------- | ------------------------------------------------------------ | ------------------ |
| `jad.mcpEndpoint` | MCP                                            | （） |
| `jad.mcpProtocol` | ：`STREAMABLE`（） `STATELESS`（） | `STREAMABLE`       |
| `jad.httpPort`    | HTTP                                                 | 8563               |
| `jad.password`    | （）                                   |                  |

### 

JAD MCP Server ：

- **STREAMABLE **（）：， HTTP/SSE ，（ watch、trace、monitor ）、。。
- **STATELESS **：，，。

 `jad.properties` ：

```properties
jad.mcpEndpoint=/mcp
# ， STREAMABLE
jad.mcpProtocol=STREAMABLE
```

### 

 `jad.password` ，MCP Server 。 AI ， Bearer Token 。

（`jad.properties`）：

```properties
jad.password=your-secure-password
```

AI ：

```json
{
  "mcpServers": {
    "jad-mcp": {
      "type": "streamableHttp",
      "url": "http://localhost:8563/mcp",
      "headers": {
        "Authorization": "Bearer your-secure-password"
      }
    }
  }
}
```

::: warning
****：Authorization header  token  `jad.password` 。
:::

### viewfile 

`viewfile` ：

-  `jad-output` （）
-  Home  `~/logs/` （）

：

```bash
export JAD_MCP_VIEWFILE_ALLOWED_DIRS=/path/to/dir1,/path/to/dir2
```

## 

::: tip
JAD MCP Server ，！
:::

- ****：[GitHub Issues](https://github.com/akshita-sahu/jad/issues)
- ****：[GitHub Discussions](https://github.com/akshita-sahu/jad/discussions)
- ****：[](https://github.com/akshita-sahu/jad/blob/master/CONTRIBUTING.md)
