# 

、， JAD  `Advice` 。



```java
public class Advice {

    private final ClassLoader loader;
    private final Class<?> clazz;
    private final JADMethod method;
    private final Object target;
    private final Object[] params;
    private final Object returnObj;
    private final Throwable throwExp;
    private final boolean isBefore;
    private final boolean isThrow;
    private final boolean isReturn;

    // getter/setter
}
```



|     |                                                                                                                                                                              |
| --------: | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|    loader |  ClassLoader                                                                                                                                                         |
|     clazz |  Class                                                                                                                                                               |
|    method |                                                                                                                                                                  |
|    target |                                                                                                                                                                      |
|    params | ，，                                                                                                                         |
| returnObj | 。 `isReturn==true` ，。 `void`， null                                             |
|  throwExp | 。 `isThrow==true` ，。                                                                                        |
|  isBefore | ，， `isBefore==true` ， `isThrow==false`  `isReturn==false`，，。 |
|   isThrow | ，。                                                                                                                                     |
|  isReturn | ，。                                                                                                                                   |

， OGNL ，；````

- ：[https://github.com/akshita-sahu/jad/issues/71](https://github.com/akshita-sahu/jad/issues/71)
- OGNL ：[https://commons.apache.org/dormant/commons-ognl/language-guide.html](https://commons.apache.org/dormant/commons-ognl/language-guide.html)
