#!/usr/bin/pypy3
import functools

with open("data/data11", "r") as f:
    ns_orig = [int(x) for x in f.read().strip().split()]

# Part 1
ns = ns_orig.copy()
for i in range(25):
    nss = []
    for n in ns:
        n_str = str(n)
        if n == 0:
            nss.append(1)
        elif len(n_str) % 2 == 0:
            nss.append(int(n_str[:len(n_str)//2]))
            nss.append(int(n_str[len(n_str)//2:]))
        else:
            nss.append(n * 2024)
    ns = nss
print(len(ns))


# Part 2
@functools.lru_cache(maxsize=None)
def stones(n, depth):
    if depth == 75:
        return 1
    if n == 0:
        return stones(1, depth + 1)
    n_str = str(n)
    if len(n_str) % 2 == 0:
        return stones(int(n_str[:len(n_str)//2]), depth + 1) + stones(int(n_str[len(n_str)//2:]), depth + 1)
    return stones(n * 2024, depth + 1)


res = 0
for n in ns_orig:
    res += stones(n, 0)
print(res)
