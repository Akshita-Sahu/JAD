# Contributing to JAD (Java Diagnostic Tool) by Akshita Sahu



## Issue

Welcome to use [issue tracker](https://github.com/akshita-sahu/jad/issues) to give us :bowtie::

* feedbacks - what you would like to have;
* usage tips - what usages you have found splendid;
* experiences - how you use JAD to do **effective** troubleshooting;

## Documentation

* Under `site/docs/`.

## Online Tutorials

Please refer to [README.MD at killercoda branch](https://github.com/akshita-sahu/jad/tree/killercoda/README.md#contribution-guide)

## Developer

* JAD runtime supports JDK6+
* It is recommended to use JDK8 to compile, and you will encounter problems when using a higher version. Reference https://github.com/akshita-sahu/jad/tree/master/.github/workflows
* If you encounter jfr related problems, it is recommended to use `8u262` and later versions of openjdk8 or zulu jdk8, https://mail.openjdk.org/pipermail/jdk8u-dev/2020-July/012143.html
### Local Installation

> Note: After modifying `jad-vmtool` related codes, the packaging results need to be manually copied to the `lib/` path of this repo, and will not be copied automatically.

Recommend to use [`jad-package.sh`](jad-package.sh) to package, which will auto-install the latest JAD to local `~/.jad` and when debugging, JAD will auto-load the latest version.

Tip: for faster local iteration, you can use `./jad-package.sh --fast` (skip `clean` and skip documentation front-end build in `site` module). If you need to rebuild docs, run without `--fast` or use `./mvnw clean package -DskipTests -P full`.

* To support jni, cpp compiling environment support is required
* mac needs to install xcode
* windows need to install gcc

F.Y.I
1. when using [`jad.sh`](https://github.com/akshita-sahu/jad/blob/master/bin/jad.sh) to start JAD, it will get the latest version under `~/.jad/lib`;
2. when [`jad-package.sh`](jad-package.sh) packaging, it will get the version from `pom.xml` and suffix it with the current timestamp e.g. `3.0.5.20180917161808`. 

You can also use `./mvnw clean package -DskipTests` to package and generate a `zip` under `packaging/target/` but remember when `jad.sh` starts, it load the version under `~/.jad/lib`.

### Start JAD in specified version

When there are several different version, you can use `--use-version` to specify the version of JAD to start your debug.

```bash
./jad.sh --use-version 3.0.5.20180919185025
```

Tip: you can use `--versions` to list all available versions.

```bash
./jad.sh --versions
```

### Debug

* [Debug JAD In IDEA](https://github.com/akshita-sahu/jad/issues/222)

### Packaging All

* when packaging the whole project (Packaging All), you need to execute:

    ```bash
    ./mvnw clean package -DskipTests -P full
    ```

---



## Issue

issuejad，，。

* https://github.com/akshita-sahu/jad/issues

## 

`site/docs/`，jad，PR。

## 

[killercoda ](https://github.com/akshita-sahu/jad/tree/killercoda/README_CN.md#)

## 

* JADJDK6+
* JDK8，。 https://github.com/akshita-sahu/jad/tree/master/.github/workflows
* jfr，`8u262` openjdk8 zulu jdk8， https://mail.openjdk.org/pipermail/jdk8u-dev/2020-July/012143.html
### 

> ： `jad-vmtool`， `lib/` ，。

，`jad-package.sh`，jad`~/.jad`。debug。

： `./jad-package.sh --fast`（ `clean`， `site` ）。， `--fast`， `./mvnw clean package -DskipTests -P full`。

* jni，cpp
* macxcode
* windowsgcc


`jad.sh`，`~/.jad/lib`，。`jad-package.sh`，`pom.xml`，，： `3.0.5.20180917161808`，。

 `./mvnw clean package -DskipTests`，zip `packaging/target/` 。`jad.sh``~/.jad/lib`。

### jad

，， `--use-version` ，

```bash
./jad.sh --use-version 3.0.5.20180919185025
```

`--versions`：

```bash
./jad.sh --versions
```

### Debug

* [Debug JAD In IDEA](https://github.com/akshita-sahu/jad/issues/222)

### 


* ，：

    ```
    ./mvnw clean package -DskipTests -P full
    ```

### Release Steps

release：

*  jad-vmtool ，action， lib 。 https://github.com/akshita-sahu/jad/actions/workflows/build-vmtool.yaml
* `jad.sh`，， `Bootstrap.java`，Dockerfile
* maven settings.xml
*  gpg --sign /tmp/2.txt ， gpg ，
* mvn clean deploy -DskipTests -P full -P release

*  https://central.sonatype.com/publishing/deployments ，Publish  Deployment
* ，： https://repo1.maven.org/maven2/com/akshita-sahu/jad/jad-packaging/
* maven，，

    ： https://maven.akshita-sahu.com/repository/public/com/akshita-sahu/jad/jad-packaging/3.x.x/jad-packaging-3.x.x-bin.zip
    
    ： https://maven.akshita-sahu.com/repository/public/com/akshita-sahu/jad/jad-packaging/maven-metadata.xml

* tag，push tag
*  gh-pages  jad-boot.jar/math-game.jar/jad.sh ， doc.zip，， github action ： https://github.com/akshita-sahu/jad/actions/workflows/update-doc.yaml
* docker，pushtag：https://hub.docker.com/r/hengyunabc/jad/tags?page=1&ordering=last_updated

     github action push： https://github.com/akshita-sahu/jad/actions/workflows/push-docker.yaml 

* README.md，，，wiki
* release issue，
*  https://github.com/Akshita-Sahu/JAD/api/latest_version api
* 
