# stack

[`stack`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-stack)

::: tip

:::

，，， stack 。

## 

|             |                                                            |
| ------------------: | :----------------------------------------------------------------- |
|     _class-pattern_ |                                                      |
|    _method-pattern_ |                                                    |
| _condition-express_ |                                                          |
|                 [E] | ，                               |
|              `[n:]` |                                                        |
|              `[c:]` |  classloader hash， classloader                |
|         `[m <arg>]` |  Class ， 50。`[maxMatch <arg>]`。 |

， ognl ，`"{params,returnObj}"`， ognl ，。

， `advice` 。`Advice` 。

[](advice-class.md)。

- ：[https://github.com/akshita-sahu/jad/issues/71](https://github.com/akshita-sahu/jad/issues/71)
- OGNL ：[https://commons.apache.org/dormant/commons-ognl/language-guide.html](https://commons.apache.org/dormant/commons-ognl/language-guide.html)

## 

###  Demo

[](quick-start.md)`math-game`。

### stack

```bash
$ stack demo.MathGame primeFactors
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 36 ms.
ts=2018-12-04 01:32:19;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)
```

###  Class 

```bash
$ stack demo.MathGame primeFactors -m 1
Press Q or Ctrl+C to abort.
Affect(class count:1 , method count:1) cost in 561 ms, listenerId: 5.
ts=2022-12-25 21:07:07;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@b4aac2
    @demo.MathGame.primeFactors()
        at demo.MathGame.run(MathGame.java:46)
        at demo.MathGame.main(MathGame.java:38)
```

###  ClassLoader 

 classloader ， `sc -d`  classloader hash， `-c`  classloader：

```bash
sc -d com.example.Foo
stack -c 3d4eac69 com.example.Foo bar
```

### 

```bash
$ stack demo.MathGame primeFactors 'params[0]<0' -n 2
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 30 ms.
ts=2018-12-04 01:34:27;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)

ts=2018-12-04 01:34:30;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)

Command execution times exceed limit: 2, so command will exit. You can set it with -n option.
```

### 

```bash
$ stack demo.MathGame primeFactors '#cost>5'
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 35 ms.
ts=2018-12-04 01:35:58;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@3d4eac69
    @demo.MathGame.run()
        at demo.MathGame.main(MathGame.java:16)
```
