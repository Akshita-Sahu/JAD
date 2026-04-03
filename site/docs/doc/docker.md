# Docker

##  Docker  JDK

， docker  jad ， JDK ， JRE 。 JRE， JAVA ，JAD 。 Docker  JDK 。

###  JDK 

- https://hub.docker.com/_/openjdk/

：

```
FROM openjdk:8-jdk
```

：

```
FROM openjdk:8-jdk-alpine
```

### 

：

```bash
# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME
```

：

```bash
RUN yum install -y \
   java-1.8.0-openjdk \
   java-1.8.0-openjdk-devel

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk/
RUN export JAVA_HOME
```

##  Docker 

1. `math-game` docker container（）

   ```sh
   $ docker stop math-game || true && docker rm math-game || true
   ```

1. `math-game`

   ```sh
   $ docker run --name math-game -it hengyunabc/jad:latest /bin/sh -c "java -jar /opt/jad/math-game.jar"
   ```

1. `jad-boot`

   ```sh
   $ docker exec -it math-game /bin/sh -c "java -jar /opt/jad/jad-boot.jar"
   * [1]: 9 jar

   [INFO] jad home: /opt/jad
   [INFO] Try to attach process 9
   [INFO] Attach process 9 success.
   [INFO] jad-client connect 127.0.0.1 3658
   ,---.  ,------. ,--------.,--.  ,--.  ,---.   ,---.
   /  O  \ |  .--. ''--.  .--'|  '--'  | /  O  \ '   .-'
   |  .-.  ||  '--'.'   |  |   |  .--.  ||  .-.  |`.  `-.
   |  | |  ||  |\  \    |  |   |  |  |  ||  | |  |.-'    |
   `--' `--'`--' '--'   `--'   `--'  `--'`--' `--'`-----'


   wiki: https://github.com/Akshita-Sahu/JAD/doc
   version: 3.0.5
   pid: 9
   time: 2018-12-18 11:30:36
   ```

##  Docker  Java 

```sh
docker exec -it  ${containerId} /bin/bash -c "wget https://github.com/Akshita-Sahu/JAD/jad-boot.jar && java -jar jad-boot.jar"
```

##  k8s  Java 

```sh
kubectl exec -it ${pod} --container ${containerId} -- /bin/bash -c "wget https://github.com/Akshita-Sahu/JAD/jad-boot.jar && java -jar jad-boot.jar"
```

##  JAD 

 JAD  Docker 。

```
FROM openjdk:8-jdk-alpine

# copy jad
COPY --from=hengyunabc/jad:latest /opt/jad /opt/jad
```

， tags：

[https://hub.docker.com/r/hengyunabc/jad/tags](https://hub.docker.com/r/hengyunabc/jad/tags)
