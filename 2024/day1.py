#!/usr/bin/pypy3

a = []
b = []
with open("data/data1", "r") as f:
    for line in f:
        [x, y] = line.strip().split()
        a.append(int(x))
        b.append(int(y))
a.sort()
b.sort()

# Part 1
res = 0
for i in range(len(a)):
    res += abs(a[i] - b[i])
print(res)

# Part 2
res = 0
for x in a:
    c = 0
    for y in b:
        if x == y:
            c += 1
    res += c * x
print(res)
