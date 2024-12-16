#!/usr/bin/pypy3
from heapq import heappop, heappush, heapify
from collections import defaultdict

a = []
with open("data/data16", "r") as f:
    for line in f:
        a.append(line.strip())


def rotate_counter_clockwise(d):
    if d == (0, 1):
        return (-1, 0)
    if d == (-1, 0):
        return (0, -1)
    if d == (0, -1):
        return (1, 0)
    if d == (1, 0):
        return (0, 1)
    assert False


def rotate_clockwise(d):
    if d == (0, 1):
        return (1, 0)
    if d == (1, 0):
        return (0, -1)
    if d == (0, -1):
        return (-1, 0)
    if d == (-1, 0):
        return (0, 1)
    assert False


def get_paths(i, j, di, dj):
    if prev[(i, j, di, dj)] == (-1, -1, -1, -1):
        return set()
    res = set([(i, j)])
    for (pi, pj, pdi, pdj) in prev[(i, j, di, dj)]:
        res = res.union(get_paths(pi, pj, pdi, pdj))
    return res


res = -1
hs = [(0, len(a) - 2, 1, 0, 1, (-1, -1, -1, -1))]
best = {}
prev = defaultdict(set)
heapify(hs)
while True:
    (score, i, j, di, dj, p) = heappop(hs)
    if (i, j, di, dj) in best:
        if best[(i, j, di, dj)] == score:
            prev[(i, j, di, dj)].add(p)
        continue
    prev[(i, j, di, dj)].add(p)
    best[(i, j, di, dj)] = score
    if a[i][j] == "E":
        break
    (di1, dj1) = rotate_clockwise((di, dj))
    (di2, dj2) = rotate_counter_clockwise((di, dj))
    if a[i+di][j+dj] != "#":
        heappush(hs, (score+1, i+di, j+dj, di, dj, (i, j, di, dj)))
    if a[i+di1][j+dj1] != "#":
        heappush(hs, (score+1000, i, j, di1, dj1, (i, j, di, dj)))
    if a[i+di2][j+dj2] != "#":
        heappush(hs, (score+1000, i, j, di2, dj2, (i, j, di, dj)))
print(score)
print(len(get_paths(i, j, di, dj)) - 1)
