# dashboard

[`dashboard`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-dashboard)

::: tip
， ctrl+c 。
:::

 Ali-tomcat ， tomcat ， HTTP  qps, rt, , 。

## 

|  |                                  |
| -------: | :--------------------------------------- |
|     [i:] |  (ms)， 5000ms |
|     [n:] |                        |

## 

```
$ dashboard
ID   NAME                           GROUP           PRIORITY   STATE     %CPU      DELTA_TIME TIME      INTERRUPTE DAEMON
-1   C2 CompilerThread0             -               -1         -         1.55      0.077      0:8.684   false      true
53   Timer-for-jad-dashboard-07b system          5          RUNNABLE  0.08      0.004      0:0.004   false      true
22   scheduling-1                   main            5          TIMED_WAI 0.06      0.003      0:0.287   false      false
-1   C1 CompilerThread0             -               -1         -         0.06      0.003      0:2.171   false      true
-1   VM Periodic Task Thread        -               -1         -         0.03      0.001      0:0.092   false      true
49   jad-NettyHttpTelnetBootstra system          5          RUNNABLE  0.02      0.001      0:0.156   false      true
16   Catalina-utility-1             main            1          TIMED_WAI 0.0       0.000      0:0.029   false      false
-1   G1 Young RemSet Sampling       -               -1         -         0.0       0.000      0:0.019   false      true
17   Catalina-utility-2             main            1          WAITING   0.0       0.000      0:0.025   false      false
34   http-nio-8080-ClientPoller     main            5          RUNNABLE  0.0       0.000      0:0.016   false      true
23   http-nio-8080-BlockPoller      main            5          RUNNABLE  0.0       0.000      0:0.011   false      true
-1   VM Thread                      -               -1         -         0.0       0.000      0:0.032   false      true
-1   Service Thread                 -               -1         -         0.0       0.000      0:0.006   false      true
-1   GC Thread#5                    -               -1         -         0.0       0.000      0:0.043   false      true
Memory                     used     total    max      usage    GC
heap                       36M      70M      4096M    0.90%    gc.g1_young_generation.count   12
g1_eden_space              6M       18M      -1       33.33%                                  86
g1_old_gen                 30M      50M      4096M    0.74%    gc.g1_old_generation.count     0
g1_survivor_space          491K     2048K    -1       24.01%   gc.g1_old_generation.time(ms)  0
nonheap                    66M      69M      -1       96.56%
codeheap_'non-nmethods'    1M       2M       5M       22.39%
metaspace                  46M      47M      -1       98.01%
Runtime
os.name                                                        Mac OS X
os.version                                                     10.15.4
java.version                                                   15
java.home                                                      /Library/Java/JavaVirtualMachines/jdk-15.jdk/Contents/Home
systemload.average                                             10.68
processors                                                     8
uptime                                                         272s
```

## 

- ID: Java  ID， ID  jstack  nativeID 。
- NAME: 
- GROUP: 
- PRIORITY: , 1~10 ，
- STATE: 
- CPU%:  cpu 。 1000ms， cpu  100ms， cpu =100/1000=10%
- DELTA_TIME:  CPU ，``
- TIME:  CPU ，`:`
- INTERRUPTED: 
- DAEMON:  daemon 

### JVM 

Java 8  JVM  CPU ， CPU ， ID （ ID -1）。
 JVM ， GC、JIT  CPU ， JVM 。

-  JVM (heap)/(metaspace) OOM ， GC  CPU 。
- `trace/watch/tt/redefine`， JIT 。 JVM  class  class  JIT ，。

JVM ：

- JIT :  `C1 CompilerThread0`, `C2 CompilerThread0`
- GC : `GC Thread0`, `G1 Young RemSet Sampling`
- : `VM Periodic Task Thread`, `VM Thread`, `Service Thread`

## 

![](/images/dashboard.png "dashboard")
