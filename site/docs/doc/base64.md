# base64

::: tip
base64 ， linux  base64 。
:::

##  base64 

```bash
[jad@70070]$ echo 'abc' > /tmp/test.txt
[jad@70070]$ cat /tmp/test.txt
abc

[jad@70070]$ base64 /tmp/test.txt
YWJjCg==
```

##  base64 

```bash
$ base64 --input /tmp/test.txt --output /tmp/result.txt
```

##  base64 

```
$ base64 -d /tmp/result.txt
abc
```

##  base64 

```bash
$ base64 -d /tmp/result.txt --output /tmp/bbb.txt
```
