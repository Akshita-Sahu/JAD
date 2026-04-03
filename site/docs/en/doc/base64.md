# base64

::: tip
Encode and decode using Base64 representation.
:::

## Encode to base64

```bash
[jad@70070]$ echo 'abc' > /tmp/test.txt
[jad@70070]$ cat /tmp/test.txt
abc

[jad@70070]$ base64 /tmp/test.txt
YWJjCg==
```

## Encode to base64 and save output to file

```bash
$ base64 --input /tmp/test.txt --output /tmp/result.txt
```

## Decode from base64

```
$ base64 -d /tmp/result.txt
abc
```

## Decode from base64 and save output to file

```bash
$ base64 -d /tmp/result.txt --output /tmp/bbb.txt
```
