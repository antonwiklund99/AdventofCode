#!/usr/bin/pypy3

EMPTY = 0
BOX = 1
WALL = 2
ROBOT = 3
BOX_L = 4
BOX_R = 5


def to_dir(d):
    if d == "^":
        return (-1, 0)
    elif d == ">":
        return (0, 1)
    elif d == "<":
        return (0, -1)
    elif d == "v":
        return (1, 0)
    assert False


def to_int(c):
    if c == ".":
        return EMPTY
    if c == "O":
        return BOX
    if c == "#":
        return WALL
    if c == "@":
        return ROBOT
    assert False


def to_int2(c):
    if c == ".":
        return [EMPTY, EMPTY]
    if c == "O":
        return [BOX_L, BOX_R]
    if c == "#":
        return [WALL, WALL]
    if c == "@":
        return [ROBOT, EMPTY]
    assert False


a = []
with open("data/data15", "r") as f:
    [m, s] = f.read().strip().split("\n\n")
    ds = [to_dir(x) for x in s.replace("\n", "").strip()]
    m = m.strip().splitlines()

# Part 1
p = (-1, -1)
for i in range(len(m)):
    a.append([])
    for j in range(len(m[i])):
        if m[i][j] == "@":
            p = (i, j)
        a[i].append(to_int(m[i][j]))

for (di, dj) in ds:
    (i, j) = p
    k = 1
    while a[i + di*k][j + dj*k] == BOX:
        k += 1
    if a[i + di*k][j + dj*k] == EMPTY:
        while k > 0:
            a[i + di*k][j + dj*k] = a[i + di*(k-1)][j + dj*(k-1)]
            k -= 1
        a[i][j] = EMPTY
        p = (i + di, j + dj)
res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] == BOX:
            res += 100 * i + j
print(res)

# Part 2
p = (-1, -1)
a = []
for i in range(len(m)):
    a.append([])
    for j in range(len(m[i])):
        if m[i][j] == "@":
            p = (i, j * 2)
        a[i] += to_int2(m[i][j])
for (di, dj) in ds:
    hs = set([p])
    levels = []
    failed = False
    while not failed and len(hs) > 0:
        nhs = set()
        for (ni, nj) in hs:
            if a[ni+di][nj+dj] == EMPTY:
                continue
            if a[ni+di][nj+dj] == WALL:
                failed = True
                break
            nhs.add((ni+di, nj+dj))
            if di != 0 and a[ni+di][nj+dj] == BOX_L:
                nhs.add((ni+di, nj+1))
            elif di != 0 and a[ni+di][nj+dj] == BOX_R:
                nhs.add((ni+di, nj-1))
        levels.append(hs)
        hs = nhs
    if not failed:
        for hs in reversed(levels):
            for (ni, nj) in hs:
                a[ni + di][nj + dj] = a[ni][nj]
                a[ni][nj] = EMPTY
        p = (p[0] + di, p[1] + dj)
res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] == BOX_L:
            res += 100 * i + j
print(res)
