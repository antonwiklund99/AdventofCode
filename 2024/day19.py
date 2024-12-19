#!/usr/bin/pypy3
import functools

a = []
with open("data/data19", "r") as f:
    for line in f:
        a.append(line.strip())
    cs = [x.strip() for x in a[0].strip().split(",")]


# Part 1
@functools.lru_cache(maxsize=None)
def possible(line):
    if len(line) <= 0:
        return True
    for c in cs:
        if line[:len(c)] == c:
            if possible(line[len(c):]):
                return True
    return False


res = 0
for line in a[2:]:
    if possible(line):
        res += 1
print(res)


# Part 2
@functools.lru_cache(maxsize=None)
def ways(line):
    n = 0
    if len(line) <= 0:
        return 1
    for c in cs:
        if line[:len(c)] == c:
            n += ways(line[len(c):])
    return n


res = 0
for line in a[2:]:
    res += ways(line)
print(res)
