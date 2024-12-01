#!/usr/bin/pypy3

a = []
with open("data/data8.txt", "r") as f:
    for line in f:
        a.append(int(line.strip()))


# Part 1
n = 0
for i in range(25, len(a)):
    found = False
    for j in range(i-25, i):
        for k in range(j+1, i):
            if a[j] + a[k] == a[i]:
                found = True
                break
    if not found:
        n = a[i]
        break
print(n)

# Part 2
res = 0
for i in range(len(a)):
    if a[i] == n:
        continue
    s = a[i]
    found = False
    for j in range(i+1, len(a)):
        s += a[j]
        if s == n:
            ns = sorted(a[i:j+1])
            res = ns[0] + ns[-1]
            found = True
            break
        elif s > n:
            break
    if found:
        break
print(res)
