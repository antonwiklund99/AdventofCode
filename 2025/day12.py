#!/usr/bin/pypy3
import re

shapes = []
cfgs = []
with open("data/data12", "r") as f:
    ss = f.read().split("\n\n")
    for s in ss[:-1]:
        shape = []
        grid = s.splitlines()[1:]
        for i in range(len(grid)):
            for j in range(len(grid[i])):
                if grid[i][j] == "#":
                    shape.append((j,i))
        shapes.append(shape)
    for s in ss[-1].splitlines():
        m = re.match(r"(\d+)x(\d+): ([^\n]+)", s)
        assert m
        cfgs.append((int(m.group(1)), int(m.group(2)), [int(n) for n in m.group(3).split()]))

# Part 1
res = 0
for (x,y,ns) in cfgs:
    if x*y >= 9*sum(ns):
        # always fits
        res += 1
    elif x*y < sum([ns[i]*len(shapes[i]) for i in range(len(ns))]):
        # impossible fit
        continue
    else:
        # possible fit (never happens??)
        assert False
print(res)
