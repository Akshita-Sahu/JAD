# classloader

[`classloader`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=command-classloader)

::: tip
 classloader ，urls，
:::

`classloader`  JVM  classloader ，，urls 。

 classloader  getResources， resources  url。`ResourceNotFoundException`。

## 

|               |                                    |
| --------------------: | :----------------------------------------- |
|                   [l] |                        |
|                   [t] |  ClassLoader               |
|                   [a] |  ClassLoader ，  |
|                `[c:]` | ClassLoader  hashcode                    |
| `[classLoaderClass:]` |  ClassLoader  class name |
|             `[c: r:]` |  ClassLoader  resource             |
|          `[c: load:]` |  ClassLoader               |

### `--url-classes` 

|           |                                                                   |
| ----------------: | :------------------------------------------------------------------------ |
|   `--url-classes` |  ClassLoader ， `codeSource(URL/jar)`           |
|   `-d, --details` | ： URL/jar （ `-n/--limit` ） |
|      `--jar <kw>` |  jar /URL （）                                |
|    `--class <kw>` | /（）                                     |
|     `-E, --regex` | `--jar/--class` （）                        |
| `-n, --limit <N>` | ， URL/jar  N （100 ）                      |

## 

### 

```bash
$ classloader
 name                                       numberOfInstances  loadedCountTotal
 com.akshita.jad.agent.JADClassloader  1                  2115
 BootstrapClassLoader                       1                  1861
 sun.reflect.DelegatingClassLoader          5                  5
 sun.misc.Launcher$AppClassLoader           1                  4
 sun.misc.Launcher$ExtClassLoader           1                  1
Affect(row-cnt:5) cost in 3 ms.
```

### 

```bash
$ classloader -l
 name                                                loadedCount  hash      parent
 BootstrapClassLoader                                1861         null      null
 com.akshita.jad.agent.JADClassloader@68b31f0a  2115         68b31f0a  sun.misc.Launcher$ExtClassLoader@66350f69
 sun.misc.Launcher$AppClassLoader@3d4eac69           4            3d4eac69  sun.misc.Launcher$ExtClassLoader@66350f69
 sun.misc.Launcher$ExtClassLoader@66350f69           1            66350f69  null
Affect(row-cnt:4) cost in 2 ms.
```

###  ClassLoader 

```bash
$ classloader -t
+-BootstrapClassLoader
+-sun.misc.Launcher$ExtClassLoader@66350f69
  +-com.akshita.jad.agent.JADClassloader@68b31f0a
  +-sun.misc.Launcher$AppClassLoader@3d4eac69
Affect(row-cnt:4) cost in 3 ms.
```

###  URLClassLoader  urls

```bash
$ classloader -c 3d4eac69
file:/private/tmp/math-game.jar
file:/Users/hengyunabc/.jad/lib/3.0.5/jad/jad-agent.jar

Affect(row-cnt:9) cost in 3 ms.
```

__ hashcode ， ClassLoader ， ClassLoader  hashcode。

 ClassLoader  class name ，：

```bash
$ classloader --classLoaderClass sun.misc.Launcher$AppClassLoader
file:/private/tmp/math-game.jar
file:/Users/hengyunabc/.jad/lib/3.0.5/jad/jad-agent.jar

Affect(row-cnt:9) cost in 3 ms.
```

###  ClassLoader  resource

```bash
$ classloader -c 3d4eac69  -r META-INF/MANIFEST.MF
 jar:file:/System/Library/Java/Extensions/MRJToolkit.jar!/META-INF/MANIFEST.MF
 jar:file:/private/tmp/math-game.jar!/META-INF/MANIFEST.MF
 jar:file:/Users/hengyunabc/.jad/lib/3.0.5/jad/jad-agent.jar!/META-INF/MANIFEST.MF
```

 class ：

```bash
$ classloader -c 1b6d3586 -r java/lang/String.class
 jar:file:/Library/Java/JavaVirtualMachines/jdk1.8.0_60.jdk/Contents/Home/jre/lib/rt.jar!/java/lang/String.class
```

###  ClassLoader 

```bash
$ classloader -c 3d4eac69 --load demo.MathGame
load class success.
 class-info        demo.MathGame
 code-source       /private/tmp/math-game.jar
 name              demo.MathGame
 isInterface       false
 isAnnotation      false
 isEnum            false
 isAnonymousClass  false
 isArray           false
 isLocalClass      false
 isMemberClass     false
 isPrimitive       false
 isSynthetic       false
 simple-name       MathGame
 modifier          public
 annotation
 interfaces
 super-class       +-java.lang.Object
 class-loader      +-sun.misc.Launcher$AppClassLoader@3d4eac69
                     +-sun.misc.Launcher$ExtClassLoader@66350f69
 classLoaderHash   3d4eac69
```

###  ClassLoader  URL  URL

::: warning
， JVM ，`Unused URLs`。`Unused URLs`，`resources`。
:::

```
$ classloader --url-stat
 com.akshita.jad.agent.JADClassloader@3c41660, hash:3c41660
 Used URLs:
 file:/Users/admin/.jad/lib/3.5.6/jad/jad-core.jar
 Unused URLs:

 sun.misc.Launcher$AppClassLoader@75b84c92, hash:75b84c92
 Used URLs:
 file:/Users/admin/code/java/jad/math-game/target/math-game.jar
 file:/Users/admin/.jad/lib/3.5.6/jad/jad-agent.jar
 Unused URLs:

 sun.misc.Launcher$ExtClassLoader@7f31245a, hash:7f31245a
 Used URLs:
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/sunec.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/sunjce_provider.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/localedata.jar
 Unused URLs:
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/nashorn.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/cldrdata.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/legacy8ujsse.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/jfxrt.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/dnsns.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/openjsse.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/sunpkcs11.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/jaccess.jar
 file:/tmp/jdk1.8/Contents/Home/jre/lib/ext/zipfs.jar
```

###  ClassLoader  jar(URL) 

`--url-classes`  ClassLoader ， jar(URL)， jar(URL) 。

```bash
$ classloader -c 3d4eac69 --url-classes
sun.misc.Launcher$AppClassLoader@3d4eac69, hash:3d4eac69
 url                                            loadedClassCount
 file:/private/tmp/math-game.jar                 42
 file:/Users/hengyunabc/.jad/lib/jad-agent.jar  15
Affect(row-cnt:2) cost in 3 ms.
```

 jar （）：

```bash
$ classloader -c 3d4eac69 --url-classes -d --jar math-game
```

/（ `matchedClassCount` ）：

```bash
$ classloader -c 3d4eac69 --url-classes --jar spring-core --class org.springframework
```
