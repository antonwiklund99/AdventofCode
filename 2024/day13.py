#!/usr/bin/pypy3

import re
from decimal import Decimal

ms = []
with open("data/data13", "r") as f:
    gs = f.read().strip().split("\n\n")
    for g in gs:
        lines = g.strip().splitlines()
        m1 = re.search(r"X\+(\d+), Y\+(\d+)", lines[0])
        m2 = re.search(r"X\+(\d+), Y\+(\d+)", lines[1])
        m3 = re.search(r"X=(\d+), Y=(\d+)", lines[2])
        ms.append((int(m1.group(1)), int(m1.group(2)), int(m2.group(1)),
                  int(m2.group(2)), int(m3.group(1)), int(m3.group(2))))
# Part 1
res = 0
for (ax, ay, bx, by, px, py) in ms:
    best = -1
    for i in range(max(px // ax, py // ay)):
        x = i * ax
        y = i * ay
        if (px - x) % bx == 0 and (py - y) % by == 0:
            j1 = (px - x) / bx
            j2 = (py - y) / by
            if j1 < 100 and i < 100 and j1 == j2 and (best == -1 or i*3 + j1 < best):
                best = i * 3 + int(j1)
    if best != -1:
        res += best
print(res)

# Part 2
res = 0
for (x1, y1, x2, y2, xp, yp) in ms:
    xp += 10000000000000
    yp += 10000000000000
    x1 = Decimal(x1)
    x2 = Decimal(x2)
    y1 = Decimal(y1)
    y2 = Decimal(y2)
    xp = Decimal(xp)
    yp = Decimal(yp)
    # |a * x1 + b * x2 = xp  => b = (xp - a*x1) / x2
    # |a * y1 + b * y2 = yp  =======================> a = (yp - (xp*y2)/x2) / (y1 - (x1*y2)/x2)
    a = (yp - ((xp*y2)/x2)) / (y1 - ((x1*y2)/x2))
    b = (xp - a*x1) / x2
    if abs(round(a) - a) < 0.000001 and abs(round(b) - b) < 0.000001:
        res += 3*a + b
print(int(res))
