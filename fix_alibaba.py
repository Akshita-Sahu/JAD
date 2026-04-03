import os

root_dir = r"C:\Users\AKSHITA\Desktop\JAD"

for dirpath, dirnames, filenames in os.walk(root_dir, topdown=False):
    if ".git" in dirpath or ".github" in dirpath:
        continue
    for dirname in dirnames:
        old_path = os.path.join(dirpath, dirname)
        new_name = dirname
        if new_name == "alibaba":
            new_name = "akshita"
        
        if new_name != dirname:
            new_path = os.path.join(dirpath, new_name)
            try:
                os.rename(old_path, new_path)
                print("Renamed directory", old_path, "->", new_path)
            except Exception as e:
                print("Failed to rename directory", old_path, ":", e)

print("Fix alibaba folders complete.")
