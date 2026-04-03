# FAQ

::: tip
， issue 。 [https://github.com/akshita-sahu/jad/issues](https://github.com/akshita-sahu/jad/issues)
:::

### ？

： `~/logs/jad/jad.log`

### telnet: connect to address 127.0.0.1: Connection refused

1.  `~/logs/jad/jad.log`
2. `jad.sh`/`jad-boot.jar` ，`port`
3. `netstat` `LISTEN 3658` ，`java`，
4. `LISTEN 3658`  `java` ，`3658`。`jad.sh`/`jad-boot.jar` 。
5. ，`telnet 127.0.0.1 3658`

`jad`java`tcp server`，`telnet`。

1. 
2. ，

JAD  `JAD server already bind.`

1. `JAD server`，。`linux`， `/proc/$pid/fd` ，`ls -alh | grep jad`，`jad` jar 。
2. ，`jad`，。

### JAD attach 

[https://github.com/akshita-sahu/jad/issues/44](https://github.com/akshita-sahu/jad/issues/44)

### target process not responding or HotSpot VM not loaded

com.sun.tools.attach.AttachNotSupportedException: Unable to open socket file: target process not responding or HotSpot VM not loaded

1.  java 。，。JVM  attach  java 。
2.  `jstack -l $pid`，，， JVM attach 。 attach  JAD 。`jmap` heapdump 。
3. [quick-start](quick-start.md) attach math-game。
4. ： [https://github.com/akshita-sahu/jad/issues/347](https://github.com/akshita-sahu/jad/issues/347)

### trace/watch  jdk ？

`java.``BootStrap ClassLoader`。。

```bash
options unsafe true
```

 [options](options.md)

::: tip
 java.lang.instrument.Instrumentation#appendToBootstrapClassLoaderSearch append `Bootstrap ClassLoader` jar  unsafe。
:::

### `json`

```bash
options json-format true
```

 [options](options.md)

### JAD  native 

。

### 

1. [`vmtool`](vmtool.md)。
2. ，[`tt`](tt.md)，。

### 

,[](advice-class.md),`params.length ==1`, `params[0] instanceof java.lang.Integer`、 `returnObj instanceof java.util.List` 。

 `-v`  [https://github.com/akshita-sahu/jad/issues/1348](https://github.com/akshita-sahu/jad/issues/1348)

[math-game](quick-start.md)

```bash
watch demo.MathGame primeFactors '{params,returnObj,throwExp}' 'params.length >0 && returnObj instanceof java.util.List' -v
```

###  watch、trace  ？

```bash
watch demo.MathGame <init> '{params,returnObj,throwExp}' -v
```

###  watch、trace ？

 JVM `OuterClass$InnerClass`。

```bash
watch OuterClass$InnerClass
```

###  watch、trace lambda ？

lambda，， JVM  lambda 。

- [https://github.com/akshita-sahu/jad/issues/1225](https://github.com/akshita-sahu/jad/issues/1225)

### /Unicode 

/Unicode `\u`：

```bash
ognl '@java.lang.System@out.println("Hello \u4e2d\u6587")'
```

### java.lang.ClassFormatError: null、skywalking jad 

`java.lang.ClassFormatError: null`, jad 。

:  skywalking V8.1.0  [ trace、watch  skywalking agent ](https://github.com/akshita-sahu/jad/issues/1141), V8.1.0 , skywalking  [skywalking compatible with other javaagent bytecode processing](https://github.com/apache/skywalking/blob/master/docs/en/FAQ/Compatible-with-other-javaagent-bytecode-processing.md#)。

#### class redefinition failed: attempted to change the schema (add/remove fields)

： [https://github.com/akshita-sahu/jad/issues/2165](https://github.com/akshita-sahu/jad/issues/2165)

### JAD 

。，: [](download.md)。

### JAD ，

1.  `jad.sh`/`jad-boot.jar`， `--use-version` 。
2. ，，`cd`jad，。

### Attach docker/k8s  pid  1 

： [https://github.com/akshita-sahu/jad/issues/362#issuecomment-448185416](https://github.com/akshita-sahu/jad/issues/362#issuecomment-448185416)

###  JAD，？

 `jad.sh/jad-boot.jar`  3.5._ ，， jad  3.4._ 。

 jad 。`stop` jad， attach。

### ognl spring bean cglib ， field  null

：

- [https://github.com/akshita-sahu/jad/issues/1802](https://github.com/akshita-sahu/jad/issues/1802)
- [https://github.com/akshita-sahu/jad/issues/1424](https://github.com/akshita-sahu/jad/issues/1424)
