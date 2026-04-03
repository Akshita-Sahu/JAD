# retransform

[`mc-retransform`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=command-mc-retransform)

::: tip
`.class`，retransform jvm 。
:::

：[Instrumentation#retransformClasses](https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/Instrumentation.html#retransformClasses-java.lang.Class...-)

## 

```bash
   retransform /tmp/Test.class
   retransform -l
   retransform -d 1                    # delete retransform entry
   retransform --deleteAll             # delete all retransform entries
   retransform --classPattern demo.*   # triger retransform classes
   retransform -c 327a647b /tmp/Test.class /tmp/Test\$Inner.class
   retransform --classLoaderClass 'sun.misc.Launcher$AppClassLoader' /tmp/Test.class
```

## retransform  .class 

```bash
$ retransform /tmp/MathGame.class
retransform success, size: 1, classes:
demo.MathGame
```

 .class ， class name， retransform jvm 。 `.class` ， retransform entry.

::: tip
 retransform  class ， retransform entry.
:::

##  retransform entry

```bash
$ retransform -l
Id              ClassName       TransformCount  LoaderHash      LoaderClassName
1               demo.MathGame   1               null            null
```

- TransformCount  ClassFileTransformer#transform  entry  .class ， transform 。

##  retransform entry

 id：

```bash
retransform -d 1
```

##  retransform entry

```bash
retransform --deleteAll
```

##  retransform

```bash
$ retransform --classPattern demo.MathGame
retransform success, size: 1, classes:
demo.MathGame
```

> ：， retransform entry ， retransform ， entry (id )。

##  retransform 

 retransform ，，：

-  retransform entry
-  retransform

::: tip
 retransform entry， retransform ， jad stop ，retransform 。
:::

##  jad/mc 

```bash
jad --source-only com.example.demo.jad.user.UserController > /tmp/UserController.java

mc /tmp/UserController.java -d /tmp

retransform /tmp/com/example/demo/jad/user/UserController.class
```

- jad ，， vim 
- mc 
-  retransform 

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

## retransform 

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
