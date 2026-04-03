# sc

[`sc`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=command-sc)

::: tip
 JVM 
:::

“Search-Class” ， JVM  Class ， `[d]`、`[E]`、`[f]`  `[x:]`。

## 

|               |                                                                                                                                               |
| --------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------- |
|       _class-pattern_ |                                                                                                                                         |
|      _method-pattern_ |                                                                                                                                       |
|                   [d] | ，、、 ClassLoader 。<br/> ClassLoader ， |
|                   [E] | ，                                                                                                                  |
|                   [f] | （-d ）                                                                                                   |
|                  [x:] | ， 0， `toString`                                                                                 |
|                `[c:]` |  class  ClassLoader  hashcode                                                                                                                 |
| `[classLoaderClass:]` |  ClassLoader  class name                                                                                                            |
|                `[n:]` | （ 100）                                                                                                          |
|          `[cs <arg>]` |  class  ClassLoader#toString() 。`[classLoaderStr <arg>]`                                                                           |

::: tip
class-pattern ， com.akshita-sahu.test.AAA， com/akshita-sahu/test/AAA ，，，`/``.`。
:::

::: tip
sc ，，，`options disable-sub-class true`
:::

## 

- 

  ```bash
  $ sc demo.*
  demo.MathGame
  Affect(row-cnt:1) cost in 55 ms.
  ```

- 

  ```bash
  $ sc -d demo.MathGame
  class-info        demo.MathGame
  code-source       /private/tmp/math-game.jar
  name              demo.MathGame
  isInterface       false
  isAnnotation      false
  isEnum            false
  isAnonymousClass  false
  isArray           false
  isLocalClass      false
  isMemberClass     false
  isPrimitive       false
  isSynthetic       false
  simple-name       MathGame
  modifier          public
  annotation
  interfaces
  super-class       +-java.lang.Object
  class-loader      +-sun.misc.Launcher$AppClassLoader@3d4eac69
                      +-sun.misc.Launcher$ExtClassLoader@66350f69
  classLoaderHash   3d4eac69

  Affect(row-cnt:1) cost in 875 ms.
  ```

-  Field 

  ```bash
  $ sc -d -f demo.MathGame
  class-info        demo.MathGame
  code-source       /private/tmp/math-game.jar
  name              demo.MathGame
  isInterface       false
  isAnnotation      false
  isEnum            false
  isAnonymousClass  false
  isArray           false
  isLocalClass      false
  isMemberClass     false
  isPrimitive       false
  isSynthetic       false
  simple-name       MathGame
  modifier          public
  annotation
  interfaces
  super-class       +-java.lang.Object
  class-loader      +-sun.misc.Launcher$AppClassLoader@3d4eac69
                      +-sun.misc.Launcher$ExtClassLoader@66350f69
  classLoaderHash   3d4eac69
  fields            modifierprivate,static
                    type    java.util.Random
                    name    random
                    value   java.util.Random@522b4
                            08a

                    modifierprivate
                    type    int
                    name    illegalArgumentCount


  Affect(row-cnt:1) cost in 19 ms.
  ```

-  ClassLoader#toString （： toString()`apo`，`demo.MathGame`, `demo.MyBar`,` demo.MyFoo`3 ）

  ```bash
  $ sc -cs apo *demo*
  demo.MathGame
  demo.MyBar
  demo.MyFoo
  Affect(row-cnt:3) cost in 56 ms.
  ```
