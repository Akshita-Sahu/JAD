import os
import re

root_dir = r"C:\Users\AKSHITA\Desktop\JAD"

replacements = [
    (r"arthas\.aliyun\.com", "github.com/Akshita-Sahu/JAD"),
    (r"<groupId>com\.taobao\.arthas</groupId>", "<groupId>com.github.akshita-sahu</groupId>"),
    (r"<groupId>com\.alibaba\.arthas</groupId>", "<groupId>com.github.akshita-sahu</groupId>"),
    (r"<groupId>com\.akshita\.jad</groupId>", "<groupId>com.github.akshita-sahu</groupId>"),
    (r"<groupId>com\.alibaba</groupId>", "<groupId>com.github.akshita-sahu</groupId>"),
    (r"com\.taobao\.arthas", "com.akshita.jad"),
    (r"com\.alibaba\.arthas", "com.akshita.jad"),
    (r"ArthasClassloader", "JADClassloader"),
    (r"as-command-execute-daemon", "jad-command-execute-daemon"),
    (r"as-session-expire-daemon", "jad-session-expire-daemon"),
    (r"\bas\.sh\b", "jad.sh"),
    (r"\bas\.bat\b", "jad.bat"),
    (r"\bas-package\.sh\b", "jad-package.sh"),
    (r"Arthas", "JAD"),
    (r"arthas", "jad"),
    (r"ARTHAS", "JAD"),
    (r"Alibaba", "Akshita Sahu"),
    (r"alibaba", "akshita-sahu"),
    (r"Taobao", "Akshita Sahu"),
    (r"taobao", "akshita-sahu"),
    (r"Aliyun", "Akshita Sahu"),
    (r"aliyun", "akshita-sahu"),
]

for dirpath, dirnames, filenames in os.walk(root_dir):
    if ".git" in dirpath:
        continue
    for filename in filenames:
        if filename.endswith(".py"):
            continue
        filepath = os.path.join(dirpath, filename)
        
        content = None
        try:
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
        except UnicodeDecodeError:
            try:
                with open(filepath, 'r', encoding='gbk') as f:
                    content = f.read()
            except UnicodeDecodeError:
                continue
                
        if content is None:
            continue
            
        new_content = content
        for old, new in replacements:
            new_content = re.sub(old, new, new_content)
        
        if new_content != content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)

print("Encoding fallback and secondary rebrand complete.")
