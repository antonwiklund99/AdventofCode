#!/usr/bin/pypy3

a = []
with open("data/data7", "r") as f:
    for line in f:
        a.append(line.strip())


# Part 1
def rec(r, n, ns):
    if n > r:
        return False
    if len(ns) == 1 and (r == ns[0] * n or r == ns[0] + n):
        return True
    elif len(ns) == 1:
        return False
    elif rec(r, n * ns[0], ns[1:]) or rec(r, n + ns[0], ns[1:]):
        return True
    return False


res = 0
for line in a:
    [r, xs] = line.split(":")
    r = int(r.strip())
    ns = [int(x) for x in xs.strip().split()]
    if rec(r, ns[0], ns[1:]):
        res += r
print(res)


# Part 2
def rec2(r, n, ns):
    if n > r:
        return False
    comb = int(str(n) + str(ns[0]))
    if len(ns) == 1 and (r == ns[0] * n or r == ns[0] + n or r == comb):
        return True
    elif len(ns) == 1:
        return False
    elif rec2(r, n * ns[0], ns[1:]) or rec2(r, n + ns[0], ns[1:]) or rec2(r, comb, ns[1:]):
        return True
    return False


res = 0
for line in a:
    [r, xs] = line.split(":")
    r = int(r.strip())
    ns = [int(x) for x in xs.strip().split()]
    if rec2(r, ns[0], ns[1:]):
        res += r
print(res)
