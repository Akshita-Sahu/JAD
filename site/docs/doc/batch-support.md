# 

::: tip
，jad ，。 `--select` 。
:::

## 

### ：

`test.as`，，.as ， ok。

::: tip

- 
- dashboard  (`-n`)，
- watch/tt/trace/monitor/stack  (`-n`)，
- ， `watch c.t.X test returnObj > &`，，，[](async.md)
  :::

```
➜  jad git:(develop) cat /var/tmp/test.as
help
dashboard -n 1
session
thread
sc -d org.apache.commons.lang.StringUtils
```

### ：

`-f`，，。

```bash
./jad.sh -f /var/tmp/test.as <pid> > test.out # pid  jps 
```

 `-c` ，

```bash
./jad.sh -c 'sysprop; thread' <pid> > test.out # pid  jps 
```

### ：

```bash
cat test.out
```
