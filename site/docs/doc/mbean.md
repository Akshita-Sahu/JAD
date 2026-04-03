# mbean

[`mbean`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-mbean)

::: tip
 Mbean 
:::

 Mbean 。

## 

|             |                                              |
| ------------------: | :--------------------------------------------------- |
|      _name-pattern_ |                                        |
| _attribute-pattern_ |                                      |
|                 [m] |                                            |
|                [i:] |  (ms)                            |
|                [n:] |                                      |
|                 [E] | ，。 |

## 

 Mbean ：

```bash
mbean
```

 Mbean ：

```bash
mbean -m java.lang:type=Threading
```

 mbean ：

```bash
mbean java.lang:type=Threading
```

mbean  name ：

```bash
mbean java.lang:type=Th*
```

::: warning
：ObjectName ，：[javax.management.ObjectName](https://docs.oracle.com/javase/8/docs/api/javax/management/ObjectName.html?is-external=true)
:::

：

```bash
mbean java.lang:type=Threading *Count
```

`-E`：

```bash
mbean -E java.lang:type=Threading PeakThreadCount|ThreadCount|DaemonThreadCount
```

`-i`：

```bash
mbean -i 1000 java.lang:type=Threading *Count
```

`-i`，`-n`（ 100 ）：

```bash
mbean -i 1000 -n 50 java.lang:type=Threading *Count
```
