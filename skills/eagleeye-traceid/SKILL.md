---
name: jad-eagleeye-traceid
description:  JAD  watch/trace  EagleEye traceId /  traceId
---

#  EagleEye traceId（JAD）

：， EagleEye `traceId`，//。

：
- EagleEye  traceId  `EagleEye.getTraceId()` 。
- JAD  `watch`  OGNL，：`@com.akshita-sahu.eagleeye.EagleEye@getTraceId()`
- JAD  `trace`  `trace_id=...`（ EagleEye）。

## （）

1)  EagleEye ：

```bash
sc -d com.akshita-sahu.eagleeye.EagleEye
```

：
-  EagleEye、 relocate/shade（/）。

2) “”：
- ：Controller 、RPC Provider 、Filter/Interceptor  doFilter/preHandle 。
- ：（、）。

##  A： watch  traceId（）

### A1)  traceId（）

```bash
watch <> <> '@com.akshita-sahu.eagleeye.EagleEye@getTraceId()' -n 5
```

：
- `@@()`  OGNL 。
- `-n 5` ，（/）。

### A2)  + traceId（）

```bash
watch <> <> '{params, @com.akshita-sahu.eagleeye.EagleEye@getTraceId()}' -n 5 -x 2
```

：
- `{...}` /。
- `params`  JAD watch ；`-x 2` （/）。

（）：
- （ after，）：
  -  traceId ， `-b`（before）；。

##  B： trace  traceId（）

```bash
trace <> <> -n 5
```

：
- `trace`  `trace_id=<xxxx>` 。
- ，“ traceId + ”。

## （）

/：
- （/）：`<>#<>`
- ：`watch` / `trace`
- ：
  - `traceId=<...>`（ params ）
- ：
  -  traceId /
  - （ DAO/RPC ） trace/watch

## 

-  traceId、 ThreadLocal ， `watch`  OGNL 。

