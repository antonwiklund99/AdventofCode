#!/usr/bin/pypy3

with open("data/data25", "r") as f:
    a = [x.split("\n") for x in f.read().strip().split("\n\n")]

rows = len(a[0])
cols = len(a[0][0])

keys = []
locks = []
for m in a:
    is_lock = m[0][0] == "#"
    hs = []
    for col in range(cols):
        h = 0 if is_lock else rows - 1
        while 0 <= h < rows and m[h][col] == "#":
            h = (h + 1) if is_lock else (h - 1)
        hs.append(h - 1 if is_lock else rows - h - 2)
    if is_lock:
        locks.append(hs)
    else:
        keys.append(hs)
res = 0
for ki in range(len(keys)):
    for li in range(len(locks)):
        match = True
        for i in range(cols):
            if keys[ki][i] + locks[li][i] > rows - 2:
                match = False
                break
        if match:
            res += 1
print(res)
