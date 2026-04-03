---
name: jad-springcontext-issues-resolve
description:  Spring ApplicationContext / Bean / 
---

# Spring Context / Bean 

：
- ****（contains/beanNames/type/environment）， `getBean()`  Bean 。
- ****：`vmtool -l` ； `getBeanDefinitionNames()`。


## 1)  ApplicationContext

 Spring Boot Context（ `AbstractApplicationContext` ）：

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 5
```

，： org.springframework.context.ApplicationContext

， classloader  Class<?> name 。

1.  ClassLoader  `LaunchedURLClassLoader`
2.  ClassLoader  com.akshita-sahu.pandora.service.loader.ModuleClassLoader

## 2) 

（：`server.port`）：

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express 'instances[0].getEnvironment().getProperty("server.port")'
```

“”

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express '#env=instances[0].getEnvironment(), #ps=#env.getPropertySources().get("configurationProperties"), #ps.findConfigurationProperty("server.port")'
```

 spring-boot-starter-actuator ，

```bash
vmtool --action getInstances \
--className org.springframework.boot.actuate.env.EnvironmentEndpoint \
--express 'instances[0].environmentEntry("server.port")'
```

## 3)  Bean Name （）

 beanName  `fooService`：

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express 'instances[0].containsBean("fooService")'
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express 'instances[0].containsLocalBean("fooService")'
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express 'instances[0].containsBeanDefinition("fooService")'
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express 'instances[0].getAliases("fooService")'
```

：
- `containsBean=true`  `containsLocalBean=false`：Bean ** Context**。
- `containsBean=false` ：** Context**、`@Profile/@Conditional`、/。

## 4)  Spring Context “” Bean（，）

（ `order` / `datasource`） beanName ：

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express '#ctx=instances[0], #names=@java.util.Arrays@asList(#ctx.getBeanDefinitionNames()), #m=#names.{? #this.toLowerCase().contains("order")}, #m.subList(0, @java.lang.Math@min(#m.size(), 50))'
```

：
- ， 50 ； beanName  3 。

## 5)  Bean（“”）

（/）（：`com.foo.OrderService`）：

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express 'instances[0].getBeanNamesForType(@com.foo.OrderService@class)'
```

（`NoUniqueBeanDefinitionException` ），：

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express 'instances[0].getBeansOfType(@com.foo.OrderService@class).keySet()'
```

：
- （JDK Proxy / CGLIB），****，。
-  `@com.foo.OrderService@class`  `ClassNotFound`，****： `classloader`（stats/instances/tree） `classLoaderHash`， `vmtool/ognl`  `--classLoader <hash>` 。 `classloader` Class Name， `--classLoaderClass` 。

## 6)  BeanDefinition（//）

 Bean （`@Bean` ？XML？？）， `BeanFactory`  `BeanDefinition`（：Context  `AbstractApplicationContext`）。

```bash
vmtool --action getInstances --className org.springframework.context.support.AbstractApplicationContext -l 1 --express '#ctx=instances[0], #bf=#ctx.getBeanFactory(), #bd=#bf.getBeanDefinition("fooService")'
```