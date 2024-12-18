#!/usr/bin/pypy3

a = []
with open("data/data18", "r") as f:
    for line in f:
        [j, i] = [int(n) for n in line.split(",")]
        a.append((i, j))
# Part 1
MAX_CORD = 70
visited = set([(0, 0)])
walls = set(a[:1024])
hs = [(0, 0)]
s = 0
found = False
while not found:
    s += 1
    nhs = []
    for (i, j) in hs:
        for (di, dj) in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if (i+di, j+dj) not in walls and (i+di, j+dj) not in visited and 0 <= i+di <= MAX_CORD and 0 <= j+dj <= MAX_CORD:
                nhs.append((i+di, j+dj))
                visited.add((i+di, j+dj))
                if (i+di, j+dj) == (MAX_CORD, MAX_CORD):
                    found = True
                    break
        if found:
            break
    hs = nhs
print(s)

# Part 2
upper = len(a) - 1
lower = 0
while upper > lower:
    steps = lower + (upper - lower) // 2
    visited = set([(0, 0)])
    walls = set(a[:steps+1])
    hs = [(0, 0)]
    found = False
    while len(hs) > 0 and not found:
        nhs = []
        for (i, j) in hs:
            for (di, dj) in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                if (i+di, j+dj) not in walls and (i+di, j+dj) not in visited and 0 <= i+di <= MAX_CORD and 0 <= j+dj <= MAX_CORD:
                    nhs.append((i+di, j+dj))
                    visited.add((i+di, j+dj))
                    if (i+di, j+dj) == (MAX_CORD, MAX_CORD):
                        found = True
                        break
            if found:
                break
        hs = nhs
    if found:
        lower = steps + 1
    else:
        upper = steps
print(str(a[upper][1]) + "," + str(a[upper][0]))
