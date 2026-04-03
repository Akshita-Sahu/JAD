import os
import re

root_dir = r"C:\Users\AKSHITA\Desktop\JAD"

# Target extensions
extensions = (".java", ".xml", ".md", ".yml", ".properties", ".sh")

# Replacements
replacements = [
    # strict case matching for specific mappings
    (r"arthas", "jad"),
    (r"Arthas", "JAD"),
    (r"alibaba", "Akshita-Sahu"),
    (r"taobao", "jad"),
]

# Chinese character regex
chinese_char_regex = re.compile(r'[\u4e00-\u9fff\u3400-\u4dbf\uf900-\ufaff]')

print(f"Scanning directory: {root_dir}")

for dirpath, dirnames, filenames in os.walk(root_dir):
    if ".git" in dirpath:
        continue
        
    for filename in filenames:
        if not filename.endswith(extensions):
            continue
            
        filepath = os.path.join(dirpath, filename)
        
        try:
            with open(filepath, "r", encoding="utf-8") as f:
                content = f.read()
        except UnicodeDecodeError:
            print(f"Skipping {filepath} due to decoding error.")
            continue
            
        new_content = content
        
        # apply replacements
        for old, new in replacements:
            # We don't use re.IGNORECASE because they are distinct.
            new_content = new_content.replace(old, new)
            
        # apply pom.xml specific replacements for groupId and project name
        if filename == "pom.xml":
            new_content = new_content.replace("com.taobao.arthas", "com.github.akshita-sahu.jad")
            # Replace project name tag if it's Arthas. Note: Project name tags <name>...</name>
            # But the replacements above already changed Arthas to JAD. Let's make sure <name>JAD</name> or so.
            # Usually POM has <name>arthas...</name>. The above replaces arthas -> jad and Arthas -> JAD.
            
        # remove remaining Chinese characters
        new_content = chinese_char_regex.sub('', new_content)
            
        if new_content != content:
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(new_content)
            print(f"Updated {filepath}")

print("Script execution completed.")
