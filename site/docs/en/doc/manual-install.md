# Manually Install JAD

1. Download the latest version

   **Latest version, Click To Download**: [![](https://img.shields.io/maven-central/v/com.akshita.jad/jad-packaging.svg?style=flat-square "JAD")](https://github.com/Akshita-Sahu/JAD/download/latest_version)

2. Unzip zip file

   ```bash
   unzip jad-packaging-bin.zip
   ```

3. Install JAD

   It is recommended to completely remove all old versions of JAD before installation.

   ```bash
   sudo su admin
   rm -rf /home/admin/.jad/lib/* # remove all the leftover of the old outdated JAD
   cd jad
   ./install-local.sh # switch the user based on the owner of the target Java process.
   ```

4. Start JAD

   Make sure `stop` the old JAD server before start.

   ```bash
   ./jad.sh
   ```

## Startup with jad.sh/jad.bat

### Linux/Unix/Mac

You can install JAD with one single line command on Linux, Unix, and Mac. Pls. copy the following command and paste it into the command line, then press _Enter_ to run:

```bash
curl -L https://github.com/Akshita-Sahu/JAD/install.sh | sh
```

The command above will download the bootstrap script `jad.sh` to the current directory. You can move it the any other place you want, or put its location in `$PATH`.

You can enter its interactive interface by executing `jad.sh`, or execute `jad.sh -h` for more help information.

### Windows

Latest Version, Click To Download: [![](https://img.shields.io/maven-central/v/com.akshita.jad/jad-packaging.svg?style=flat-square "JAD")](https://github.com/Akshita-Sahu/JAD/download/latest_version)

Download and unzip, then find `jad.bat` from 'bin' directory. For now this script will only take one argument `pid`, which means you can only diagnose the local Java process. (Welcome any bat script expert to make it better :heart:)

```bash
jad.bat <pid>
```

If you want to diagnose Java process run as windows service, try these commands:

```bash
as-service.bat -port <port>
as-service.bat -pid <pid>
as-service.bat -pid <pid> --interact
```

Use this command to remove jad service:

```bash
as-service.bat -remove
```

## Manual command line startup

If you fail to boot JAD with the provided batch file, you could try to assemble the bootstrap command in the following way.

1. Locate java in the target JVM:
   - Linux/Unix/Mac: `ps aux | grep java`
   - Windows: open the Process Monitor to search java

2. Assemble bootstrap command:

   Let's suppose we are using `/opt/jdk1.8/bin/java`, then the command should be:

   ```bash
   /opt/jdk1.8/bin/java -Xbootclasspath/a:/opt/jdk1.8/lib/tools.jar \
       -jar /tmp/jad-packaging/jad-core.jar \
       -pid 15146 \
       -target-ip 127.0.0.1 -telnet-port 3658 -http-port 8563 \
       -core /tmp/jad-packaging/jad-core.jar \
       -agent /tmp/jad-packaging/jad/jad-agent.jar
   ```

   Note:
   - `-Xbootclasspath` adds tools.jar
   - `-jar /tmp/jad-packaging/jad-core.jar` specifies main entry
   - `-pid 15146` specifies the target java process PID
   - `-target-ip 127.0.0.1` specifies the IP
   - `-telnet-port 3658 -http-port 8563` specifies telnet and HTTP ports for remote access
   - `-core /tmp/jad-packaging/jad-core.jar -agent /tmp/jad-packaging/jad/jad-agent.jar` specifies core/agent jar package

   If you are running on JDK 1.9 or above，then it's unnecessary to add `tools.jar` in option `-Xbootclasspath`.

   You can find the logs from `~/logs/jad/jad.log`.

3. Use telnet to connect once attaching to the target JVM (in step 2) succeeds

   ```bash
   telnet 127.0.0.1 3658
   ```
