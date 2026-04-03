# mc

[`mc-retransform`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=command-mc-retransform)

## 

::: tip
Memory Compiler/，`.java``.class`。
:::

```bash
mc /tmp/Test.java
```

`-c` classloader：

```bash
mc -c 327a647b /tmp/Test.java
```

`--classLoaderClass` ClassLoader：

```bash
$ mc --classLoaderClass org.springframework.boot.loader.LaunchedURLClassLoader /tmp/UserController.java -d /tmp
Memory compiler output:
/tmp/com/example/demo/jad/user/UserController.class
Affect(row-cnt:1) cost in 346 ms
```

`-d`：

```bash
mc -d /tmp/output /tmp/ClassA.java /tmp/ClassB.java
```

`.class`，[retransform](retransform.md)。

::: warning
，mc 。`.class`，。[retransform](retransform.md)。
:::
