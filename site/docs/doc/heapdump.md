# heapdump

[`heapdump`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-heapdump)

::: tip
dump java heap,  jmap  heap dump 。
:::

## 

### dump 

```bash
[jad@58205]$ heapdump jad-output/dump.hprof
Dumping heap to jad-output/dump.hprof ...
Heap dump file created
```

::: tip
`jad-output`，： http://localhost:8563/jad-output/
:::

###  dump live 

```bash
[jad@58205]$ heapdump --live /tmp/dump.hprof
Dumping heap to /tmp/dump.hprof ...
Heap dump file created
```

## dump 

```bash
[jad@58205]$ heapdump
Dumping heap to /var/folders/my/wy7c9w9j5732xbkcyt1mb4g40000gp/T/heapdump2019-09-03-16-385121018449645518991.hprof...
Heap dump file created
```
