#!/usr/bin/pypy3

a = []
with open("data/data10", "r") as f:
    for line in f:
        a.append([int(x) for x in line.strip()])

# Part 1
res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] == 0:
            found = set()
            ps = [(i, j)]
            visited = set(ps)
            while len(ps) > 0:
                nps = []
                for (ii, jj) in ps:
                    for (di, dj) in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                        if 0 <= ii + di < len(a) and 0 <= jj + dj < len(a[0]) and a[ii+di][jj+dj] == a[ii][jj] + 1:
                            visited.add((ii+di, jj+dj))
                            if a[ii+di][jj+dj] == 9:
                                found.add((ii+di, jj+dj))
                            else:
                                nps.append((ii+di, jj+dj))
                ps = nps
            res += len(found)
print(res)


# Part 2
def dfs(i, j):
    found = 0
    for (di, dj) in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
        if 0 <= i + di < len(a) and 0 <= j + dj < len(a[0]) and a[i+di][j+dj] == a[i][j] + 1:
            if a[i+di][j+dj] == 9:
                found += 1
            else:
                found += dfs(i+di, j+dj)
    return found


res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] == 0:
            res += dfs(i, j)
print(res)
