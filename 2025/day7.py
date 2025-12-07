#!/usr/bin/pypy3
import functools

a = []
with open("data/data7", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1
res = 0
q = set([(a[0].index("S"),0)])
for y in range(len(a)):
    nq = set()
    for (x,y) in q:
        if a[y][x] == '^':
            res += 1
            nq.add((x+1,y+1))
            nq.add((x-1,y+1))
        else:
            nq.add((x,y+1))
    q = nq
print(res)

# Part 2
@functools.lru_cache
def rec(x, y):
    if x < 0 or x >= len(a[0]) or y >= len(a):
        return 1
    if a[y][x] == '^':
        return rec(x - 1, y) + rec(x + 1, y)
    else:
        return rec(x, y + 1)

print(rec(a[0].index("S"), 0))
