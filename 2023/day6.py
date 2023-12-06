#!/usr/bin/pypy3

# Part 1
t = [38,67,76,73]
d = [234,1027,1157,1236]
res = 1
for r in range(len(t)):
  w = 0
  for th in range(t[r]):
    b = th*(t[r]-th)
    if b > d[r]:
      w += 1
  res *= w
print(res)

# Part 2
t = 38677673
d = 234102711571236
res = 0
for th in range(t):
  b = th*(t-th)
  if b > d:
    res += 1
print(res)