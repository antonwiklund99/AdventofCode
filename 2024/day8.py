#!/usr/bin/pypy3
from collections import defaultdict

a = []
with open("data/data8", "r") as f:
    for line in f:
        a.append(line.strip())

antennas = defaultdict(list)
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] != ".":
            antennas[a[i][j]].append((i, j))

# Part 1
s = set()
for ant in antennas.keys():
    for i in range(len(antennas[ant])):
        for j in range(i + 1, len(antennas[ant])):
            (i1, j1) = antennas[ant][i]
            (i2, j2) = antennas[ant][j]
            di = i1 - i2
            dj = j1 - j2
            if len(a) > i1 + di >= 0 and len(a[0]) > j1 + dj >= 0:
                s.add((i1 + di, j1 + dj))
            if len(a) > i2 - di >= 0 and len(a[0]) > j2 - dj >= 0:
                s.add((i2 - di, j2 - dj))
print(len(s))

# Part 2
s = set()
for ant in antennas.keys():
    for i in range(len(antennas[ant])):
        (i1, j1) = antennas[ant][i]
        s.add(antennas[ant][i])
        for j in range(i + 1, len(antennas[ant])):
            (i2, j2) = antennas[ant][j]
            s.add(antennas[ant][j])
            di = i1 - i2
            dj = j1 - j2
            k = 1
            while len(a) > i1 + k * di >= 0 and len(a[0]) > j1 + k * dj >= 0:
                s.add((i1 + k * di, j1 + k * dj))
                k += 1
            k = 1
            while len(a) > i2 - k * di >= 0 and len(a[0]) > j2 - k * dj >= 0:
                s.add((i2 - k * di, j2 - k * dj))
                k += 1
print(len(s))
