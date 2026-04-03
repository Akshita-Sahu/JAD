# heapdump

[`heapdump` online tutorial](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=en&id=command-heapdump)

::: tip
dump java heap in hprof binary format, like `jmap`.
:::

## Usage

### Dump to file

```bash
[jad@58205]$ heapdump jad-output/dump.hprof
Dumping heap to jad-output/dump.hprof ...
Heap dump file created
```

::: tip
The generated file is located in the jad-output directory and can be downloaded through the browser at http://localhost:8563/jad-output/
:::

### Dump only live objects

```bash
[jad@58205]$ heapdump --live /tmp/dump.hprof
Dumping heap to /tmp/dump.hprof ...
Heap dump file created
```

### Dump to tmp file

```bash
[jad@58205]$ heapdump
Dumping heap to /var/folders/my/wy7c9w9j5732xbkcyt1mb4g40000gp/T/heapdump2019-09-03-16-385121018449645518991.hprof...
Heap dump file created
```
