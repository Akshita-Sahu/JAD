# perfcounter

[`perfcounter`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-perfcounter)

::: tip
 JVM  Perf Counter 
:::

## 

```
$ perfcounter
 java.ci.totalTime                            2325637411
 java.cls.loadedClasses                       3403
 java.cls.sharedLoadedClasses                 0
 java.cls.sharedUnloadedClasses               0
 java.cls.unloadedClasses                     0
 java.property.java.version                   11.0.4
 java.property.java.vm.info                   mixed mode
 java.property.java.vm.name                   OpenJDK 64-Bit Server VM
...
```

`-d`：

```
$ perfcounter -d
 Name                                   Variability   Units        Value
---------------------------------------------------------------------------------
 java.ci.totalTime                      Monotonic     Ticks        3242526906
 java.cls.loadedClasses                 Monotonic     Events       3404
 java.cls.sharedLoadedClasses           Monotonic     Events       0
 java.cls.sharedUnloadedClasses         Monotonic     Events       0
 java.cls.unloadedClasses               Monotonic     Events       0
```

## jdk9 

，，：

```
--add-opens java.base/jdk.internal.perf=ALL-UNNAMED --add-exports java.base/jdk.internal.perf=ALL-UNNAMED --add-opens java.management/sun.management.counter.perf=ALL-UNNAMED --add-opens java.management/sun.management.counter=ALL-UNNAMED
```
