#!/usr/bin/pypy3
import itertools
from collections import deque
from math import floor, ceil
import re

res = 0
a = []
i = 0
j = 0
with open("data/data1", "r") as f:
  #a = [x.strip() for x in f.read().split(',')]
  for l in f:
    #a.append(int(l.strip()))
    a.append(l.strip())

#for x in a:
#  res += a

#for i in range(len(a)):
#  res += a[i]

#for l in a:
#  m = re.search(r"(\d+)-(\d+) (\w)\: (\w+)",l)
#  g = [group for group in m.groups()]
#  [x,y] = l.split(" ")
res2 = 1
for (dx,dy) in [(1,1),(3,1),(5,1),(7,1),(1,2)]:
  i = 0
  j = 0
  res = 0
  while i < len(a):
    if a[i][j] == "#":
      res += 1
    i += dy
    j = (j + dx) % len(a[0])
  if res != 0:
    res2 *= res

print(res2)