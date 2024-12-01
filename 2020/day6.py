#!/usr/bin/pypy3

with open("data/data6.txt", "r") as f:
    gs = f.read().split("\n\n")

# Part 1
res = 0
for g in gs:
    s = set()
    for line in g.split("\n"):
        for c in line:
            s.add(c)
    res += len(s)
print(res)

# Part 2
res = 0
for g in gs:
    lines = g.split("\n")
    s = set([c for c in lines[0]])
    for line in lines[1:]:
        s = set(filter(lambda c: c in line, s))
    res += len(s)
print(res)
