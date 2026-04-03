# options

[`options`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-options)

::: tip

:::

|                    |    |                                                                                                                                                        |
| ---------------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| unsafe                 | false    | ， JVM ，！                                                                                |
| dump                   | false    |  dump ，，class  dump `/${application working dir}/jad-class-dump/`， |
| batch-re-transform     | true     |  retransform                                                                                                               |
| json-format            | false    |  json                                                                                                                                      |
| object-size-limit      | 10485760 | ObjectView （）， 0， `10 * 1024 * 1024`                                                                                       |
| disable-sub-class      | false    | ，，，                                                                 |
| support-default-method | true     |  default method，  interface， default method。 [#1105](https://github.com/akshita-sahu/jad/issues/1105)               |
| save-result            | false    | ，`~/logs/jad-cache/result.log`                                                         |
| job-timeout            | 1d       | ，，； 1d, 2h, 3m, 25s，、、、                                                 |
| print-parent-fields    | true     |  parent class  filed                                                                                                                         |
| verbose                | false    |                                                                                                                                        |
| strict                 | true     |  strict                                                                                                                                        |

##  options

```bash
$ options
 LEVEL  TYPE    NAME          VALUE   SUMMARY               DESCRIPTION
-------------------------------------------------------------------------------------------------------
 0      boolea  unsafe        false   Option to support sy  This option enables to proxy functionality
        n                             stem-level class       of JVM classes. Due to serious security r
                                                            isk a JVM crash is possibly be introduced.
                                                             Do not activate it unless you are able to
                                                             manage.
 1      boolea  dump          false   Option to dump the e  This option enables the enhanced classes t
        n                             nhanced classes       o be dumped to external file for further d
                                                            e-compilation and analysis.
 1      boolea  batch-re-tra  true    Option to support ba  This options enables to reTransform classe
        n       nsform                tch reTransform Clas  s with batch mode.
                                      s
 2      boolea  json-format   false   Option to support JS  This option enables to format object outpu
        n                             ON format of object   t with JSON when -x option selected.
                                      output
 1      int     object-size-  10485760 Option to control O  Upper size limit in bytes for ObjectView o
         limit                          bjectView output    utput, must be greater than 0. Default val
                                                             ue is 10 * 1024 * 1024.
 1      boolea  disable-sub-  false   Option to control in  This option disable to include sub class w
        n       class                 clude sub class when  hen matching class.
                                       class matching
 1      boolea  support-defa  true    Option to control in  This option disable to include default met
        n       ult-method            clude default method  hod in interface when matching class.
                                       in interface when c
                                      lass matching
 1      boolea  save-result   false   Option to print comm  This option enables to save each command's
        n                             and's result to log    result to log file, which path is ${user.
                                      file                  home}/logs/jad-cache/result.log.
 2      String  job-timeout   1d      Option to job timeou  This option setting job timeout,The unit c
                                      t                     an be d, h, m, s for day, hour, minute, se
                                                            cond. 1d is one day in default
 1      boolea  print-parent  true    Option to print all   This option enables print files in parent
        n       -fields               fileds in parent cla  class, default value true.
                                      ss
 1      boolea  verbose       false   Option to print verb  This option enables print verbose informat
        n                             ose information       ion, default value false.
 1      boolea  strict        true    Option to strict mod  By default, strict mode is true, not allow
        n                             e                     ed to set object properties. Want to set o
                                                            bject properties, execute `options strict
                                                            false`
```

##  option 

```
$ options json-format
 LEVEL  TYPE  NAME         VALUE  SUMMARY             DESCRIPTION
--------------------------------------------------------------------------------------------
 2      bool  json-format  false  Option to support   This option enables to format object
        ean                       JSON format of obj  output with JSON when -x option selec
                                  ect output          ted.
```

::: tip
`json-format` false，`watch`/`tt` json ，`json-format` true。
:::

##  option

，，：

```
$ options save-result true
 NAME         BEFORE-VALUE  AFTER-VALUE
----------------------------------------
 save-result  false         true
```

##  unsafe ， jdk package 

，`watch`/`trace`/`tt`/`trace`/`monitor``java.*` package 。`unsafe` true，。

```bash
$ options unsafe true
 NAME    BEFORE-VALUE  AFTER-VALUE
-----------------------------------
 unsafe  false         true
```

```bash
$ watch java.lang.invoke.Invokers callSiteForm
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 61 ms, listenerId: 1
```

##  strict ， ognl 

::: tip
since 3.6.0
:::

， ognl ，。

`Student`， 18 ，`target.age=18`，`age` 18 。`target.age==18`。

，JAD `strict`，`ognl`， Property `setter`。

`MathGame`，。

```
$ watch demo.MathGame primeFactors 'target' 'target.illegalArgumentCount=1'
Press Q or Ctrl+C to abort.
Affect(class count: 1 , method count: 1) cost in 206 ms, listenerId: 1
watch failed, condition is: target.illegalArgumentCount=1, express is: target, By default, strict mode is true, not allowed to set object properties. Want to set object properties, execute `options strict false`, visit /Users/admin/logs/jad/jad.log for more details.
```

`ognl`，`options strict false`，`strict`。

- ： https://github.com/akshita-sahu/jad/issues/2128
