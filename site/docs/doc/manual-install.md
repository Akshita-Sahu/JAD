#  JAD

1. 

   **，**：[![](https://img.shields.io/maven-central/v/com.akshita.jad/jad-packaging.svg?style=flat-square "JAD")](https://github.com/Akshita-Sahu/JAD/download/latest_version?mirror=akshita-sahu)

2.  jad 

   ```
   unzip jad-packaging-bin.zip
   ```

3.  JAD

    JAD 

   ```
   sudo su admin
   rm -rf /home/admin/.jad/lib/*
   cd jad
   ./install-local.sh
   ```

   ::: warning
   ， Java 
   :::

4.  JAD

   ， JAD `stop`.

   ```
   ./jad.sh
   ```

##  jad.sh/jad.bat

### Linux/Unix/Mac

JAD  Linux/Unix/Mac ，，， `` ：

```bash
curl -L https://github.com/Akshita-Sahu/JAD/install.sh | sh
```

 `jad.sh` ， `$PATH` 。

 shell `./jad.sh`，。

`./jad.sh -h`。

### Windows

，：[![](https://img.shields.io/maven-central/v/com.akshita.jad/jad-packaging.svg?style=flat-square "JAD")](https://github.com/Akshita-Sahu/JAD/download/latest_version?mirror=akshita-sahu)

 bin  `jad.bat`。 pid， Java 。（ bat ）

```
jad.bat <pid>
```

 windows  Java  (--interact  UI ，)：

```
as-service.bat -port <port>
as-service.bat -pid <pid>
as-service.bat -pid <pid> --interact
```

 jad windows ：

```
as-service.bat -remove
```

## 

，。

1.  jvm  java 。

    linux/mac `ps aux | grep java`， windows 。`/opt/jdk1.8/bin/java`。

2. 

   ```bash
   /opt/jdk1.8/bin/java -Xbootclasspath/a:/opt/jdk1.8/lib/tools.jar \
    -jar /tmp/jad-packaging/jad-core.jar \
    -pid 15146 \
    -target-ip 127.0.0.1 -telnet-port 3658 -http-port 8563 \
    -core /tmp/jad-packaging/jad-core.jar \
    -agent /tmp/jad-packaging/jad/jad-agent.jar
   ```

   ：
   - `-Xbootclasspath`  tools.jar
   - `-jar /tmp/jad-packaging/jad-core.jar`  main 
   - `-pid 15146`  java  ID
   - `-target-ip 127.0.0.1`  IP
   - `-telnet-port 3658 -http-port 8563`  telnet  http 
   - `-core /tmp/jad-packaging/jad-core.jar -agent /tmp/jad-packaging/jad/jad-agent.jar`  core/agent jar 

   `jdk > 9`， 9/10/11 ，`tools.jar`，`-Xbootclasspath` 。

   `~/logs/jad/jad.log`。

3. attach ， telnet 

   ```bash
   telnet 127.0.0.1 3658
   ```
