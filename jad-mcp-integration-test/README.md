# jad-mcp-integration-test

 JAD MCP Server ：

-  JVM（`TargetJvmApp`）。
-  `packaging/target/jad-bin/jad.sh`  attach  JVM， JVM  JAD Server（ HTTP ，telnet  0）。
-  MCP（Streamable HTTP + SSE） `tools/list`  `tools/call`， MCP tools 。

## 

：

```bash
./mvnw -pl jad-mcp-integration-test -am verify
```

：

- `-am`  `packaging` ， `packaging/target/jad-bin`  `jad.sh`  jar。
-  `bash`，Windows 。

