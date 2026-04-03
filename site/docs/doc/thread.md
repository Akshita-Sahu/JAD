# thread

[`thread`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-thread)

::: tip
，
:::

## 

|       |                                                 |
| ------------: | :------------------------------------------------------ |
|          _id_ |  id                                                 |
|          [n:] |  N                          |
|           [b] |                               |
| [i `<value>`] |  cpu ，， 200 |
|       [--all] |                                       |

## cpu ？

 cpu  linux `top -H -p <pid>` `%CPU`，， JVM  cpu 。

### ：

- ， CPU (`java.lang.management.ThreadMXBean#getThreadCpuTime()``sun.management.HotspotThreadMBean.getInternalThreadCpuTimes()`)
- （ 200ms，`-i`）
- ， CPU ，， CPU 
-  CPU  =  CPU  /  \* 100%

::: warning
： （JDK ）， as ，，， 5000 。
:::

::: tip
 Java  cpu ：[`show-busy-java-threads`](https://github.com/oldratlee/useful-scripts/blob/dev-2.x/docs/java.md#-show-busy-java-threads)。
:::

## 

###  N ：

```shell
$ thread -n 3
"C1 CompilerThread0" [Internal] cpuUsage=1.63% deltaTime=3ms time=1170ms


"jad-command-execute" Id=23 cpuUsage=0.11% deltaTime=0ms time=401ms RUNNABLE
    at java.management@11.0.7/sun.management.ThreadImpl.dumpThreads0(Native Method)
    at java.management@11.0.7/sun.management.ThreadImpl.getThreadInfo(ThreadImpl.java:466)
    at com.akshita.jad.core.command.monitor200.ThreadCommand.processTopBusyThreads(ThreadCommand.java:199)
    at com.akshita.jad.core.command.monitor200.ThreadCommand.process(ThreadCommand.java:122)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl.process(AnnotatedCommandImpl.java:82)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl.access$100(AnnotatedCommandImpl.java:18)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl$ProcessHandler.handle(AnnotatedCommandImpl.java:111)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl$ProcessHandler.handle(AnnotatedCommandImpl.java:108)
    at com.akshita.jad.core.shell.system.impl.ProcessImpl$CommandProcessTask.run(ProcessImpl.java:385)
    at java.base@11.0.7/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
    at java.base@11.0.7/java.util.concurrent.FutureTask.run(FutureTask.java:264)
    at java.base@11.0.7/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304)
    at java.base@11.0.7/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
    at java.base@11.0.7/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
    at java.base@11.0.7/java.lang.Thread.run(Thread.java:834)


"VM Periodic Task Thread" [Internal] cpuUsage=0.07% deltaTime=0ms time=584ms
```

-  ID，`[Internal]` JVM ，[dashboard](dashboard.md)。
- `cpuUsage` CPU ，[dashboard](dashboard.md)。
- `deltaTime` CPU ， 1ms  0ms。
- `time`  CPU 。

：，。，。
，。

### ，

 CPU ，。

```shell
$ thread
Threads Total: 33, NEW: 0, RUNNABLE: 9, BLOCKED: 0, WAITING: 3, TIMED_WAITING: 4, TERMINATED: 0, Internal threads: 17
ID   NAME                           GROUP          PRIORITY  STATE     %CPU      DELTA_TIME TIME      INTERRUPT DAEMON
-1   C2 CompilerThread0             -              -1        -         5.06      0.010      0:0.973   false     true
-1   C1 CompilerThread0             -              -1        -         0.95      0.001      0:0.603   false     true
23   jad-command-execute         system         5         RUNNABLE  0.17      0.000      0:0.226   false     true
-1   VM Periodic Task Thread        -              -1        -         0.05      0.000      0:0.094   false     true
-1   Sweeper thread                 -              -1        -         0.04      0.000      0:0.011   false     true
-1   G1 Young RemSet Sampling       -              -1        -         0.02      0.000      0:0.025   false     true
12   Attach Listener                system         9         RUNNABLE  0.0       0.000      0:0.022   false     true
11   Common-Cleaner                 InnocuousThrea 8         TIMED_WAI 0.0       0.000      0:0.000   false     true
3    Finalizer                      system         8         WAITING   0.0       0.000      0:0.000   false     true
2    Reference Handler              system         10        RUNNABLE  0.0       0.000      0:0.000   false     true
4    Signal Dispatcher              system         9         RUNNABLE  0.0       0.000      0:0.000   false     true
15   jad-NettyHttpTelnetBootstra system         5         RUNNABLE  0.0       0.000      0:0.029   false     true
22   jad-NettyHttpTelnetBootstra system         5         RUNNABLE  0.0       0.000      0:0.196   false     true
24   jad-NettyHttpTelnetBootstra system         5         RUNNABLE  0.0       0.000      0:0.038   false     true
16   jad-NettyWebsocketTtyBootst system         5         RUNNABLE  0.0       0.000      0:0.001   false     true
17   jad-NettyWebsocketTtyBootst system         5         RUNNABLE  0.0       0.000      0:0.001   false     true
```

### thread --all, 

， JVM 。

### thread id, 

```shell
$ thread 1
"main" Id=1 WAITING on java.util.concurrent.CountDownLatch$Sync@29fafb28
    at sun.misc.Unsafe.park(Native Method)
    -  waiting on java.util.concurrent.CountDownLatch$Sync@29fafb28
    at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
    at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
    at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
    at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
    at java.util.concurrent.CountDownLatch.await(CountDownLatch.java:231)
```

### thread -b, 

， ， 。 ， jad `thread -b`， 。

```bash
$ thread -b
"http-bio-8080-exec-4" Id=27 TIMED_WAITING
    at java.lang.Thread.sleep(Native Method)
    at test.jad.TestThreadBlocking.doGet(TestThreadBlocking.java:22)
    -  locked java.lang.Object@725be470 <---- but blocks 4 other threads!
    at javax.servlet.http.HttpServlet.service(HttpServlet.java:624)
    at javax.servlet.http.HttpServlet.service(HttpServlet.java:731)
    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)
    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)
    at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)
    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)
    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)
    at test.filter.TestDurexFilter.doFilter(TestDurexFilter.java:46)
    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)
    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)
    at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:220)
    at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)
    at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)
    at com.akshita-sahu.tomcat.valves.ContextLoadFilterValve$FilterChainAdapter.doFilter(ContextLoadFilterValve.java:191)
    at com.akshita-sahu.eagleeye.EagleEyeFilter.doFilter(EagleEyeFilter.java:81)
    at com.akshita-sahu.tomcat.valves.ContextLoadFilterValve.invoke(ContextLoadFilterValve.java:150)
    at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:170)
    at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)
    at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)
    at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:429)
    at org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1085)
    at org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:625)
    at org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)
    -  locked org.apache.tomcat.util.net.SocketWrapper@7127ee12
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
    at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
    at java.lang.Thread.run(Thread.java:745)

    Number of locked synchronizers = 1
    - java.util.concurrent.ThreadPoolExecutor$Worker@31a6493e
```

::: warning
，  synchronized ， `java.util.concurrent.Lock`， 。
:::

### thread -i, 

- `thread -i 1000` :  1000ms  CPU 。

- `thread -n 3 -i 1000` :  1000ms  3 

```bash
$ thread -n 3 -i 1000
"jad-command-execute-daemon" Id=4759 cpuUsage=23% RUNNABLE
    at sun.management.ThreadImpl.dumpThreads0(Native Method)
    at sun.management.ThreadImpl.getThreadInfo(ThreadImpl.java:440)
    at com.akshita.jad.core.command.monitor200.ThreadCommand.processTopBusyThreads(ThreadCommand.java:133)
    at com.akshita.jad.core.command.monitor200.ThreadCommand.process(ThreadCommand.java:79)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl.process(AnnotatedCommandImpl.java:96)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl.access$100(AnnotatedCommandImpl.java:27)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl$ProcessHandler.handle(AnnotatedCommandImpl.java:125)
    at com.akshita.jad.core.shell.command.impl.AnnotatedCommandImpl$ProcessHandler.handle(AnnotatedCommandImpl.java:122)
    at com.akshita.jad.core.shell.system.impl.ProcessImpl$CommandProcessTask.run(ProcessImpl.java:332)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
    at java.lang.Thread.run(Thread.java:756)

    Number of locked synchronizers = 1
    - java.util.concurrent.ThreadPoolExecutor$Worker@546aeec1
...
```

### thread --state ，

```bash
[jad@28114]$ thread --state WAITING
Threads Total: 16, NEW: 0, RUNNABLE: 9, BLOCKED: 0, WAITING: 3, TIMED_WAITING: 4, TERMINATED: 0
ID   NAME                           GROUP           PRIORITY   STATE     %CPU      DELTA_TIME TIME      INTERRUPTE DAEMON
3    Finalizer                      system          8          WAITING   0.0       0.000      0:0.000   false      true
20   jad-UserStat                system          9          WAITING   0.0       0.000      0:0.001   false      true
14   jad-timer                   system          9          WAITING   0.0       0.000      0:0.000   false      true
```
