#  JAD

## 

```bash
./jad.sh
```

```bash
➜  bin git:(develop) ✗ ./jad.sh
Found existing java process, please choose one and input the serial number of the process, eg: 1 . Then hit ENTER.
  [1]: 3088 org.jetbrains.idea.maven.server.RemoteMavenServer
* [2]: 12872 org.apache.catalina.startup.Bootstrap
  [3]: 2455
Attaching to 12872...
  ,---.  ,------. ,--------.,--.  ,--.  ,---.   ,---.
 /  O  \ |  .--. ''--.  .--'|  '--'  | /  O  \ '   .-'
|  .-.  ||  '--'.'   |  |   |  .--.  ||  .-.  |`.  `-.
|  | |  ||  |\  \    |  |   |  |  |  ||  | |  |.-'    |
`--' `--'`--' '--'   `--'   `--'  `--'`--' `--'`-----'
$
```

## 

：

```bash
./jad.sh <PID>[@IP:PORT]
```

### 

- PID： Java  ID（ Java ）
- IP：JAD Server ， `127.00.1`。JAD ，
- PORT： JAD Server ， 3658

### 

-  IP  PORT， 127.0.0.1  3658

  > ./jad.sh 12345

  ：

  > ./jad.sh 12356@127.0.0.1:3658

### 

 JAD Server ， telnet ，：

```bash
telnet 192.168.1.119 3658
```

### sudo 

 JVM ， sudo-list 。 jad.sh ， -H 

```bash
sudo -u admin -H ./jad.sh 12345
```

### Windows 

`jad.bat`：pid

```bash
jad.bat <pid>
```
