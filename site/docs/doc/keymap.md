# keymap

[`keymap`](https://github.com/Akshita-Sahu/JAD/doc/jad-tutorials.html?language=cn&id=command-keymap)

`keymap`：

：

|         |        |              |                          |
| ------------- | ---------------- | -------------------- | -------------------------------- |
| `"\C-a"`      | ctrl + a         | beginning-of-line    |                          |
| ` "\C-e"`     | ctrl + e         | end-of-line          |                          |
| `"\C-f"`      | ctrl + f         | forward-word         |                  |
| `"\C-b"`      | ctrl + b         | backward-word        |                  |
| `"\e[D"`      |      | backward-char        |              |
| `"\e[C"`      |      | forward-char         |              |
| `"\e[B"`      |      | next-history         |                |
| `"\e[A"`      |      | previous-history     |                |
| `"\C-h"`      | ctrl + h         | backward-delete-char |                  |
| `"\C-?"`      | ctrl + shift + / | backward-delete-char |                  |
| `"\C-u"`      | ctrl + u         | undo                 | ， |
| `"\C-d"`      | ctrl + d         | delete-char          |              |
| `"\C-k"`      | ctrl + k         | kill-line            |      |
| `"\C-i"`      | ctrl + i         | complete             | ，`TAB`          |
| `"\C-j"`      | ctrl + j         | accept-line          | ，         |
| `"\C-m"`      | ctrl + m         | accept-line          | ，         |
| `"\C-w"`      |                  | backward-delete-word |                                  |
| `"\C-x\e[3~"` |                  | backward-kill-line   |                                  |
| `"\e\C-?"`    |                  | backward-kill-word   |                                  |

-  `tab` ，
-  `-`  `--` ， `tab` ，

## 

`$USER_HOME/.jad/conf/inputrc`，。

 vim ，`ctrl+h`，，

```
"\C-a": beginning-of-line
"\C-e": end-of-line
"\C-f": forward-word
"\C-b": backward-word
"\e[D": backward-char
"\e[C": forward-char
"\e[B": next-history
"\e[A": previous-history
"\C-h": backward-delete-char
"\C-?": backward-delete-char
"\C-u": undo
"\C-d": delete-char
"\C-k": kill-line
"\C-i": complete
"\C-j": accept-line
"\C-m": accept-line
"\C-w": backward-delete-word
"\C-x\e[3~": backward-kill-line
"\e\C-?": backward-kill-word
```

`"\C-h": backward-delete-char``"\C-h": backward-char`，。

## 

- ctrl + c: 
- ctrl + z: ， bg/fg ， kill 
- ctrl + a: 
- ctrl + e: 
