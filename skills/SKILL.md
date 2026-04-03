---
name: jad
description: jad  java，jvm skill
---

# JAD  Skill

## Overview

JAD  Java ， Skill 。，。

**：**
- 、，。
-  `watch` / `trace` / `tt` / `stack` **** `-n`（），。
- （），。

---

## 

### 1. CPU 

**：** `cpu-high/SKILL.md`

： CPU 、、。

：
1. `dashboard`  CPU /  / GC 
2. `thread`（topN）
3. （CPU  /  / GC ）
4.  `stack` / `trace` / `watch` 
5. （、、、）

---

### 2.  EagleEye traceId

**：** `eagleeye-traceid/SKILL.md`

：， EagleEye traceId， / 。

：
1. `sc -d com.akshita-sahu.eagleeye.EagleEye` 
2. （Controller / RPC Provider / Filter ）
3. ** A**：`watch` + OGNL  `@com.akshita-sahu.eagleeye.EagleEye@getTraceId()`  traceId
4. ** B**：`trace`  traceId，
5.  traceId  / 

---

### 3. Spring Context / Bean 

**：** `spring-context/SKILL.md`

： Spring ApplicationContext / Bean / 。

：
1. `vmtool --action getInstances`  `AbstractApplicationContext` （ ClassLoader  Context）
2. （`getEnvironment().getProperty(...)` / `findConfigurationProperty(...)`）
3. `containsBean` / `containsLocalBean` / `containsBeanDefinition`  Bean （）
4.  Bean（`getBeanDefinitionNames` + OGNL ）
5.  Bean（`getBeanNamesForType` / `getBeansOfType`）
6.  BeanDefinition  Bean （`@Bean`  / XML / ）

**：** ， `getBean()`  Bean ； `ClassNotFound`，， `classloader`  `classLoaderHash`。
