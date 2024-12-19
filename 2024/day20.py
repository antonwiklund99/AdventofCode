#!/usr/bin/pypy3

a = []
with open("data/data20", "r") as f:
    for line in f:
        a.append(line.strip())

ei = -1
ej = -1
for i in range(len(a)):
    for j in range(len(a[0])):
        if a[i][j] == "E":
            (ei, ej) = (i, j)
            break
    if ei != -1:
        break

score_from = {(ei, ej): 0}
visited = set([(ei, ej)])
(i, j) = (ei, ej)
s = 0
while a[i][j] != "S":
    for (di, dj) in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
        if (i+di, j+dj) not in visited and a[i+di][j+dj] != "#":
            score_from[(i+di, j+dj)] = s + 1
            visited.add((i+di, j+dj))
            (i, j) = (i + di, j + dj)
            break
    s += 1

# Part 1
res = 0
for i in range(len(a)):
    for j in range(len(a[0])):
        if a[i][j] == "#" or (i, j) == (ei, ej):
            continue
        for (di1, dj1) in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
            for (di2, dj2) in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
                ni = i + di1 + di2
                nj = j + dj1 + dj2
                if 0 <= ni < len(a) and 0 <= nj < len(a[0]) and a[ni][nj] != "#" and score_from[(ni, nj)] <= score_from[(i, j)] - 102:
                    res += 1
print(res)

# Part 2
res = 0
for i in range(len(a)):
    for j in range(len(a[0])):
        if a[i][j] == "#" or (i, j) == (ei, ej):
            continue
        hs = [(i, j)]
        visited = set([(i, j)])
        for k in range(20):
            nhs = []
            for (hi, hj) in hs:
                for (di, dj) in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
                    ni = hi + di
                    nj = hj + dj
                    if 0 <= ni < len(a) and 0 <= nj < len(a[0]) and (ni, nj) not in visited:
                        if a[ni][nj] != "#" and score_from[(ni, nj)] <= score_from[(i, j)] - (101 + k):
                            res += 1
                        visited.add((ni, nj))
                        nhs.append((ni, nj))
            hs = nhs
print(res)
