# groovy

::: tip
JAD  groovy ， BTrace ， groovy  if/for/switch/while ，， BTrace 。
:::

### 

1. ， watch ，。
1.  before/success/exception/finish 。

### 

|           |                              |
| ----------------: | :----------------------------------- |
|   _class-pattern_ |                        |
|  _method-pattern_ |                      |
| _script-filepath_ | groovy                 |
|               [S] |                        |
|               [E] | ， |

，， `/tmp/test.groovy`，， `./test.groovy`

### 

```java
/**
 * 
 */
interface ScriptListener {

    /**
     * 
     *
     * @param output 
     */
    void create(Output output);

    /**
     * 
     *
     * @param output 
     */
    void destroy(Output output);

    /**
     * 
     *
     * @param output 
     * @param advice 
     */
    void before(Output output, Advice advice);

    /**
     * 
     *
     * @param output 
     * @param advice 
     */
    void afterReturning(Output output, Advice advice);

    /**
     * 
     *
     * @param output 
     * @param advice 
     */
    void afterThrowing(Output output, Advice advice);

}
```

###  `Advice` 

`Advice` 。[](advice-class.md)。

###  `Output` 

`Output` ，

```java
/**
 * 
 */
interface Output {

    /**
     * ()
     *
     * @param string 
     * @return this
     */
    Output print(String string);

    /**
     * ()
     *
     * @param string 
     * @return this
     */
    Output println(String string);

    /**
     * 
     *
     * @return this
     */
    Output finish();

}
```

###  groovy 

```groovy
import com.akshita.jad.core.command.ScriptSupportCommand
import com.akshita.jad.core.util.Advice

import static java.lang.String.format

/**
 * 
 */
public class Logger implements ScriptSupportCommand.ScriptListener {

    @Override
    void create(ScriptSupportCommand.Output output) {
        output.println("script create.");
    }

    @Override
    void destroy(ScriptSupportCommand.Output output) {
        output.println("script destroy.");
    }

    @Override
    void before(ScriptSupportCommand.Output output, Advice advice) {
        output.println(format("before:class=%s;method=%s;paramslen=%d;%s;",
                advice.getClazz().getSimpleName(),
                advice.getMethod().getName(),
                advice.getParams().length, advice.getParams()))
    }

    @Override
    void afterReturning(ScriptSupportCommand.Output output, Advice advice) {
        output.println(format("returning:class=%s;method=%s;",
                advice.getClazz().getSimpleName(),
                advice.getMethod().getName()))
    }

    @Override
    void afterThrowing(ScriptSupportCommand.Output output, Advice advice) {
        output.println(format("throwing:class=%s;method=%s;",
                advice.getClazz().getSimpleName(),
                advice.getMethod().getName()))
    }
}
```

：

```
$ groovy com.akshita-sahu.sample.petstore.dal.dao.ProductDao getProductById /Users/zhuyong/middleware/jad/scripts/Logger.groovy -S
script create.
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 102 ms.
before:class=IbatisProductDao;method=getProductById;paramslen=1;[Ljava.lang.Object;@45df64fc;
returning:class=IbatisProductDao;method=getProductById;
before:class=IbatisProductDao;method=getProductById;paramslen=1;[Ljava.lang.Object;@5b0e2d00;
returning:class=IbatisProductDao;method=getProductById;
```
