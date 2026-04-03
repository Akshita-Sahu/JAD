# jfr

[`jfr`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-jfr)

::: tip
Java Flight Recorder (JFR)  Java 。 Java  (JVM) ，，。
:::

`jfr`  JFR 。  event 。 JVM  Java 。、。， CPU 、 Java 、 ID 。

`jfr`  `jfr cmd [actionArg]`

> ： JDK8  8u262  jfr

## 

|       |                                                                                   |
| ------------: | :---------------------------------------------------------------------------------------- |
|         _cmd_ |  【start，status，dump，stop】                                      |
|   _actionArg_ |                                                                                 |
|          [n:] |                                                                                   |
|          [r:] |  id                                                                                 |
| [dumponexit:] | ， dump  .jfr ， false                                        |
|          [d:] |  JFR ，，eg: 60s, 2m, 5h, 3d. ，  |
|   [duration:] | JFR ，，，                              |
|          [s:] |  Event ， default.jfc  `$JAVA_HOME/lib/jfr/default.jfc`       |
|          [f:] |                                                                       |
|     [maxage:] | ，，，                |
|    [maxsize:] | ，， ，m  M  MB，g  G  GB。 |
|      [state:] | jfr                                                                               |

##  JFR 

```
$ jfr start
Started recording 1. No limit specified, using maxsize=250MB as default.
```

::: tip
， jfr 
:::

 jfr ，，，。

```
$ jfr start -n myRecording --duration 60s -f /tmp/myRecording.jfr
Started recording 2. The result will be written to:
/tmp/myRecording.jfr
```

##  JFR 

 JFR 

```bash
$ jfr status
Recording: recording=1 name=Recording-1 (running)
Recording: recording=2 name=myRecording duration=PT1M (closed)
```

 id 

```bash
$ jfr status -r 1
Recording: recording=1 name=Recording-1 (running)
```



```bash
$ jfr status --state closed
Recording: recording=2 name=myRecording duration=PT1M (closed)
```

## dump jfr 

`jfr dump`{{}}  JFR ， `jfr`{{}}   


```bash
$ jfr dump -r 1 -f /tmp/myRecording1.jfr
Dump recording 1, The result will be written to:
/tmp/myRecording1.jfr
```

，`jad-output`

```bash
$ jfr dump -r 1
Dump recording 1, The result will be written to:
/tmp/test/jad-output/20220819-200915.jfr
```

##  jfr 

，`jad-output`

```bash
$ jfr stop -r 1
Stop recording 1, The result will be written to:
/tmp/test/jad-output/20220819-202049.jfr
```

> 。

。

##  jad-output  JFR 

，jad  8563 ，： [http://localhost:8563/jad-output/](http://localhost:8563/jad-output/) `jad-output` JFR ：

![](/images/jad-output-recording.png)

 jfr 。：

- JDK Mission Control ： https://github.com/openjdk/jmc
