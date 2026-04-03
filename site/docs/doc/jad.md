# jad

[`jad`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=command-jad)

::: tip

:::

`jad`  JVM  class  byte code  java ，； class  [dump](/doc/dump.md)。

-  JAD Console ，，
- ， java ，

## 

|               |                                    |
| --------------------: | :----------------------------------------- |
|       _class-pattern_ |                              |
|                `[c:]` |  ClassLoader  hashcode             |
| `[classLoaderClass:]` |  ClassLoader  class name |
|                   [E] | ，       |

## 

### `java.lang.String`

```java
$ jad java.lang.String

ClassLoader:

Location:


        /*
         * Decompiled with CFR.
         */
        package java.lang;

        import java.io.ObjectStreamField;
        import java.io.Serializable;
...
        public final class String
        implements Serializable,
        Comparable<String>,
        CharSequence {
            private final char[] value;
            private int hash;
            private static final long serialVersionUID = -6849794470754667710L;
            private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
            public static final Comparator<String> CASE_INSENSITIVE_ORDER = new CaseInsensitiveComparator();
...
            public String(byte[] byArray, int n, int n2, Charset charset) {
/*460*/         if (charset == null) {
                    throw new NullPointerException("charset");
                }
/*462*/         String.checkBounds(byArray, n, n2);
/*463*/         this.value = StringCoding.decode(charset, byArray, n, n2);
            }
...
```

### 

，`ClassLoader`，`--source-only`，。[mc](mc.md)/[retransform](retransform.md)。

```java
$ jad --source-only demo.MathGame
/*
 * Decompiled with CFR 0_132.
 */
package demo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MathGame {
    private static Random random = new Random();
    public int illegalArgumentCount = 0;
...
```

### 

```java
$ jad demo.MathGame main

ClassLoader:
+-sun.misc.Launcher$AppClassLoader@232204a1
  +-sun.misc.Launcher$ExtClassLoader@7f31245a

Location:
/private/tmp/math-game.jar

       public static void main(String[] args) throws InterruptedException {
           MathGame game = new MathGame();
           while (true) {
/*16*/         game.run();
/*17*/         TimeUnit.SECONDS.sleep(1L);
           }
       }
```

### 

`--lineNumber`  true， false 。

```java
$ jad demo.MathGame main --lineNumber false

ClassLoader:
+-sun.misc.Launcher$AppClassLoader@232204a1
  +-sun.misc.Launcher$ExtClassLoader@7f31245a

Location:
/private/tmp/math-game.jar

public static void main(String[] args) throws InterruptedException {
    MathGame game = new MathGame();
    while (true) {
        game.run();
        TimeUnit.SECONDS.sleep(1L);
    }
}
```

###  ClassLoader

::: tip
 `ClassLoader` ，`jad`  `ClassLoader`  `hashcode`， `jad` ， `-c <hashcode>`  ClassLoader ；
:::

```java
$ jad org.apache.log4j.Logger

Found more than one class for: org.apache.log4j.Logger, Please use jad -c hashcode org.apache.log4j.Logger
HASHCODE  CLASSLOADER
69dcaba4  +-monitor's ModuleClassLoader
6e51ad67  +-java.net.URLClassLoader@6e51ad67
            +-sun.misc.Launcher$AppClassLoader@6951a712
            +-sun.misc.Launcher$ExtClassLoader@6fafc4c2
2bdd9114  +-pandora-qos-service's ModuleClassLoader
4c0df5f8  +-pandora-framework's ModuleClassLoader

Affect(row-cnt:0) cost in 38 ms.
$ jad org.apache.log4j.Logger -c 69dcaba4

ClassLoader:
+-monitor's ModuleClassLoader

Location:
/Users/admin/app/log4j-1.2.14.jar

package org.apache.log4j;

import org.apache.log4j.spi.*;

public class Logger extends Category
{
    private static final String FQCN;

    protected Logger(String name)
    {
        super(name);
    }

...

Affect(row-cnt:1) cost in 190 ms.
```

 ClassLoader `--classLoaderClass` class name，：

`--classLoaderClass`  ClassLoader ， ClassLoader ，，`-c <hashcode>`。

### dump class

`jad`class dump，dumplogback.xmllog，`-d/--directory`dump。

```java
$ jad demo.MathGame -d /tmp/jad/dump
```
