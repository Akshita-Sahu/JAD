# profiler

[`profiler`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-profiler)

::: tip
[async-profiler](https://github.com/jvm-profiling-tools/async-profiler)
:::

`profiler` 。，。

`profiler`  `profiler action [actionArg]`

`profiler`  [async-profiler](https://github.com/async-profiler/async-profiler) ， README、Github Disscussions 。

## 

|     |                                                         |
| ----------: | :-------------------------------------------------------------- |
|    _action_ |                                                     |
| _actionArg_ |                                                       |
|        [i:] | （：ns）（：10'000'000， 10 ms）            |
|        [f:] |                                             |
|        [d:] |                                                   |
|        [e:] | （cpu, alloc, lock, cache-misses ）， cpu |

##  profiler

```
$ profiler start
Started [cpu] profiling
```

::: tip
， cpu ， event `cpu`。`--event`，。
:::

##  sample 

```
$ profiler getSamples
23
```

##  profiling 

```bash
$ profiler status
[cpu] profiling is running for 4 seconds
```

 profiler `event`。

##  profiler 

```
$ profiler meminfo
Call trace storage:   10244 KB
      Dictionaries:      72 KB
        Code cache:   12890 KB
------------------------------
             Total:   23206 KB
```

##  profiler

### 

， [Flame Graph](https://github.com/BrendanGregg/FlameGraph)  `html` ， `-o`  `--format` ， flat、traces、collapsed、flamegraph、tree、jfr、md。

###  LLM  Markdown 

 `--format md`（ `md=N`），JAD  async-profiler  `collapsed`  Markdown， LLM  grep。：

- Top N Hotspots（self）
- Call Tree（，）
- Function Details（ Top stacks）

```bash
$ profiler stop --format md
```

（）：

```bash
$ profiler stop --format md --file /tmp/profile.md
```

 `--file`  `.md`  `--format`， Markdown ：

```bash
$ profiler stop --file /tmp/profile.md
```

 TopN（， 10）：

```bash
$ profiler stop --format md=20
```

```bash
$ profiler stop --format flamegraph
profiler output file: /tmp/test/jad-output/20211207-111550.html
OK
```

`--file` `html`  `jfr` ，。`--file /tmp/result.html` 。

##  jad-output  profiler 

，jad  3658 ，： [http://localhost:3658/jad-output/](http://localhost:3658/jad-output/) `jad-output` profiler ：

![](/images/jad-output.jpg)

：

![](/images/jad-output-svg.jpg)

::: tip
 chrome ，。
:::

## profiler  events

， OS ， events 。 macos ：

```bash
$ profiler list
Basic events:
  cpu
  alloc
  lock
  wall
  itimer
  ctimer
```

 linux 

```bash
$ profiler list
Basic events:
  cpu
  alloc
  lock
  wall
  itimer
  ctimer
Java method calls:
  ClassName.methodName
Perf events:
  page-faults
  context-switches
  cycles
  instructions
  cache-references
  cache-misses
  branch-instructions
  branch-misses
  bus-cycles
  L1-dcache-load-misses
  LLC-load-misses
  dTLB-load-misses
  rNNN
  pmu/event-descriptor/
  mem:breakpoint
  trace:tracepoint
  kprobe:func
  uprobe:path
```

 OS /， event， [async-profiler ](https://github.com/jvm-profiling-tools/async-profiler)。

 `check` action  event ， action  start 。

`--event`， `alloc` ：

```bash
$ profiler start --event alloc
```

## 

```bash
$ profiler resume
Started [cpu] profiling
```

`start``resume`：`start`，`resume`，。

`profiler getSamples` samples 。

## Dump 

```bash
$ profiler dump
OK
```

`dump` action ， profiling 。， `start` action  profiling，5  `dump` action，2  `dump` action， 2 ， 0\~5 ， 0\~7 。

## `execute`

：

```bash
profiler execute 'start,framebuf=5000000'
```

，：

```bash
profiler execute 'stop,file=/tmp/result.html'
```

： [arguments.cpp](https://github.com/async-profiler/async-profiler/blob/v2.9/src/arguments.cpp#L52)

##  action

```bash
$ profiler actions
Supported Actions: [resume, dumpCollapsed, getSamples, start, list, version, execute, meminfo, stop, load, dumpFlat, dump, actions, dumpTraces, status, check]
```

## 

```bash
$ profiler version
Async-profiler 2.9 built on May  8 2023
Copyright 2016-2021 Andrei Pangin
```

##  Java 

 `-j`  `--jstackdepth`  Java 。 2048，。，，：

```bash
profiler start -j 256
```

##  profiling

 `-t`  `--threads`  profiling ，。

```bash
profiler start -t
```

##  include/exclude 

，， stack traces， `--include/--exclude`  stack traces，`--include`  stack traces， `--exclude`  stack traces。 `*`,`*` （）。 

```bash
profiler stop --include 'java/*' --include 'com/demo/*' --exclude '*Unsafe.park*'
```

> `--include/--exclude` ，。 `-I/-X`。
> `--include/--exclude``stop`action`-d`/`--duration``start`action，。

## 

， profiler  300 ， `-d`/`--duration`  start action ：

```bash
profiler start --duration 300
```

##  jfr 

> ，jfr  `start`。`stop`，。

```
profiler start --file /tmp/test.jfr
profiler start -o jfr
```

`file`：

- ： `--file /tmp/test-%t.jfr`
-  ID： `--file /tmp/test-%p.jfr`

 jfr 。：

- JDK Mission Control ： https://github.com/openjdk/jmc
- JProfiler ： https://github.com/akshita-sahu/jad/issues/1416

## 

 `-s`  Fully qualified name ， `demo.MathGame.main`  `MathGame.main`。 `-g` ， `demo.MathGame.main`  `demo.MathGame.main([Ljava/lang/String;)V`。， [async-profiler  README ](https://github.com/async-profiler/async-profiler#readme)  [async-profiler  Github Discussions](https://github.com/async-profiler/async-profiler/discussions) 。

，，`-s` ，`-g` ，`-a`  Java ，`-l` ，`--title` ，`--minwidth`  15% ，`--reverse` 。

```
profiler stop -s -g -a -l --title <flametitle> --minwidth 15 --reverse
```

##  unknown

- https://github.com/jvm-profiling-tools/async-profiler/discussions/409

##  locks/allocations 

 lock  alloc event  profiling ， `--lock`  `--alloc` ，：

```bash
profiler start -e lock --lock 10ms
profiler start -e alloc --alloc 2m
```

 10ms （， ns ）， 2MB 。

##  JFR 

 JFR ， `--chunksize`  `--chunktime`  JFR （ byte ， 100 MB）（ 1 ），：

```bash
profiler start -f profile.jfr --chunksize 100m --chunktime 1h
```

## 

 `--sched`  Linux ， BATCH/IDLE/OTHER。：

```bash
profiler start --sched
```

。

## 

 `--live`  JVM 。 Java 。

```bash
profiler start --live
```

##  C 

 `--cstack MODE`  native 。 fp (Frame Pointer), dwarf (DWARF unwind info), lbr (Last Branch Record,  Linux 4.1  Haswell ), and no ( native ).

，C  cpu、itimer、wall-clock、perf-events ， Java  event  alloc  lock  Java stack。

```bash
profiler --cstack fp
```

 native  Frame Pointer 。

##  Native / Profiling

 `--begin function`  `--end function` ， Native 。 JVM ， GC  Safepoint。 JVM  Native ， HotSpot JVM  SafepointSynchronize::begin  SafepointSynchronize::end。

### Time-to-Safepoint Profiling

 `--ttsp`  `--begin SafepointSynchronize::begin --end RuntimeService::record_safepoint_synchronized` 。，。，Profiler ， VM  Safepoint 。

， `--ttsp`  JFR ，`profiler`  JFR  profiler.Window 。 Time-to-Safepoint ， JVM 。



```bash
profiler start --begin SafepointSynchronize::begin --end RuntimeService::record_safepoint_synchronized
profiler start --ttsp --format jfr
```

 JFR  profiler.Window ， JDK Mission Control 。

**:**

- profiler.Window ， --begin  --end ， Safepoint 。

-  Safepoint ，profiler.Window 。

-  --ttsp ， JFR ， profiler.Window 。

##  profiler  event  JFR 

 `--jfrsync CONFIG`  Java Flight Recording， jfr  JFR event， profiler 。

CONFIG :

- ：CONFIG  profile， $JAVA_HOME/lib/jfr  profile 。
- ：CONFIG  JFR （.jfc）， jcmd JFR.start  settings 。
-  JFR ：， --jfrsync  JFR ， .jfc 。， + ， + 。

：

 profile  JFR：

```bash
profiler start -e cpu --jfrsync profile -f combined.jfr
```

 JFR ， jdk.YoungGarbageCollection  jdk.OldGarbageCollection ：

```bash
profiler start -e cpu --jfrsync +jdk.YoungGarbageCollection+jdk.OldGarbageCollection -f combined.jfr
```

****

- ， , ， + 。
-  --jfrsync  + ， .jfc 。
- ，。

## 

 `--loop TIME`  profiler 。 hh:mm:ss 、、。，。 profiling  jfr 。

>  `-f` ，。 `-f`  `%t`，。

```bash
profiler start --loop 1h -f /var/log/profile-%t.jfr
```

## `--timeout` 

```bash
profiler start --timeout 300s
```

 profiling 。 `--loop` ，，。 `start` action。 [async-profiler docs](https://github.com/async-profiler/async-profiler/blob/master/docs/ProfilerOptions.md) 。

## `--wall` 

 --wall ， CPU  Wall Clock 。

1. 。
2.  CPU  Wall Clock 。， -e cpu -i 10 --wall 200， CPU  10 ， 200 。
3.  CPU  Wall Clock ， jfr。（ STATE_RUNNABLE  STATE_SLEEPING），。

 [async-profiler Github pr#740](https://github.com/async-profiler/async-profiler/issues/740) 。

：

Linux :  Linux 。macOS  CPU  Wall clock ，。
:  Wall clock ， CPU  Wall clock ， Wall clock 。

```bash
profiler start -e cpu -i 10 --wall 100 -f out.jfr
```

## `ctimer`

`ctimer`  CPU ， `timer_create`， `perf_events`  CPU 。

，`perf_events` ， `perf_event_paranoid`  `seccomp` ，。 itimer ，。

`ctimer`  `cpu`  `itimer` ：

- ： CPU 。
- ：。
- ：。

**，`ctimer`  `Linux` ， `macOS`。**
 [async-profiler Github Issues](https://github.com/async-profiler/async-profiler/issues/855) 。

：

```bash
profiler start -e ctimer -o jfr -f ./out-test.jfr
```

## `vtable`

， CPU  `megamorphic` ， `vtable stub`  `itable stub`。`megamorphic` 。

vtable ` vtable stub`  `itable stub` ，。，。

， `-F vtable` （ `features=vtable`）。
 [async-profiler Github Issues](https://github.com/async-profiler/async-profiler/issues/736) 。

：

```bash
profiler start -F vtable
```

## `comptask` 

`profiler`  JIT  Java ， JIT  CPU 。，Java ， Java  CPU 。

`comptask`  `C1/C2` ，， Java 。

，` -F comptask` （ `features=comptask`）。
 [async-profiler Github Issues](https://github.com/async-profiler/async-profiler/issues/777) 。

：

```bash
profiler start -F comptask
```

## 

`profiler`  `POSIX` 。，`SIGPROF`  `CPU` ，`SIGVTALRM`  `Wall-Clock` 。，， `profiler` ，。

， `signal` ，。
 [async-profiler Github Issues](https://github.com/async-profiler/async-profiler/issues/759) 。



```bash
profiler start --signal <>
```

 CPU  Wall-Clock ，：

```bash
profiler start --signal <CPU>/<Wall>
```

## `--clock` 

`--clock` 。 `profiler` 。



```bash
profiler start --clock <tsc|monotonic>
```



- `tsc`： CPU （`RDTSC`）。，。
- `monotonic`：（`CLOCK_MONOTONIC`）。。
   [async-profiler Github Issues](https://github.com/async-profiler/async-profiler/issues/723) 。

 :

 `CLOCK_MONOTONIC` ：

```bash
profiler start --clock monotonic
```

**:**

-  `profiler`  `CLOCK_MONOTONIC` （ `perf`）， `--clock monotonic`。
-  `jfrsync` ， `--clock` ， JVM  `profiler` ，。

## `--norm` 

 Java 20 ， `lambda` 。， `lambda` ，， `lambda` （ `lambda$method$0`、`lambda$method$1` ）。，。

，`profiler`  `--norm` ，，，。
 [async-profiler Github Issues](https://github.com/async-profiler/async-profiler/issues/832) 。

**:**

:

```bash
profiler start --norm
```
