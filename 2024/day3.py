#!/usr/bin/pypy3
import re

with open("data/data3", "r") as f:
    s = f.read()

# Part 1
res = 0
for (x, y) in re.findall(r"mul\((\d+),(\d+)\)", s):
    res += int(x) * int(y)
print(res)

# Part 2
res = 0
enable = True
for (f, x, y) in re.findall(r"(mul\((\d+),(\d+)\)|do\(\)|don't\(\))", s):
    if f == "do()":
        enable = True
    elif f == "don't()":
        enable = False
    elif enable:
        res += int(x) * int(y)
print(res)
