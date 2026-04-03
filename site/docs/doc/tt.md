# tt

[`tt`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-tt)

::: tip
，，
:::

`watch` ，，，，。

、。

，TimeTunnel 。

## 

- tt ：/，`Map<Integer, TimeFragment>`， 100。
- tt ，，OOM。 jad  tt  map。

## 

###  Demo

[](quick-start.md)`math-game`。

### 

，。

```bash
$ tt -t demo.MathGame primeFactors
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 66 ms.
 INDEX   TIMESTAMP            COST(ms)  IS-RET  IS-EXP   OBJECT         CLASS                          METHOD
-------------------------------------------------------------------------------------------------------------------------------------
 1000    2018-12-04 11:15:38  1.096236  false   true     0x4b67cf4d     MathGame                       primeFactors
 1001    2018-12-04 11:15:39  0.191848  false   true     0x4b67cf4d     MathGame                       primeFactors
 1002    2018-12-04 11:15:40  0.069523  false   true     0x4b67cf4d     MathGame                       primeFactors
 1003    2018-12-04 11:15:41  0.186073  false   true     0x4b67cf4d     MathGame                       primeFactors
 1004    2018-12-04 11:15:42  17.76437  true    false    0x4b67cf4d     MathGame                       primeFactors
```

###  Class 

```bash
$ tt -t -m 1 demo.MathGame primeFactors
Press Q or Ctrl+C to abort.
Affect(class count:1 , method count:1) cost in 130 ms, listenerId: 1.
 INDEX   TIMESTAMP            COST(ms)  IS-RET  IS-EXP   OBJECT         CLASS                          METHOD
-------------------------------------------------------------------------------------------------------------------------------------
 1000    2022-12-25 19:41:45  2.629929  true    false    0x3bf400       MathGame                       primeFactors
 1001    2022-12-25 19:41:55  0.146161  false   true     0x3bf400       MathGame                       primeFactors
```

- 
  - `-t`

    tt ，`-t` 。 `demo.MathGame`  `primeFactors` 。

  - `-n 3`

     `CTRL+C`  tt ，， JVM 。

     `-n` ， JAD  tt ，。

  - `-m 1`

     `-m`  Class ， Class  JVM ， 50。

  - `-c <classloader hash>`

     classloader ， `-c`  classloader 。 `sc -d className`  classloader hash。

- 

|   |                                                                                                                           |
| --------- | --------------------------------------------------------------------------------------------------------------------------------- |
| INDEX     | ，， tt ，。                            |
| TIMESTAMP | ，                                                                            |
| COST(ms)  |                                                                                                                     |
| IS-RET    |                                                                                                       |
| IS-EXP    |                                                                                                         |
| OBJECT    | `hashCode()`，， JVM ，。 |
| CLASS     |                                                                                                                         |
| METHOD    |                                                                                                                       |

- 

  
  - JAD 
  - ， tt 

   `OGNL` ， `Advice` 。 `tt` ，`watch`、`trace`、`stack` 。

- 

  `tt -t *Test print params.length==1`

  ，，

  `tt -t *Test print 'params[1] instanceof Integer'`

- 

  `tt -t *Test print params[0].mobile=="13989838402"`

-  `Advice` 

  ， `params[0]`，，[](advice-class.md)

### 

 `tt` ，，。



```bash
$ tt -l
 INDEX   TIMESTAMP            COST(ms)  IS-RET  IS-EXP   OBJECT         CLASS                          METHOD
-------------------------------------------------------------------------------------------------------------------------------------
 1000    2018-12-04 11:15:38  1.096236  false   true     0x4b67cf4d     MathGame                       primeFactors
 1001    2018-12-04 11:15:39  0.191848  false   true     0x4b67cf4d     MathGame                       primeFactors
 1002    2018-12-04 11:15:40  0.069523  false   true     0x4b67cf4d     MathGame                       primeFactors
 1003    2018-12-04 11:15:41  0.186073  false   true     0x4b67cf4d     MathGame                       primeFactors
 1004    2018-12-04 11:15:42  17.76437  true    false    0x4b67cf4d     MathGame                       primeFactors
                              9
 1005    2018-12-04 11:15:43  0.4776    false   true     0x4b67cf4d     MathGame                       primeFactors
Affect(row-cnt:6) cost in 4 ms.
```

 `primeFactors` 

```bash
$ tt -s 'method.name=="primeFactors"'
 INDEX   TIMESTAMP            COST(ms)  IS-RET  IS-EXP   OBJECT         CLASS                          METHOD
-------------------------------------------------------------------------------------------------------------------------------------
 1000    2018-12-04 11:15:38  1.096236  false   true     0x4b67cf4d     MathGame                       primeFactors
 1001    2018-12-04 11:15:39  0.191848  false   true     0x4b67cf4d     MathGame                       primeFactors
 1002    2018-12-04 11:15:40  0.069523  false   true     0x4b67cf4d     MathGame                       primeFactors
 1003    2018-12-04 11:15:41  0.186073  false   true     0x4b67cf4d     MathGame                       primeFactors
 1004    2018-12-04 11:15:42  17.76437  true    false    0x4b67cf4d     MathGame                       primeFactors
                              9
 1005    2018-12-04 11:15:43  0.4776    false   true     0x4b67cf4d     MathGame                       primeFactors
Affect(row-cnt:6) cost in 607 ms.
```

 `-s` 。<span style="color:red;">， `Advice` 。</span>

### 

， `-i`  `INDEX` 。

```bash
$ tt -i 1003
 INDEX            1003
 GMT-CREATE       2018-12-04 11:15:41
 COST(ms)         0.186073
 OBJECT           0x4b67cf4d
 CLASS            demo.MathGame
 METHOD           primeFactors
 IS-RETURN        false
 IS-EXCEPTION     true
 PARAMETERS[0]    @Integer[-564322413]
 THROW-EXCEPTION  java.lang.IllegalArgumentException: number is: -564322413, need >= 2
                      at demo.MathGame.primeFactors(MathGame.java:46)
                      at demo.MathGame.run(MathGame.java:24)
                      at demo.MathGame.main(MathGame.java:16)

Affect(row-cnt:1) cost in 11 ms.
```

### 

，，。，。

`tt` ， `INDEX` ，。 `-p` 。 `--replay-times` 
， `--replay-interval` ( ms,  1000ms)

```bash
$ tt -i 1004 -p
 RE-INDEX       1004
 GMT-REPLAY     2018-12-04 11:26:00
 OBJECT         0x4b67cf4d
 CLASS          demo.MathGame
 METHOD         primeFactors
 PARAMETERS[0]  @Integer[946738738]
 IS-RETURN      true
 IS-EXCEPTION   false
 COST(ms)         0.186073
 RETURN-OBJ     @ArrayList[
                    @Integer[2],
                    @Integer[11],
                    @Integer[17],
                    @Integer[2531387],
                ]
Time fragment[1004] successfully replayed.
Affect(row-cnt:1) cost in 14 ms.
```

，， JAD 。

### 

`-w, --watch-express` `ognl` 

- [](advice-class.md)。

```bash
[jad@10718]$ tt -t demo.MathGame run -n 5
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 56 ms, listenerId: 1
 INDEX      TIMESTAMP                   COST(ms)     IS-RET     IS-EXP      OBJECT              CLASS                                     METHOD
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 1000       2021-01-08 21:54:17         0.901091     true       false       0x7699a589          MathGame                                  run
[jad@10718]$ tt -w 'target.illegalArgumentCount'  -x 1 -i 1000
@Integer[60]
Affect(row-cnt:1) cost in 7 ms.
```

- 、

```bash
[jad@10718]$ tt -t demo.MathGame run -n 5
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 56 ms, listenerId: 1
 INDEX      TIMESTAMP                   COST(ms)     IS-RET     IS-EXP      OBJECT              CLASS                                     METHOD
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 1000       2021-01-08 21:54:17         0.901091     true       false       0x7699a589          MathGame                                  run
[jad@10718]$ tt -w '@demo.MathGame@random.nextInt(100)'  -x 1 -i 1000
@Integer[46]
```

 `com.akshita.jad.core.advisor.Advice#getLoader`,`classloader` [ognl](ognl.md)。

 [ spring context  bean ](https://github.com/akshita-sahu/jad/issues/482)

- 
  1. **ThreadLocal **

      ThreadLocal ，， ThreadLocal  JAD ，。

      CASE ： TraceId 。

  2. ****

     ，`tt` ，。，， `tt` 。 `watch` 。

###  tt 

```
tt -d -i 1001
```

###  tt 

```
tt --delete-all
```
