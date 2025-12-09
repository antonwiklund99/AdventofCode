#!/usr/bin/python3
from shapely.geometry import Polygon

res = 0
a = []
with open("data/data9", "r") as f:
  for l in f:
    a.append([int(x) for x in l.strip().split(",")])

# Part 1
for (i,[x0,y0]) in enumerate(a):
    for [x1,y1] in a[i+1:]:
        dx = abs(x1-x0)+1
        dy = abs(y1-y0)+1
        res = max(res, dx*dy)
print(res)

# Part 2
res = 0
poly = Polygon(a)
for (i,[x0,y0]) in enumerate(a):
    for [x1,y1] in a[i+1:]:
        rect = Polygon([(x0,y0),(x1,y0),(x1,y1),(x0,y1)])
        if poly.contains(rect):
            dx = abs(x1-x0)+1
            dy = abs(y1-y0)+1
            res = max(res, dx*dy)
print(res)
