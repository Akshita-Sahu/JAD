# telnet-stop-leak 

： JVM  `attach -> telnet  -> stop -> jmap`， `com.akshita.jad.agent.JADClassloader` 。

## 

- JDK（ `java`、`jmap`）
- `expect`、`telnet`
- Maven（ `packaging/target/jad-bin`）

## 

1. ：

```bash
mvn -V -ntp -pl packaging -am package -DskipTests
```

2. （； `--work-dir` ）：

`threshold` ，JVM JADClassLoader 。

```bash
python3 integration-test/telnet-stop-leak/run_telnet_stop_leak_test.py \
  --iterations 10 \
  --warmup 2 \
  --threshold 3 \
  --work-dir integration-test/telnet-stop-leak/work
```

：

- `results.csv`： `JADClassloader` 
- `logs/`： JVM、attach、telnet transcript、jmap 

## 

- ：`integration-test/telnet-stop-leak/commands.txt`
- telnet ：`integration-test/telnet-stop-leak/jad_telnet.exp`

