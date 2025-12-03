#!/usr/bin/pypy3

a = []
with open("data/data3", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1
res = 0
for (i,e) in enumerate(a):
    m = 0
    for j in range(len(e)):
        for k in range(j+1, len(e)):
            m = max(m, int(e[j] + e[k]))
    res += m
print(res)

# Part 2
def rec(e, x):
    if len(x) == 12:
        return int(x)

    idx = -1
    m = 0
    for i in range(len(e)):
        if len(e) - i < 12 - len(x):
            break
        k = int(e[i])
        if k > m:
            idx = i
            m = k

    if idx == -1:
        return -1

    return rec(e[idx+1:], x + e[idx])

res = 0
for e in a:
    res += rec(e, "")
print(res)
