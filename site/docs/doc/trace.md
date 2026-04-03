# trace

[`trace`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-trace)

::: tip
，
:::

`trace`  `class-pattern`／`method-pattern` ，。

## 

|             |                                                            |
| ------------------: | :----------------------------------------------------------------- |
|     _class-pattern_ |                                                      |
|    _method-pattern_ |                                                    |
| _condition-express_ |                                                          |
|                 [E] | ，                               |
|              `[n:]` | ， 100。                                       |
|             `#cost` |                                                        |
|              `[c:]` |  classloader hash， classloader                |
|         `[m <arg>]` |  Class ， 50。`[maxMatch <arg>]`。 |

``，`` ognl ，`"params[0]<0"`， ognl ，。

[](advice-class.md)。

- ：[https://github.com/akshita-sahu/jad/issues/71](https://github.com/akshita-sahu/jad/issues/71)
- OGNL ：[https://commons.apache.org/dormant/commons-ognl/language-guide.html](https://commons.apache.org/dormant/commons-ognl/language-guide.html)

 rt  trace ， JAD ，`trace *StringUtils isBlank '#cost>100'` 100ms ， trace 。

::: tip
watch/stack/trace `#cost`
:::

## 

- `trace`  RT ，。

  ：[Trace ](https://github.com/akshita-sahu/jad/issues/597)

- 3.3.0 ， Trace ，，。

-  `trace java.lang.Thread getName`， issue: [#1610](https://github.com/akshita-sahu/jad/issues/1610) ，，，

## 

###  Demo

[](quick-start.md)`math-game`。

### trace 

```bash
$ trace demo.MathGame run
Press Q or Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 28 ms.
`---ts=2019-12-04 00:45:08;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[0.617465ms] demo.MathGame:run()
        `---[0.078946ms] demo.MathGame:primeFactors() #24 [throws Exception]

`---ts=2019-12-04 00:45:09;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[1.276874ms] demo.MathGame:run()
        `---[0.03752ms] demo.MathGame:primeFactors() #24 [throws Exception]
```

::: tip
 `#24`， run ，`24``primeFactors()`。
:::

###  Class 

```bash
$ trace demo.MathGame run -m 1
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 412 ms, listenerId: 4
`---ts=2022-12-25 21:00:00;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@b4aac2
    `---[0.762093ms] demo.MathGame:run()
        `---[30.21% 0.230241ms] demo.MathGame:primeFactors() #46 [throws Exception]

`---ts=2022-12-25 21:00:10;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@b4aac2
    `---[0.315298ms] demo.MathGame:run()
        `---[13.95% 0.043995ms] demo.MathGame:primeFactors() #46 [throws Exception]
```

###  ClassLoader 

 classloader ， `sc -d`  classloader hash， `-c`  classloader：

```bash
sc -d com.example.Foo
trace -c 3d4eac69 com.example.Foo bar
```

### trace 

，`-n`。，。

```bash
$ trace demo.MathGame run -n 1
Press Q or Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 20 ms.
`---ts=2019-12-04 00:45:53;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[0.549379ms] demo.MathGame:run()
        +---[0.059839ms] demo.MathGame:primeFactors() #24
        `---[0.232887ms] demo.MathGame:print() #25

Command execution times exceed limit: 1, so command will exit. You can set it with -n option.
```

###  jdk 

- `--skipJDKMethod <value> ` skip jdk method trace, default value true.

，trace  jdk ， trace jdk ，`--skipJDKMethod false`。

```bash
$ trace --skipJDKMethod false demo.MathGame run
Press Q or Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 60 ms.
`---ts=2019-12-04 00:44:41;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[1.357742ms] demo.MathGame:run()
        +---[0.028624ms] java.util.Random:nextInt() #23
        +---[0.045534ms] demo.MathGame:primeFactors() #24 [throws Exception]
        +---[0.005372ms] java.lang.StringBuilder:<init>() #28
        +---[0.012257ms] java.lang.Integer:valueOf() #28
        +---[0.234537ms] java.lang.String:format() #28
        +---[min=0.004539ms,max=0.005778ms,total=0.010317ms,count=2] java.lang.StringBuilder:append() #28
        +---[0.013777ms] java.lang.Exception:getMessage() #28
        +---[0.004935ms] java.lang.StringBuilder:toString() #28
        `---[0.06941ms] java.io.PrintStream:println() #28

`---ts=2019-12-04 00:44:42;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[3.030432ms] demo.MathGame:run()
        +---[0.010473ms] java.util.Random:nextInt() #23
        +---[0.023715ms] demo.MathGame:primeFactors() #24 [throws Exception]
        +---[0.005198ms] java.lang.StringBuilder:<init>() #28
        +---[0.006405ms] java.lang.Integer:valueOf() #28
        +---[0.178583ms] java.lang.String:format() #28
        +---[min=0.011636ms,max=0.838077ms,total=0.849713ms,count=2] java.lang.StringBuilder:append() #28
        +---[0.008747ms] java.lang.Exception:getMessage() #28
        +---[0.019768ms] java.lang.StringBuilder:toString() #28
        `---[0.076457ms] java.io.PrintStream:println() #28
```

### 

```bash
$ trace demo.MathGame run '#cost > 10'
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 41 ms.
`---ts=2018-12-04 01:12:02;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[12.033735ms] demo.MathGame:run()
        +---[0.006783ms] java.util.Random:nextInt()
        +---[11.852594ms] demo.MathGame:primeFactors()
        `---[0.05447ms] demo.MathGame:print()
```

::: tip
 10ms ，，
:::

- ，， JProfiler ，。 ，`trace` ， JProfiler 。，、，。。
- [12.033735ms] ，`12.033735` ：，
- [0,0,0ms,11]xxx:yyy() [throws Exception]，，`0,0,0ms,11` ，`min,max,total,count`；`throws Exception` 
- ，， JAD 

### trace 

trace  trace ， trace 。 trace ， trace  trace 。

， trace 。

```bash
trace -E com.test.ClassA|org.test.ClassB method1|method2|method3
```

### 

 `--exclude-class-pattern` ，：

```bash
trace javax.servlet.Filter * --exclude-class-pattern com.demo.TestFilter
```

##  trace

::: tip
3.3.0 。
:::

 1，trace  demo `run`， `listenerId: 1`：

```bash
[jad@59161]$ trace demo.MathGame run
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 112 ms, listenerId: 1
`---ts=2020-07-09 16:48:11;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[1.389634ms] demo.MathGame:run()
        `---[0.123934ms] demo.MathGame:primeFactors() #24 [throws Exception]

`---ts=2020-07-09 16:48:12;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[3.716391ms] demo.MathGame:run()
        +---[3.182813ms] demo.MathGame:primeFactors() #24
        `---[0.167786ms] demo.MathGame:print() #25
```

`primeFactors`， 2，`telnet localhost 3658` jad， trace `primeFactors`，`listenerId`。

```bash
[jad@59161]$ trace demo.MathGame primeFactors --listenerId 1
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 34 ms, listenerId: 1
```

 2 ，：`Affect(class count: 1 , method count: 1)`，。

 1， trace ，`primeFactors`：

```bash
`---ts=2020-07-09 16:49:29;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[0.492551ms] demo.MathGame:run()
        `---[0.113929ms] demo.MathGame:primeFactors() #24 [throws Exception]
            `---[0.061462ms] demo.MathGame:primeFactors()
                `---[0.001018ms] throw:java.lang.IllegalArgumentException() #46

`---ts=2020-07-09 16:49:30;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    `---[0.409446ms] demo.MathGame:run()
        +---[0.232606ms] demo.MathGame:primeFactors() #24
        |   `---[0.1294ms] demo.MathGame:primeFactors()
        `---[0.084025ms] demo.MathGame:print() #25
```

`listenerId` trace，。 `watch`/`tt`/`monitor`。

## trace 

：`0.705196 > (0.152743 + 0.145825)`

```bash
$ trace demo.MathGame run -n 1
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 66 ms, listenerId: 1
`---ts=2021-02-08 11:27:36;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@232204a1
    `---[0.705196ms] demo.MathGame:run()
        +---[0.152743ms] demo.MathGame:primeFactors() #24
        `---[0.145825ms] demo.MathGame:print() #25
```

？

1.  trace 。`java.*` 。`--skipJDKMethod false`。

   ```bash
   $ trace demo.MathGame run --skipJDKMethod false
   Press Q or Ctrl+C to abort.
   Affect(class count: 1 , method count: 1) cost in 35 ms, listenerId: 2
   `---ts=2021-02-08 11:27:48;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@232204a1
       `---[0.810591ms] demo.MathGame:run()
           +---[0.034568ms] java.util.Random:nextInt() #23
           +---[0.119367ms] demo.MathGame:primeFactors() #24 [throws Exception]
           +---[0.017407ms] java.lang.StringBuilder:<init>() #28
           +---[0.127922ms] java.lang.String:format() #57
           +---[min=0.01419ms,max=0.020221ms,total=0.034411ms,count=2] java.lang.StringBuilder:append() #57
           +---[0.021911ms] java.lang.Exception:getMessage() #57
           +---[0.015643ms] java.lang.StringBuilder:toString() #57
           `---[0.086622ms] java.io.PrintStream:println() #57
   ```

2. 。 `i++`, `getfield`。

3. ，JVM ， GC，。

###  -v 

::: tip
watch/trace/monitor/stack/tt  `-v` 
:::

，。：

1. 
2.  false

。

 `-v`，`Condition express`，。
