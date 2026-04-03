# auth

::: tip

:::

## 

 attach ，。：

```
java -jar jad-boot.jar --password ppp
```

-  `--username` ，`jad`。
-  `jad.properties`  username/password。。
- `username`，`password`，，`~/logs/jad/jad.log`

  ```
  Using generated security password: 0vUBJpRIppkKuZ7dYzYqOKtranj4unGh
  ```

## 

，`jad.properties`：

```
jad.localConnectionNonAuth=true
```

，，。 true，。，。

##  telnet console 

 jad ，：

```bash
[jad@37430]$ help
Error! command not permitted, try to use 'auth' command to authenticates.
```

`auth`，。

```
[jad@37430]$ auth ppp
Authentication result: true
```

-  `--username` ，`jad`。

## Web console 

，   。

， web console。

## HTTP API 

### Authorization Header （）

JAD  HTTP  Basic Authorization， header 。

- ：[https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication)

，：`admin`， `admin`，： `admin:admin`，base64 ： `YWRtaW46YWRtaW4=`， HTTP `Authorization` header：

```bash
curl 'http://localhost:8563/api' \
  -H 'Authorization: Basic YWRtaW46YWRtaW4=' \
  --data-raw '{"action":"exec","command":"version"}'
```

### URL 

， parameters  username  password。：

```bash
curl 'http://localhost:8563/api?password=admin' \
  --data-raw '{"action":"exec","command":"version"}'
```
