### JAD3.0 

#### 

JAD3.0 ，，

##### 

TODO

##### 

TODO

#### 

JAD 3.0 , `grep`,`wc`,`plaintext`。

###  groovy 

groovy  jad2.0 ， watch 

```bash
watch com.akshita-sahu.sample.petstore.web.store.module.screen.ItemList add "params + ' ' + returnObj" params.size()==2
```

`"params + ' ' + returnObj"``params.size()==2` groovy ，，groovy  classloader， perm  FGC。

，JAD 3.0  ognl  groovy， groovy  FGC 。， groovy 。。

 3.0 ，watch ，[](https://github.com/Akshita-Sahu/JAD/doc/watch)

####  rt 

JAD 2.0 ， rt `ms`，， 0ms， trace （， JAD ）。JAD 3.0  rt `ns`，， 0ms ！

```
$ tt -l
 INDEX     TIMESTAMP               COST(ms)    IS-RET    IS-EXP   OBJECT            CLASS                                METHOD
------------------------------------------------------------------------------------------------------------------------------------------------------------
 1000      2017-02-24 10:56:46     808.743525  true      false    0x3bd5e918        TestTraceServlet                     doGet
 1001      2017-02-24 10:56:55     805.799155  true      false    0x3bd5e918        TestTraceServlet                     doGet
 1002      2017-02-24 10:57:04     808.026935  true      false    0x3bd5e918        TestTraceServlet                     doGet
 1003      2017-02-24 10:57:22     805.036963  true      false    0x3bd5e918        TestTraceServlet                     doGet
 1004      2017-02-24 10:57:24     803.581886  true      false    0x3bd5e918        TestTraceServlet                     doGet
 1005      2017-02-24 10:57:39     814.657657  true      false    0x3bd5e918        TestTraceServlet                     doGet
```

#### watch/stack/trace 

 trace ， rt ， rt ，， rt ，JAD 3.0 `#cost`(,`ms`)， trace ，，，。

#### sysprop  SystemProperty

sysprop ，。

```
$ sysprop
...
 os.arch                                              x86_64
 java.ext.dirs                                        /Users/wangtao/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.
                                                      8.0_51.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library
                                                      /Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
 user.dir                                             /Users/wangtao/work/ali-tomcat-home/ant-develop/output/build
 catalina.vendor                                      akshita-sahu
 line.separator

 java.vm.name                                         Java HotSpot(TM) 64-Bit Server VM
 file.encoding                                        UTF-8
 org.apache.tomcat.util.http.ServerCookie.ALLOW_EQUA  true
 LS_IN_VALUE
 com.akshita-sahu.tomcat.info                               Apache Tomcat/7.0.70.1548
 java.specification.version                           1.8
$ sysprop java.version
java.version=1.8.0_51
$ sysprop production.mode true
Successfully changed the system property.
production.mode=true
```

#### thread 

thread  cpu ， 100ms  cpu  cpu 。 100ms ，，JAD3.0  thread (`ms`)， cpu 。

```
$ thread -i 1000
Threads Total: 74, NEW: 0, RUNNABLE: 17, BLOCKED: 0, WAITING: 15, TIMED_WAITING: 42, TERMINATED: 0
ID                 NAME                                                     GROUP                                  PRIORITY           STATE              %CPU               TIME               INTERRUPTED        DAEMON
78                 com.akshita-sahu.config.client.timer                           main                                   5                  TIMED_WAITING      22                 0:0                false              true
92                 Abandoned connection cleanup thread                      main                                   5                  TIMED_WAITING      15                 0:2                false              true
361                jad-command-execute-daemon                                system                                 10                 RUNNABLE           14                 0:0                false              true
67                 HSF-Remoting-Timer-10-thread-1                           main                                   10                 TIMED_WAITING      12                 0:2                false              true
113                JamScheduleThread                                        system                                 9                  TIMED_WAITING      2                  0:0                false              true
14                 Thread-3                                                 main                                   5                  RUNNABLE           2                  0:0                false              false
81                 com.akshita-sahu.remoting.TimerThread                          main                                   5                  TIMED_WAITING      2                  0:0                false              true
104                http-bio-7001-AsyncTimeout                               main                                   5                  TIMED_WAITING      2                  0:0                false              true
123                nioEventLoopGroup-2-1                                    system                                 10                 RUNNABLE           2                  0:0                false              false
127                nioEventLoopGroup-3-2                                    system                                 10                 RUNNABLE           2                  0:0                false              false
345                nioEventLoopGroup-3-3                                    system                                 10                 RUNNABLE           2                  0:0                false              false
358                nioEventLoopGroup-3-4                                    system                                 10                 RUNNABLE           2                  0:0                false              false
27                 qos-boss-1-1                                             main                                   5                  RUNNABLE           2                  0:0                false              true
22                 EagleEye-AsyncAppender-Thread-BizLog                     main                                   5                  TIMED_WAITING      1                  0:0                false              true
```

#### trace 

trace 

![Untitled2](TODO /Untitled2.gif)
