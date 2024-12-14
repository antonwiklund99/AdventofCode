#!/usr/bin/pypy3

max_x = 101
max_y = 103
robots = []
ns = [[0 for _ in range(max_x)] for _ in range(max_y)]
with open("data/data14", "r") as f:
    for line in f:
        [ps, vs] = line.split()
        [px, py] = [int(x) for x in ps.replace("p=", "").split(",")]
        [vx, vy] = [int(x) for x in vs.replace("v=", "").split(",")]
        ns[py][px] += 1
        robots.append((px, py, vx, vy))

# Part 1
for sec in range(100):
    nrobots = []
    for i in range(len(robots)):
        (px, py, vx, vy) = robots[i]
        nx = (px + vx) % max_x
        ny = (py + vy) % max_y
        ns[py][px] -= 1
        ns[ny][nx] += 1
        robots[i] = (nx, ny, vx, vy)
res = 1
n = 0
for x in range((max_x - 1) // 2):
    for y in range((max_y - 1) // 2):
        n += ns[y][x]
res *= n
n = 0
for x in range((max_x + 1) // 2, max_x):
    for y in range((max_y - 1) // 2):
        n += ns[y][x]
res *= n
n = 0
for x in range((max_x - 1) // 2):
    for y in range((max_y + 1) // 2, max_y):
        n += ns[y][x]
res *= n
n = 0
for x in range((max_x + 1) // 2, max_x):
    for y in range((max_y + 1) // 2, max_y):
        n += ns[y][x]
res *= n
print(res)

# Part 2
for sec in range(18000):
    print(sec + 101)
    nrobots = []
    for i in range(len(robots)):
        (px, py, vx, vy) = robots[i]
        nx = (px + vx) % max_x
        ny = (py + vy) % max_y
        ns[py][px] -= 1
        ns[ny][nx] += 1
        robots[i] = (nx, ny, vx, vy)
    for y in range(max_y):
        s = ""
        for x in range(max_x):
            s += " " if ns[y][x] == 0 else "X"
        print(s)
    print("\n\n\n")
