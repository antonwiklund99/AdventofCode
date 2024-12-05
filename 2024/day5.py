#!/usr/bin/pypy3
from collections import defaultdict

with open("data/data5", "r") as f:
    [rs, us] = f.read().split("\n\n")

d = defaultdict(list)
for line in rs.split("\n"):
    [x, y] = line.strip().split("|")
    d[x].append(y)

# Part 1
res = 0
for u in us.strip().split("\n"):
    ns = u.strip().split(",")
    fail = False
    for i in range(len(ns)):
        for y in d[ns[i]]:
            if y in ns[0:i]:
                fail = True
                break
        if fail:
            break
    if not fail:
        res += int(ns[len(ns) // 2])
print(res)

# Part 2
res = 0
for u in us.strip().split("\n"):
    ns = u.strip().split(",")
    fail = True
    incorrect = False
    while fail:
        fail = False
        for i in range(len(ns)):
            for y in d[ns[i]]:
                for j in range(i):
                    if ns[j] == y:
                        ns[j] = ns[i]
                        ns[i] = y
                        fail = True
                        incorrect = True
    if incorrect:
        res += int(ns[len(ns) // 2])
print(res)
