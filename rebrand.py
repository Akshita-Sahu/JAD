import os
import re

root_dir = r"C:\Users\AKSHITA\Desktop\JAD"

# 1. Delete unnecessary files
print("Deleting files...")
files_to_delete = ["USERS.md", "README_CN.md", "README_EN.md"]
for f in files_to_delete:
    fp = os.path.join(root_dir, f)
    if os.path.exists(fp):
        os.remove(fp)
        print("Deleted", fp)

# 2. Rename directories (bottom-up to avoid path invalidation)
print("Renaming directories...")
for dirpath, dirnames, filenames in os.walk(root_dir, topdown=False):
    if ".git" in dirpath or ".github" in dirpath:
        continue
    for dirname in dirnames:
        old_path = os.path.join(dirpath, dirname)
        new_name = dirname
        if new_name.startswith("arthas-"):
            new_name = new_name.replace("arthas-", "jad-")
        if new_name == "arthas":
            new_name = "jad"
        elif new_name == "taobao":
            new_name = "akshita"
        
        if new_name != dirname:
            new_path = os.path.join(dirpath, new_name)
            try:
                os.rename(old_path, new_path)
                print("Renamed directory", old_path, "->", new_path)
            except Exception as e:
                print("Failed to rename directory", old_path, ":", e)

# 3. String Replacements
print("Replacing strings in files...")
replacements = [
    (r"arthas\.aliyun\.com", "github.com/Akshita-Sahu/JAD"),
    (r"com\.taobao\.arthas", "com.akshita.jad"),
    (r"com\.alibaba\.arthas", "com.akshita.jad"),
    (r"ArthasClassloader", "JADClassloader"),
    (r"as-command-execute-daemon", "jad-command-execute-daemon"),
    (r"as-session-expire-daemon", "jad-session-expire-daemon"),
    (r"<groupId>com\.alibaba</groupId>", "<groupId>com.github.akshita-sahu</groupId>"),
    (r"<groupId>com\.taobao\.arthas</groupId>", "<groupId>com.github.akshita-sahu</groupId>"),
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

copyright_replacement = "Copyright (c) 2024 Akshita Sahu. All Rights Reserved."

for dirpath, dirnames, filenames in os.walk(root_dir):
    if ".git" in dirpath:
        continue
    for filename in filenames:
        if filename in ["rebrand.py", "task.md", "implementation_plan.md"]:
            continue
        filepath = os.path.join(dirpath, filename)
        
        try:
            with open(filepath, "r", encoding="utf-8") as f:
                content = f.read()
        except UnicodeDecodeError:
            continue
            
        new_content = content
        
        new_content = re.sub(
            r"(?i).*Copyright\s+(?:\([cC]\)\s+)?[0-9]{4}(?:-[0-9]{4})?(?:[, ]*[0-9]{4})*\s+(?:Alibaba|Taobao|Arthas|Akshita Sahu).*?\n", 
            " * " + copyright_replacement + "\n", 
            new_content
        )
        new_content = re.sub(
            r"(?i).*Copyright\s+[0-9-]+\s+Alibaba.*?\n",
            " * " + copyright_replacement + "\n",
            new_content
        )

        for old, new in replacements:
            new_content = re.sub(old, new, new_content)
            
        if new_content != content:
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(new_content)

# 4. Handle file renames separately to avoid modifying during walk
print("Renaming files...")
file_renames = []
for dirpath, dirnames, filenames in os.walk(root_dir):
    if ".git" in dirpath:
        continue
    for filename in filenames:
        new_name = filename
        if "arthas" in new_name.lower():
            # keep case
            new_name = new_name.replace("arthas", "jad").replace("Arthas", "JAD").replace("ARTHAS", "JAD")
            
        if new_name == "as.sh":
            new_name = "jad.sh"
        elif new_name == "as.bat":
            new_name = "jad.bat"
        elif new_name == "as-package.sh":
            new_name = "jad-package.sh"
            
        if new_name != filename:
            file_renames.append((os.path.join(dirpath, filename), os.path.join(dirpath, new_name)))

for old, new in file_renames:
    try:
        os.rename(old, new)
        print("Renamed file", old, "->", new)
    except Exception as e:
        print("Failed to rename file", old, ":", e)

print("Rebranding script completed successfully.")
