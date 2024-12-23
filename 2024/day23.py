#!/usr/bin/pypy3
from collections import defaultdict

a = []
with open("data/data23", "r") as f:
    for line in f:
        a.append(line.strip())

c = defaultdict(set)
for line in a:
    (x1, x2) = line.split("-")
    c[x1].add(x2)
    c[x2].add(x1)

# Part 1
computers = list(c.keys())
connected = set()
for i in range(len(computers)):
    x1 = computers[i]
    for j in range(i + 1, len(computers)):
        x2 = computers[j]
        if x2 not in c[x1]:
            continue
        for k in range(j + 1, len(computers)):
            x3 = computers[k]
            if x1 in c[x3] and x2 in c[x3]:
                connected.add(",".join(sorted([x1, x2, x3])))
res = 0
for x in connected:
    (x1, x2, x3) = x.split(",")
    if x1[0] == "t" or x2[0] == "t" or x3[0] == "t":
        res += 1
print(res)


# Part 2
def longest(xs):
    global best_overall, best_str
    res = 0
    k = ",".join(sorted(xs))
    if k in cache:
        return cache[k]
    for j in range(0, len(computers)):
        x1 = computers[j]
        if x1 in xs:
            continue
        if all([x in c[x1] for x in xs]):
            xs.add(x1)
            res = max(res, longest(xs))
            xs.remove(x1)
    if res == 0:
        if len(xs) > best_overall:
            best_str = k
            best_overall = len(xs)
        res = len(xs)
    cache[k] = res
    return res


cache = {}
best_overall = 0
best_str = ""
for i in range(len(computers)):
    longest(set([computers[i]]))
print(best_str)
