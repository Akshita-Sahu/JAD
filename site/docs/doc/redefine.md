# redefine

::: tip
 [retransform](retransform.md) 
:::

[`mc-redefine`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=command-mc-redefine)

::: tip
`.class`，redefine jvm 。
:::

：[Instrumentation#redefineClasses](https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/Instrumentation.html#redefineClasses-java.lang.instrument.ClassDefinition...-)

## 

::: tip
 [retransform](retransform.md) 
:::

- redefine  class 、、 field  method，、

-  mc ， class ，， redefine  class

-  redefine  watch/trace/jad/tt ， redefine 

::: warning
， redefine ，redefine （ field）， jdk 。
:::

::: tip
`reset``redefine`。，`redefine`。
:::

::: tip
`redefine``jad`/`watch`/`trace`/`monitor`/`tt`。`redefine`，，`redefine`。
 jdk  redefine  Retransform ，，。
:::

## 

|               |                                    |
| --------------------: | :----------------------------------------- |
|                  [c:] | ClassLoader  hashcode                    |
| `[classLoaderClass:]` |  ClassLoader  class name |

## 

```bash
   redefine /tmp/Test.class
   redefine -c 327a647b /tmp/Test.class /tmp/Test\$Inner.class
   redefine --classLoaderClass sun.misc.Launcher$AppClassLoader /tmp/Test.class /tmp/Test\$Inner.class
```

##  jad/mc 

```bash
jad --source-only com.example.demo.jad.user.UserController > /tmp/UserController.java

mc /tmp/UserController.java -d /tmp

redefine /tmp/com/example/demo/jad/user/UserController.class
```

- jad ，， vim 
- mc 
-  redefine 

##  .class 

`mc``jad`。，。，`base64`。

1. `.class` base64， result.txt

   ```bash
   base64 < Test.class > result.txt
   ```

2. ，`result.txt`，，

3.  `result.txt``.class`

   ```
   base64 -d < result.txt > Test.class
   ```

4.  md5 ，

## redefine 

-  field/method
- ，，`System.out.println`，`run()`

```java
public class MathGame {
    public static void main(String[] args) throws InterruptedException {
        MathGame game = new MathGame();
        while (true) {
            game.run();
            TimeUnit.SECONDS.sleep(1);
            // ， while
            System.out.println("in loop");
        }
    }

    public void run() throws InterruptedException {
        // ，run()
        System.out.println("call run()");
        try {
            int number = random.nextInt();
            List<Integer> primeFactors = primeFactors(number);
            print(number, primeFactors);

        } catch (Exception e) {
            System.out.println(String.format("illegalArgumentCount:%3d, ", illegalArgumentCount) + e.getMessage());
        }
    }
}
```
