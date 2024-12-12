#!/usr/bin/pypy3

a = []
with open("data/data12", "r") as f:
    for line in f:
        a.append(line.strip())

# Part 1
res = 0
done = [[False for _ in range(len(a[0]))] for _ in range(len(a))]
group_idx = 0
for i in range(len(a)):
    for j in range(len(a[0])):
        if not done[i][j]:
            p = 0
            area = 1
            hs = [(i, j)]
            done[i][j] = True
            while len(hs) > 0:
                (ii, jj) = hs.pop()
                for (di, dj) in [(1, 0), (0, 1), (-1, 0), (0, -1)]:
                    ni = ii + di
                    nj = jj + dj
                    if ni < 0 or ni >= len(a) or nj < 0 or nj >= len(a[0]) or a[ni][nj] != a[i][j]:
                        p += 1
                    elif not done[ni][nj]:
                        area += 1
                        done[ni][nj] = True
                        hs.append((ni, nj))
            res += area * p
            group_idx += 1

print(res)

# Part 2
res = 0
done = [[False for _ in range(len(a[0]))] for _ in range(len(a))]
for i in range(len(a)):
    for j in range(len(a[0])):
        if not done[i][j]:
            area = 1
            p = 0
            hs = [(i, j)]
            done[i][j] = True
            already_counted = set()
            while len(hs) > 0:
                (ii, jj) = hs.pop()
                for (di, dj) in [(1, 0), (0, 1), (-1, 0), (0, -1)]:
                    ni = ii + di
                    nj = jj + dj
                    if ni < 0 or ni >= len(a) or nj < 0 or nj >= len(a[0]) or a[ni][nj] != a[i][j]:
                        if not (ii, jj, di, dj) in already_counted:
                            already_counted.add((ii, jj, di, dj))
                            p += 1
                            if di != 0:
                                for k in range(1, jj + 1):
                                    if a[ii][jj - k] != a[i][j] or (0 <= ni < len(a) and a[ni][jj - k] == a[i][j]):
                                        break
                                    already_counted.add((ii, jj - k, di, dj))
                                for k in range(1, len(a[0]) - jj):
                                    if a[ii][jj + k] != a[i][j] or (0 <= ni < len(a) and a[ni][jj + k] == a[i][j]):
                                        break
                                    already_counted.add((ii, jj + k, di, dj))
                            else:
                                for k in range(1, ii + 1):
                                    if a[ii - k][jj] != a[i][j] or (0 <= nj < len(a[0]) and a[ii - k][nj] == a[i][j]):
                                        break
                                    already_counted.add((ii - k, jj, di, dj))
                                for k in range(1, len(a) - ii):
                                    if a[ii + k][jj] != a[i][j] or (0 <= nj < len(a[0]) and a[ii + k][nj] == a[i][j]):
                                        break
                                    already_counted.add((ii + k, jj, di, dj))
                    elif not done[ni][nj]:
                        area += 1
                        done[ni][nj] = True
                        hs.append((ni, nj))
            res += area * p
print(res)
