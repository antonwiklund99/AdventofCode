#!/usr/bin/pypy3

res = 0
res2 = 0
a = []

with open("data/data2", "r") as f:
  for l in f:
    a.append(l.strip())

for l in a:
  [x,y] = l.split(":")
  x = int(x.split(" ")[1])
  d = {}
  for g in y.split(";"):
    for c in g.split(","):
      [num,col] = c.strip().split(" ")
      if not col in d:
        d[col] = 0
      d[col] = max(d[col], int(num))
  # Part 1
  if d.get("red", 0) <= 12 and d.get("green", 0) <= 13 and d.get("blue", 0) <= 14:
    res += x
  # Part 2
  p = 1
  for k in d.keys():
    p *= d[k]
  res2 += p

print(res)
print(res2)