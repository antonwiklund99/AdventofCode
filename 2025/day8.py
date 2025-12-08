#!/usr/bin/pypy3
from utils import index_where, dist

cords = []
with open("data/data8", "r") as f:
  for l in f:
    [x,y,z] = l.split(",")
    cords.append((int(x),int(y),int(z)))

dists = []
for (i,p1) in enumerate(cords):
    for p2 in cords[i+1:]:
        dists.append((dist(p1,p2), p1, p2))
dists.sort(key=lambda k:k[0])

# Part 1
ccs = [set([p]) for p in cords]
for (d,p1,p2) in dists[:1000]:
    i1 = index_where(ccs, lambda k: p1 in k)
    i2 = index_where(ccs, lambda k: p2 in k)
    if i1 != i2:
        ccs[i1] = ccs[i1].union(ccs[i2])
        ccs.pop(i2)
cc_lens = [len(cc) for cc in ccs]
cc_lens.sort(reverse=True)
print(cc_lens[0] * cc_lens[1] * cc_lens[2])

# Part 2
idx = 1000
while len(ccs) > 1:
    (d,p1,p2) = dists[idx]
    idx += 1
    i1 = index_where(ccs, lambda k: p1 in k)
    i2 = index_where(ccs, lambda k: p2 in k)
    if i1 != i2:
        ccs[i1] = ccs[i1].union(ccs[i2])
        ccs.pop(i2)
print(p1[0]*p2[0])
