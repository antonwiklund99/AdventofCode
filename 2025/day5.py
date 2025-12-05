#!/usr/bin/pypy3

with open("data/data5", "r") as f:
  [a,b] = [x.strip().splitlines() for x in f.read().strip().split("\n\n")]

r = []
for ra in a:
    k = ra.split("-")
    r.append((int(k[0]), int(k[1])))

# Part 1
res = 0
for i in b:
    for (s,e) in r:
        if s <= int(i) <= e:
            res += 1
            break
print(res)

# Part 2
res = 0
rs = sorted(r, key=lambda p: p[0])
(cs,ce) = rs.pop(0)
while len(rs) > 0:
    (s,e) = rs.pop(0)
    if e <= ce:
        continue
    elif s <= ce:
        ce = e
    else:
        res += ce - cs + 1
        (cs,ce) = (s,e)
res += ce - cs + 1
print(res)
            
