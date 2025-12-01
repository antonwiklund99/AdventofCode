#!/usr/bin/pypy3

a = []
with open("data/data1", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1
c = 50
res = 0
for e in a:
    if e[0] == "L":
        c = (c - int(e[1:])) % 100
    else:
        c = (c + int(e[1:])) % 100
    if c == 0:
        res += 1
print(res)

# Part 2
c = 50
res = 0
for (i,e) in enumerate(a):
    if e[0] == "L":
        if c <= int(e[1:]):
            if c != 0:
                res += 1
            res += abs(c - int(e[1:])) // 100
        c = (c - int(e[1:])) % 100
    else:
        res += (c + int(e[1:])) // 100
        c = (c + int(e[1:])) % 100
print(res)

