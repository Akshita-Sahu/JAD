# watch

[`watch`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-watch)

::: tip

:::

。：``、``、``， OGNL 。

## 

watch ， 4 

|             |                                                            |
| ------------------: | :----------------------------------------------------------------- |
|     _class-pattern_ |                                                      |
|    _method-pattern_ |                                                    |
|           _express_ | ，：`{params, target, returnObj}`                  |
| _condition-express_ |                                                          |
|                 [b] | ****                                             |
|                 [e] | ****                                             |
|                 [s] | ****                                             |
|                 [f] | ****()                         |
|                 [E] | ，                               |
|                [x:] | ， 1， 4                   |
|                [c:] |  classloader hash， classloader                |
|         `[m <arg>]` |  Class ， 50。`[maxMatch <arg>]`。 |

， ognl ，`"{params,returnObj}"`， ognl ，。

， `advice` 。`Advice` 。[](advice-class.md)。

- ：[https://github.com/akshita-sahu/jad/issues/71](https://github.com/akshita-sahu/jad/issues/71)
- OGNL ：[https://commons.apache.org/dormant/commons-ognl/language-guide.html](https://commons.apache.org/dormant/commons-ognl/language-guide.html)

****：

- watch  4 ， `-b` ，`-e` ，`-s` ，`-f` 
- 4  `-b`、`-e`、`-s` ，`-f` ，，
- ````，， `-b`  `params` ，
-  `-b` ，，
-  watch ，`location`。`location`：`AtEnter`，`AtExit`，`AtExceptionExit`。， return，。

## 

###  Demo

[](quick-start.md)`math-game`。

### 、this 

::: tip
，`{params, target, returnObj}`
:::

```bash
$ watch demo.MathGame primeFactors -x 2
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 32 ms, listenerId: 5
method=demo.MathGame.primeFactors location=AtExceptionExit
ts=2021-08-31 15:22:57; [cost=0.220625ms] result=@ArrayList[
    @Object[][
        @Integer[-179173],
    ],
    @MathGame[
        random=@Random[java.util.Random@31cefde0],
        illegalArgumentCount=@Integer[44],
    ],
    null,
]
method=demo.MathGame.primeFactors location=AtExit
ts=2021-08-31 15:22:58; [cost=1.020982ms] result=@ArrayList[
    @Object[][
        @Integer[1],
    ],
    @MathGame[
        random=@Random[java.util.Random@31cefde0],
        illegalArgumentCount=@Integer[44],
    ],
    @ArrayList[
        @Integer[2],
        @Integer[2],
        @Integer[26947],
    ],
]
```

- ，，`location=AtExceptionExit`，，`returnObj` null
- `location=AtExit`，，`returnObj` ArrayList

###  Class 

```bash
$ watch demo.MathGame primeFactors -m 1
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 302 ms, listenerId: 3
method=demo.MathGame.primeFactors location=AtExceptionExit
ts=2022-12-25 19:58:41; [cost=0.222419ms] result=@ArrayList[
    @Object[][isEmpty=false;size=1],
    @MathGame[demo.MathGame@3bf400],
    null,
]
method=demo.MathGame.primeFactors location=AtExceptionExit
ts=2022-12-25 19:58:51; [cost=0.046928ms] result=@ArrayList[
    @Object[][isEmpty=false;size=1],
    @MathGame[demo.MathGame@3bf400],
    null,
]
```

###  ClassLoader 

 classloader ， `sc -d`  classloader hash， `-c`  classloader：

```bash
sc -d com.example.Foo
watch -c 3d4eac69 com.example.Foo bar '{params,returnObj}'
```

### 

```bash
$ watch demo.MathGame primeFactors "{params,returnObj}" -x 2 -b
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 50 ms.
ts=2018-12-03 19:23:23; [cost=0.0353ms] result=@ArrayList[
    @Object[][
        @Integer[-1077465243],
    ],
    null,
]
```

- ，（，）

### 

```bash
$ watch demo.MathGame primeFactors "{params,target,returnObj}" -x 2 -b -s -n 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 46 ms.
ts=2018-12-03 19:29:54; [cost=0.01696ms] result=@ArrayList[
    @Object[][
        @Integer[1],
    ],
    @MathGame[
        random=@Random[java.util.Random@522b408a],
        illegalArgumentCount=@Integer[13038],
    ],
    null,
]
ts=2018-12-03 19:29:54; [cost=4.277392ms] result=@ArrayList[
    @Object[][
        @Integer[1],
    ],
    @MathGame[
        random=@Random[java.util.Random@522b408a],
        illegalArgumentCount=@Integer[13038],
    ],
    @ArrayList[
        @Integer[2],
        @Integer[2],
        @Integer[2],
        @Integer[5],
        @Integer[5],
        @Integer[73],
        @Integer[241],
        @Integer[439],
    ],
]
```

- `-n 2`，

- ，，

- ， `-s -b` 

### `-x`，

```bash
$ watch demo.MathGame primeFactors "{params,target}" -x 3
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 58 ms.
ts=2018-12-03 19:34:19; [cost=0.587833ms] result=@ArrayList[
    @Object[][
        @Integer[1],
    ],
    @MathGame[
        random=@Random[
            serialVersionUID=@Long[3905348978240129619],
            seed=@AtomicLong[3133719055989],
            multiplier=@Long[25214903917],
            addend=@Long[11],
            mask=@Long[281474976710655],
            DOUBLE_UNIT=@Double[1.1102230246251565E-16],
            BadBound=@String[bound must be positive],
            BadRange=@String[bound must be greater than origin],
            BadSize=@String[size must be non-negative],
            seedUniquifier=@AtomicLong[-3282039941672302964],
            nextNextGaussian=@Double[0.0],
            haveNextNextGaussian=@Boolean[false],
            serialPersistentFields=@ObjectStreamField[][isEmpty=false;size=3],
            unsafe=@Unsafe[sun.misc.Unsafe@2eaa1027],
            seedOffset=@Long[24],
        ],
        illegalArgumentCount=@Integer[13159],
    ],
]
```

- `-x`，， 1。
- `-x` 4，。`ognl` field。

### 

```bash
$ watch demo.MathGame primeFactors "{params[0],target}" "params[0]<0"
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 68 ms.
ts=2018-12-03 19:36:04; [cost=0.530255ms] result=@ArrayList[
    @Integer[-18178089],
    @MathGame[demo.MathGame@41cf53f9],
]
```

- ，。

### 

```bash
$ watch demo.MathGame primeFactors "{params[0],throwExp}" -e -x 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 62 ms.
ts=2018-12-03 19:38:00; [cost=1.414993ms] result=@ArrayList[
    @Integer[-1120397038],
    java.lang.IllegalArgumentException: number is: -1120397038, need >= 2
	at demo.MathGame.primeFactors(MathGame.java:46)
	at demo.MathGame.run(MathGame.java:24)
	at demo.MathGame.main(MathGame.java:16)
,
]
```

- `-e`
- express ，`throwExp`

### 

```bash
$ watch demo.MathGame primeFactors '{params, returnObj}' '#cost>200' -x 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 66 ms.
ts=2018-12-03 19:40:28; [cost=2112.168897ms] result=@ArrayList[
    @Object[][
        @Integer[1],
    ],
    @ArrayList[
        @Integer[5],
        @Integer[428379493],
    ],
]
```

- `#cost>200`(`ms`) 200ms ， 200ms 

### 

，，`target`，

```bash
$ watch demo.MathGame primeFactors 'target'
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 52 ms.
ts=2018-12-03 19:41:52; [cost=0.477882ms] result=@MathGame[
    random=@Random[java.util.Random@522b408a],
    illegalArgumentCount=@Integer[13355],
]
```

`target.field_name`

```bash
$ watch demo.MathGame primeFactors 'target.illegalArgumentCount'
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 67 ms.
ts=2018-12-03 20:04:34; [cost=131.303498ms] result=@Integer[8]
ts=2018-12-03 20:04:35; [cost=0.961441ms] result=@Integer[8]
```

### 、

```bash
watch demo.MathGame * '{params,@demo.MathGame@random.nextInt(100)}' -v -n 1 -x 2
[jad@6527]$ watch demo.MathGame * '{params,@demo.MathGame@random.nextInt(100)}' -n 1 -x 2
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 5) cost in 34 ms, listenerId: 3
ts=2021-01-05 21:35:20; [cost=0.173966ms] result=@ArrayList[
    @Object[][
        @Integer[-138282],
    ],
    @Integer[89],
]
```

-  `Thread.currentThread().getContextClassLoader()` ,`classloader` [ognl](ognl.md)。

### 

::: tip
watch/trace/monitor/stack/tt  `--exclude-class-pattern` 
:::

 `--exclude-class-pattern` ，：

```bash
watch javax.servlet.Filter * --exclude-class-pattern com.demo.TestFilter
```

### 

 watch/trace/monitor/stack/tt 。，。

```bash
options disable-sub-class true
```

###  -v 

::: tip
watch/trace/monitor/stack/tt  `-v` 
:::

，。：

1. 
2.  false

。

 `-v`，`Condition express`，。

：

```
$ watch -v -x 2 demo.MathGame print 'params' 'params[0] > 100000'
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 29 ms, listenerId: 11
Condition express: params[0] > 100000 , result: false
Condition express: params[0] > 100000 , result: false
Condition express: params[0] > 100000 , result: true
ts=2020-12-02 22:38:56; [cost=0.060843ms] result=@Object[][
    @Integer[200033],
    @ArrayList[
        @Integer[200033],
    ],
]
Condition express: params[0] > 100000 , result: true
ts=2020-12-02 22:38:57; [cost=0.052877ms] result=@Object[][
    @Integer[123047],
    @ArrayList[
        @Integer[29],
        @Integer[4243],
    ],
]
```
