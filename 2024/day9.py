#!/usr/bin/pypy3

with open("data/data9", "r") as f:
    s = f.read().strip()

fs = []
files = []
for i in range(0, len(s), 2):
    if i + 1 < len(s):
        files.append((int(s[i]), int(s[i+1]), len(fs)))
    else:
        files.append((int(s[i]), 0, len(fs)))
    for j in range(int(s[i])):
        fs.append(i // 2)
    if i + 1 < len(s):
        for j in range(int(s[i+1])):
            fs.append(-1)
fs_orig = fs.copy()

# Part 1
zi = 0
res = 0
for (size, unused, start) in reversed(files[1:]):
    i = 0
    while i < (size) and zi < start + i:
        if fs[zi] == -1:
            assert (fs[start+i] != -1)
            fs[zi] = fs[start+i]
            fs[start+i] = -1
            i += 1
        zi += 1

for i in range(len(fs)):
    if fs[i] == -1:
        break
    res += fs[i] * i
print(res)

# Part 2
fs = fs_orig
res = 0
for (size, unused, start) in reversed(files[1:]):
    i = 0
    while i < len(fs):
        zi = 0
        while i + zi < start and fs[i + zi] == -1:
            zi += 1
        if zi >= size:
            for j in range(size):
                fs[i + j] = fs[start + j]
                fs[start + j] = -1
        i += zi if zi > 0 else 1

for i in range(len(fs)):
    if fs[i] == -1:
        continue
    res += fs[i] * i
print(res)
