# dump

[`dump`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=command-dump)

::: tip
dump  bytecode 
:::

dump  JVM  class  byte code dump ， class ；、， [jad](/doc/jad.md)。

## 

|               |                                    |
| --------------------: | :----------------------------------------- |
|       _class-pattern_ |                              |
|                `[c:]` |  ClassLoader  hashcode             |
| `[classLoaderClass:]` |  ClassLoader  class name |
|                `[d:]` |                        |
|                   [E] | ，       |

## 

```bash
$ dump java.lang.String
 HASHCODE  CLASSLOADER  LOCATION
 null                   /Users/admin/logs/jad/classdump/java/lang/String.class
Affect(row-cnt:1) cost in 119 ms.
```

```bash
$ dump demo.*
 HASHCODE  CLASSLOADER                                    LOCATION
 3d4eac69  +-sun.misc.Launcher$AppClassLoader@3d4eac69    /Users/admin/logs/jad/classdump/sun.misc.Launcher$AppClassLoader-3d4eac69/demo/MathGame.class
             +-sun.misc.Launcher$ExtClassLoader@66350f69
Affect(row-cnt:1) cost in 39 ms.
```

```bash
$ dump -d /tmp/output java.lang.String
 HASHCODE  CLASSLOADER  LOCATION
 null                   /tmp/output/java/lang/String.class
Affect(row-cnt:1) cost in 138 ms.
```

-  classLoader

 hashcode ， ClassLoader ， ClassLoader  hashcode。

`-c`， hashcode：`-c <hashcode>`

```bash
$ dump -c 3d4eac69 demo.*
```

 ClassLoader `--classLoaderClass` class name，：

```bash
$ dump --classLoaderClass sun.misc.Launcher$AppClassLoader demo.*
 HASHCODE  CLASSLOADER                                    LOCATION
 3d4eac69  +-sun.misc.Launcher$AppClassLoader@3d4eac69    /Users/admin/logs/jad/classdump/sun.misc.Launcher$AppClassLoader-3d4eac69/demo/MathGame.class
             +-sun.misc.Launcher$ExtClassLoader@66350f69
Affect(row-cnt:1) cost in 39 ms.
```

- ： classLoaderClass  java 8  sun.misc.Launcher$AppClassLoader， java 11  classloader  jdk.internal.loader.ClassLoaders$AppClassLoader，killercoda  java11。

`--classLoaderClass`  ClassLoader ， ClassLoader ，，`-c <hashcode>`。
