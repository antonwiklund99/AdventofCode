#!/usr/bin/pypy3
import math

a = []
with open("data/data5.txt", "r") as f:
    for line in f:
        a.append(line.strip())

# Part 1
ids = []
for line in a:
    r_start = 0
    r_end = 127
    c_start = 0
    c_end = 7
    for c in line[:7]:
        diff = math.ceil((r_end - r_start)/2)
        if c == "F":
            r_end = r_end - diff
        elif c == "B":
            r_start = r_start + diff
    for c in line[7:]:
        diff = math.ceil((c_end - c_start)/2)
        if c == "R":
            c_start = c_start + diff
        elif c == "L":
            c_end = c_end - diff
    ids.append(r_end * 8 + c_end)
ids.sort()
print(ids[-1])

# Part 2
for i in range(len(ids)):
    if ids[i] + 1 != ids[i+1]:
        print(ids[i]+1)
        break
