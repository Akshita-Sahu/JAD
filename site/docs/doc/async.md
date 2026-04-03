# JAD 

[``](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials?language=cn&id=case-async-jobs)

jad ， linux 。[linux ](https://ehlxr.me/2017/01/18/Linux-%E4%B8%AD-fg%E3%80%81bg%E3%80%81jobs%E3%80%81-%E6%8C%87%E4%BB%A4/)。

## 1. &

 trace ，

```bash
trace Test t &
```

， console 。

## 2.  jobs 

 jad ， jobs ，

```bash
$ jobs
[10]*
       Stopped           watch com.akshita-sahu.container.Test test "params[0].{? #this.name == null }" -x 2
       execution count : 19
       start time      : Fri Sep 22 09:59:55 CST 2017
       timeout date    : Sat Sep 23 09:59:55 CST 2017
       session         : 3648e874-5e69-473f-9eed-7f89660b079b (current)
```

。

- job id  10, `*`  job  session 
-  Stopped
- execution count ， 19 
- timeout date ，，

## 3. 

，`trace Test t``trace Test t &``fg`。 console ，：

- ‘ctrl + z’：。`jobs` Stopped，`bg <job-id>``fg <job-id>`
- ‘ctrl + c’：
- ‘ctrl + d’： linux ， jad ，

## 4. fg、bg ，、

- （`ctrl + z`），`fg <job-id>`。， console 
- （`ctrl + z`），`bg <job-id>`
-  session  job， session fg 

## 5. 

`>``>>`，`&`， jad 。：

```bash
$ trace Test t >> test.out &
```

 trace ，```test.out`。。。`pwd```。

```bash
$ cat test.out
```

，`~/logs/jad-cache/`，：

```bash
$ trace Test t >>  &
job id  : 2
cache location  : /Users/admin/logs/jad-cache/28198/2
```

，（`~/logs/jad-cache/${PID}/${JobId}`）；

-  session ； 1 ， `options` ；
-   ； `save-result`  true，`~/logs/jad-cache/result.log` 。

## 6. 

，，`kill <job-id>`

## 7. 

-  8 
- ， JVM 
-  jad，， `quit`  jad （`stop`  jad ）
