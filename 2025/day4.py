#!/usr/bin/pypy3

a = []
with open("data/data4", "r") as f:
  for l in f:
    a.append([c for c in l.strip()])

# Part 1
res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] != "@":
            continue
        r = 0
        for x in range(-1,2):
            for y in range(-1,2):
                if (x != 0 or y != 0) and 0 <= i+x < len(a) and 0 <= j+y < len(a[i]) and a[i+x][j+y] == "@":
                    r += 1
        if r < 4:
            res += 1
print(res)

# Part 2
done = False
res = 0
while not done:
    done = True
    for i in range(len(a)):
        for j in range(len(a[i])):
            if a[i][j] != "@":
                continue
            r = 0
            for di in range(-1,2):
                for dj in range(-1,2):
                    if (di != 0 or dj != 0) and 0 <= i+di < len(a) and 0 <= j+dj < len(a[i]) and a[i+di][j+dj] == "@":
                        r += 1
            if r < 4:
                a[i][j] = "x"
                res += 1
                done = False
print(res)
