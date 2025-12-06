#!/usr/bin/pypy3

a = []
with open("data/data6", "r") as f:
  for l in f:
    a.append(l)

ops = a[-1].strip().split()

# Part 1
res = 0
ns = []
for e in a[:-1]:
    ns.append([])
    for n in e.split():
        ns[-1].append(int(n.strip()))
for (i, op) in enumerate(ops):
    if op == "+":
        k = 0
    else:
        k = 1
    for j in range(len(ns)):
        if op == "+":
            k += ns[j][i]
        else:
            k *= ns[j][i]
    res += k
print(res)

# Part 2
res = 0
ns = []
c = []
for i in range(len(a[0])):
    n = ""
    for j in range(len(a)-1):
        n += a[j][i]
    if n.isspace():
        ns.append(c)
        c = []
    else:
        c.append(n)
for (i, op) in enumerate(ops):
    if op == "+":
        k = 0
    else:
        k = 1
    for n in ns[i]:
        if op == "+":
            k += int(n)
        else:
            k *= int(n)
    res += k
print(res)
