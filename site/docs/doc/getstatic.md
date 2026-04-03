# getstatic

[`getstatic`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-getstatic)

### 

- [ognl](ognl.md)，。

 getstatic 。`getstatic class_name field_name`

```bash
$ getstatic demo.MathGame random
field: random
@Random[
    serialVersionUID=@Long[3905348978240129619],
    seed=@AtomicLong[120955813885284],
    multiplier=@Long[25214903917],
    addend=@Long[11],
    mask=@Long[281474976710655],
    DOUBLE_UNIT=@Double[1.1102230246251565E-16],
    BadBound=@String[bound must be positive],
    BadRange=@String[bound must be greater than origin],
    BadSize=@String[size must be non-negative],
    seedUniquifier=@AtomicLong[-3282039941672302964],
    nextNextGaussian=@Double[0.0],
    haveNextNextGaussian=@Boolean[false],
    serialPersistentFields=@ObjectStreamField[][isEmpty=false;size=3],
    unsafe=@Unsafe[sun.misc.Unsafe@2eaa1027],
    seedOffset=@Long[24],
]
```

-  classLoader

 hashcode ， ClassLoader ，`sc -d <ClassName>` ClassLoader  hashcode。

`-c`， hashcode：`-c <hashcode>`

```bash
$ getstatic -c 3d4eac69 demo.MathGame random
```

 ClassLoader `--classLoaderClass` class name，：

`getstatic --classLoaderClass sun.misc.Launcher$AppClassLoader demo.MathGame random`

- :  classLoaderClass  java 8  sun.misc.Launcher$AppClassLoader，java 11classloaderjdk.internal.loader.ClassLoaders$AppClassLoader，killercoda  java11。

`--classLoaderClass`  ClassLoader ， ClassLoader ，，`-c <hashcode>`。

， ognl ，，。

- OGNL ：[https://github.com/akshita-sahu/jad/issues/71](https://github.com/akshita-sahu/jad/issues/71)
- OGNL ：[https://commons.apache.org/dormant/commons-ognl/language-guide.html](https://commons.apache.org/dormant/commons-ognl/language-guide.html)

， n  Map，Map  Key  Enum， Map  Key  Enum ，

```
$ getstatic com.akshita.jad.Test n 'entrySet().iterator.{? #this.key.name()=="STOP"}'
field: n
@ArrayList[
    @Node[STOP=bbb],
]
Affect(row-cnt:1) cost in 68 ms.


$ getstatic com.akshita.jad.Test m 'entrySet().iterator.{? #this.key=="a"}'
field: m
@ArrayList[
    @Node[a=aaa],
]
```
