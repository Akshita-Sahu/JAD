# Http API

[`Http API`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=case-http-api)

## 

Http API
 RESTful ， JSON 。 Telnet/WebConsole ，Http
API ，，。

### 

Http API ：`http://ip:port/api`， POST 。 POST
`http://127.0.0.1:8563/api` 。

：telnet  3658  Chrome ， http  8563  http 。

### 

```json
{
  "action": "exec",
  "requestId": "req112",
  "sessionId": "94766d3c-8b39-42d3-8596-98aee3ccbefb",
  "consumerId": "955dbd1325334a84972b0f3ac19de4f7_2",
  "command": "version",
  "execTimeout": "10000"
}
```

：

- `action` : /，" Action"。
- `requestId` :  ID，。
- `sessionId` : JAD  ID， ID。
- `consumerId` : JAD  ID，。
- `command` : JAD command line 。
- `execTimeout` : (ms)， 30000。

:  action ， action 。

###  Action

 Action ：

- `exec` : ，。
- `async_exec` : ，，`pull_results`。
- `interrupt_job` : ， Telnet `Ctrl + c`。
- `pull_results` : ， http （long-polling）
- `init_session` : 
- `join_session` : ， JAD 
- `close_session` : 

### 

 state ，：

- `SCHEDULED`： job ，；
- `SUCCEEDED`：（）；
- `FAILED`：（）， message ；
- `REFUSED`：（）， message ；

## 

，。，`sessionId`。

```json
{
  "action": "exec",
  "command": "<JAD command line>"
}
```

 JAD ：

```bash
curl -Ss -XPOST http://localhost:8563/api -d '
{
  "action":"exec",
  "command":"version"
}
'
```

：

```json
{
  "state": "SUCCEEDED",
  "sessionId": "ee3bc004-4586-43de-bac0-b69d6db7a869",
  "body": {
    "results": [
      {
        "type": "version",
        "version": "3.3.7",
        "jobId": 5
      },
      {
        "jobId": 5,
        "statusCode": 0,
        "type": "status"
      }
    ],
    "timeExpired": false,
    "command": "version",
    "jobStatus": "TERMINATED",
    "jobId": 5
  }
}
```

：

- `state`: ，“”
- `sessionId `: JAD  ID，
- `body.jobId`:  ID， Result  jobId
- `body.jobStatus`: ，`TERMINATED `
- `body.timeExpired`: 
- `body/results`: 

****

```json
[
  {
    "type": "version",
    "version": "3.3.7",
    "jobId": 5
  },
  {
    "jobId": 5,
    "statusCode": 0,
    "type": "status"
  }
]
```

- `type` : ，`status`， JAD 。"[](#special_command_results)"。
- `jobId` :  ID。
- 。

： watch/trace ，，。"[watch  map ](#change_watch_value_to_map)"。

：

- `execTimeout`，，。
- `-n`。
-  condition-express ， watch/trace `-n 1`。

## 

 JAD ，。：

- 
- (）
- 
- 
- 
- 

### 

```bash
curl -Ss -XPOST http://localhost:8563/api -d '
{
  "action":"init_session"
}
'
```

：

```json
{
  "sessionId": "b09f1353-202c-407b-af24-701b744f971e",
  "consumerId": "5ae4e5fbab8b4e529ac404f260d4e2d1_1",
  "state": "SUCCEEDED"
}
```

 ID ： `b09f1353-202c-407b-af24-701b744f971e`，  ID ：`5ae4e5fbab8b4e529ac404f260d4e2d1_1 `。

### 

 ID， ID。。。

```bash
curl -Ss -XPOST http://localhost:8563/api -d '
{
  "action":"join_session",
  "sessionId" : "b09f1353-202c-407b-af24-701b744f971e"
}
'
```

：

```json
{
  "consumerId": "8f7f6ad7bc2d4cb5aa57a530927a95cc_2",
  "sessionId": "b09f1353-202c-407b-af24-701b744f971e",
  "state": "SUCCEEDED"
}
```

 ID `8f7f6ad7bc2d4cb5aa57a530927a95cc_2 ` 。

### 

 action `pull_results`。 Http long-polling ，。
 5 ，`join_session`。，，。

 ID  ID:

```bash
curl -Ss -XPOST http://localhost:8563/api -d '
{
  "action":"pull_results",
  "sessionId" : "b09f1353-202c-407b-af24-701b744f971e",
  "consumerId" : "8f7f6ad7bc2d4cb5aa57a530927a95cc_2"
}
'
```

 Bash :

```bash
while true; do curl -Ss -XPOST http://localhost:8563/api -d '
{
  "action":"pull_results",
  "sessionId" : "2b085b5d-883b-4914-ab35-b2c5c1d5aa2a",
  "consumerId" : "8ecb9cb7c7804d5d92e258b23d5245cc_1"
}
' | json_pp; sleep 2; done
```

： `json_pp`  pretty json。

：

```json
{
  "body": {
    "results": [
      {
        "inputStatus": "DISABLED",
        "jobId": 0,
        "type": "input_status"
      },
      {
        "type": "message",
        "jobId": 0,
        "message": "Welcome to jad!"
      },
      {
        "tutorials": "https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html",
        "time": "2020-08-06 15:56:43",
        "type": "welcome",
        "jobId": 0,
        "pid": "7909",
        "wiki": "https://github.com/Akshita-Sahu/JAD/doc",
        "version": "3.3.7"
      },
      {
        "inputStatus": "ALLOW_INPUT",
        "type": "input_status",
        "jobId": 0
      }
    ]
  },
  "sessionId": "b09f1353-202c-407b-af24-701b744f971e",
  "consumerId": "8f7f6ad7bc2d4cb5aa57a530927a95cc_2",
  "state": "SUCCEEDED"
}
```

### 

```bash
curl -Ss -XPOST http://localhost:8563/api -d '''
{
  "action":"async_exec",
  "command":"watch demo.MathGame primeFactors \"{params, returnObj, throwExp}\" ",
   "sessionId" : "2b085b5d-883b-4914-ab35-b2c5c1d5aa2a"
}
'''
```

`async_exec` ：

```json
{
  "sessionId": "2b085b5d-883b-4914-ab35-b2c5c1d5aa2a",
  "state": "SCHEDULED",
  "body": {
    "jobStatus": "READY",
    "jobId": 3,
    "command": "watch demo.MathGame primeFactors \"{params, returnObj, throwExp}\" "
  }
}
```

- `state` : `SCHEDULED` ，。
- `body.jobId` :
   ID， ID `pull_results`。
- `body.jobStatus` : `READY`。

 shell ：

```json
{
   "body" : {
      "results" : [
         {
            "type" : "command",
            "jobId" : 3,
            "state" : "SCHEDULED",
            "command" : "watch demo.MathGame primeFactors \"{params, returnObj, throwExp}\" "
         },
         {
            "inputStatus" : "ALLOW_INTERRUPT",
            "jobId" : 0,
            "type" : "input_status"
         },
         {
            "success" : true,
            "jobId" : 3,
            "effect" : {
               "listenerId" : 3,
               "cost" : 24,
               "classCount" : 1,
               "methodCount" : 1
            },
            "type" : "enhancer"
         },
         {
            "sizeLimit" : 10485760,
            "expand" : 1,
            "jobId" : 3,
            "type" : "watch",
            "cost" : 0.071499,
            "ts" : 1596703453237,
            "value" : [
               [
                  -170365
               ],
               null,
               {
                  "stackTrace" : [
                     {
                        "className" : "demo.MathGame",
                        "classLoaderName" : "app",
                        "methodName" : "primeFactors",
                        "nativeMethod" : false,
                        "lineNumber" : 46,
                        "fileName" : "MathGame.java"
                     },
                     ...
                  ],
                  "localizedMessage" : "number is: -170365, need >= 2",
                  "@type" : "java.lang.IllegalArgumentException",
                  "message" : "number is: -170365, need >= 2"
               }
            ]
         },
         {
            "type" : "watch",
            "cost" : 0.033375,
            "jobId" : 3,
            "ts" : 1596703454241,
            "value" : [
               [
                  1
               ],
               [
                  2,
                  2,
                  2,
                  2,
                  13,
                  491
               ],
               null
            ],
            "sizeLimit" : 10485760,
            "expand" : 1
         }
      ]
   },
   "consumerId" : "8ecb9cb7c7804d5d92e258b23d5245cc_1",
   "sessionId" : "2b085b5d-883b-4914-ab35-b2c5c1d5aa2a",
   "state" : "SUCCEEDED"
}
```

watch `value` watch-experss ，`{params, returnObj, throwExp}`， watch  value  3 ，。
"[watch  map ](#change_watch_value_to_map)"。

### 

 Job（）：

```bash
curl -Ss -XPOST http://localhost:8563/api -d '''
{
  "action":"interrupt_job",
  "sessionId" : "2b085b5d-883b-4914-ab35-b2c5c1d5aa2a"
}
'''
```

```
{
   "state" : "SUCCEEDED",
   "body" : {
      "jobStatus" : "TERMINATED",
      "jobId" : 3
   }
}
```

### 

 ID，。

```bash
curl -Ss -XPOST http://localhost:8563/api -d '''
{
  "action":"close_session",
  "sessionId" : "2b085b5d-883b-4914-ab35-b2c5c1d5aa2a"
}
'''
```

```json
{
  "state": "SUCCEEDED"
}
```

## 

： [auth](auth.md)

## Web UI

![](/images/jad-web-ui.png "jad web ui")

 Http API  Web UI，： [http://127.0.0.1:8563/ui](http://127.0.0.1:8563/ui) 。

：

- 
-  url ，
- 
- 
- /

：

- 
- 
- 
- 

<a id="special_command_results"></a>

## 

### status

```json
{
  "jobId": 5,
  "statusCode": 0,
  "type": "status"
}
```

`type``status`：

 status 。`statusCode`
 0 ，`statusCode`  0 ，(exit code)。

，：

```json
{
  "jobId": 3,
  "message": "The argument 'class-pattern' is required",
  "statusCode": -10,
  "type": "status"
}
```

### input_status

```json
{
  "inputStatus": "ALLOW_INPUT",
  "type": "input_status",
  "jobId": 0
}
```

`type``input_status`：

 UI ，。
`inputStatus` ：

- `ALLOW_INPUT` :
  ，，。
- `ALLOW_INTERRUPT` :
  ，，`interrupt_job`。
- `DISABLED` : ，。

### command

```json
{
  "type": "command",
  "jobId": 3,
  "state": "SCHEDULED",
  "command": "watch demo.MathGame primeFactors \"{params, returnObj, throwExp}\" "
}
```

`type` `command`：

 UI ，`command`，。

### enhancer

```json
{
  "success": true,
  "jobId": 3,
  "effect": {
    "listenerId": 3,
    "cost": 24,
    "classCount": 1,
    "methodCount": 1
  },
  "type": "enhancer"
}
```

`type``enhancer`：

`trace/watch/jad/tt`，`enhancer`。`enhancer`，，`enhancer`。

## 

###  Java  Classpath

 Http api  Java  System properties，`java.class.path`。

```bash
json_data=$(curl -Ss -XPOST http://localhost:8563/api -d '
{
  "action":"exec",
  "command":"sysprop"
}')
```

- `sed`：

```bash
class_path=$(echo $json_data | tr -d '\n' | sed 's/.*"java.class.path":"\([^"]*\).*/\1/')
echo "classpath: $class_path"
```

- `json_pp/awk`

```bash
class_path=$(echo $json_data | tr -d '\n' | json_pp | grep java.class.path | awk -F'"' '{ print $4 }')
echo "classpath: $class_path"
```

：

```
classpath: demo-jad-spring-boot.jar
```

：

- `echo $json_data | tr -d '\n'` : (`line.separator`)，`sed`/`json_pp`。
- `awk -F'"' '{ print $4 }'` : 

<a id="change_watch_value_to_map"></a>

### watch  map 

watch `watch-express` ognl ， ognl ，[OGNL ](https://commons.apache.org/dormant/commons-ognl/language-guide.html)。

::: tip
Maps can also be created using a special syntax.

#{ "foo" : "foo value", "bar" : "bar value" }

This creates a Map initialized with mappings for "foo" and "bar".
:::

 map ：

```bash
watch *MathGame prime* '#{ "params" : params, "returnObj" : returnObj, "throwExp": throwExp}' -x 2 -n 5
```

 Telnet shell/WebConsole ，：

```bash
ts=2020-08-06 16:57:20; [cost=0.241735ms] result=@LinkedHashMap[
    @String[params]:@Object[][
        @Integer[1],
    ],
    @String[returnObj]:@ArrayList[
        @Integer[2],
        @Integer[241],
        @Integer[379],
    ],
    @String[throwExp]:null,
]
```

 Http api ， JSON ：

```bash
curl -Ss -XPOST http://localhost:8563/api -d @- << EOF
{
  "action":"exec",
  "execTimeout": 30000,
  "command":"watch *MathGame prime* '#{ \"params\" : params, \"returnObj\" : returnObj, \"throwExp\": throwExp}' -n 3 "
}
EOF
```

Http api ：

```json
{
    "body": {
         ...
        "results": [
            ...
            {
                ...
                "type": "watch",
                "value": {
                    "params": [
                        1
                    ],
                    "returnObj": [
                        2,
                        5,
                        17,
                        23,
                        23
                    ]
                }
            },
            {
                ...
                "type": "watch",
                "value": {
                    "params": [
                        -98278
                    ],
                    "throwExp": {
                        "@type": "java.lang.IllegalArgumentException",
                        "localizedMessage": "number is: -98278, need >= 2",
                        "message": "number is: -98278, need >= 2",
                        "stackTrace": [
                            ...
                        ]
                    }
                }
            },
            ...
}
```

 watch  value  map ， key 。
