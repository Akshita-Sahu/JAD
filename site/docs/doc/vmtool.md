# vmtool

::: tip
@since 3.5.1
:::

[`vmtool`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-vmtool)

`vmtool` `JVMTI`，， GC 。

- [JVM Tool Interface](https://docs.oracle.com/javase/8/docs/platform/jvmti/jvmti.html)

## 

```bash
$ vmtool --action getInstances --className java.lang.String --limit 10
@String[][
    @String[com/akshita-sahu/jad/core/shell/session/Session],
    @String[com.akshita.jad.core.shell.session.Session],
    @String[com/akshita-sahu/jad/core/shell/session/Session],
    @String[com/akshita-sahu/jad/core/shell/session/Session],
    @String[com/akshita-sahu/jad/core/shell/session/Session.class],
    @String[com/akshita-sahu/jad/core/shell/session/Session.class],
    @String[com/akshita-sahu/jad/core/shell/session/Session.class],
    @String[com/],
    @String[java/util/concurrent/ConcurrentHashMap$ValueIterator],
    @String[java/util/concurrent/locks/LockSupport],
]
```

::: tip
 `--limit`，， JVM 。 10。
:::

##  classloader name

```bash
vmtool --action getInstances --classLoaderClass org.springframework.boot.loader.LaunchedURLClassLoader --className org.springframework.context.ApplicationContext
```

##  classloader hash

`sc` class  classloader。

```bash
$ sc -d org.springframework.context.ApplicationContext
 class-info        org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext
 code-source       file:/private/tmp/demo-jad-spring-boot.jar!/BOOT-INF/lib/spring-boot-1.5.13.RELEASE.jar!/
 name              org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext
...
 class-loader      +-org.springframework.boot.loader.LaunchedURLClassLoader@19469ea2
                     +-sun.misc.Launcher$AppClassLoader@75b84c92
                       +-sun.misc.Launcher$ExtClassLoader@4f023edb
 classLoaderHash   19469ea2
```

`-c`/`--classloader` ：

```bash
vmtool --action getInstances -c 19469ea2 --className org.springframework.context.ApplicationContext
```

## 

::: tip
`getInstances` action `instances`，。

 `-x`/`--expand` ， 1。
:::

```bash
vmtool --action getInstances -c 19469ea2 --className org.springframework.context.ApplicationContext -x 2
```

## 

::: tip
`getInstances` action `instances`，。`--express`。
:::

```bash
vmtool --action getInstances --classLoaderClass org.springframework.boot.loader.LaunchedURLClassLoader --className org.springframework.context.ApplicationContext --express 'instances[0].getBeanDefinitionNames()'
```

##  GC

```bash
vmtool --action forceGc
```

-  [`vmoption`](vmoption.md) `PrintGC`。

## 

`heapAnalyze`  GC Root ，，。

```bash
$ vmtool --action heapAnalyze --classNum 5 --objectNum 3
```

::: tip
 `--classNum` ， `--objectNum` 。
:::

## 

`referenceAnalyze` ，（ GC Root），。

```bash
$ vmtool --action referenceAnalyze --className java.lang.String --objectNum 5 --backtraceNum 3
```

::: tip

-  `--objectNum` 
-  `--backtraceNum` ， `-1`  root， `0` 
- `getInstances`  `--classLoaderClass` / `--classloader`  `referenceAnalyze`
  :::

## interrupt 

thread id `-t`， `thread`。

```bash
vmtool --action interruptThread -t 1
```

## glibc 

Linux man page: [malloc_trim](https://man7.org/linux/man-pages/man3/malloc_trim.3.html)

```bash
vmtool --action mallocTrim
```

## glibc 

 stderr。Linux man page: [malloc_stats](https://man7.org/linux/man-pages/man3/malloc_stats.3.html)

```bash
vmtool --action mallocStats
```

 stderr ：

```
Arena 0:
system bytes     =     135168
in use bytes     =      74352
Total (incl. mmap):
system bytes     =     135168
in use bytes     =      74352
max mmap regions =          0
max mmap bytes   =          0
```
