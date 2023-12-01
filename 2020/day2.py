#!/usr/bin/pypy3
import itertools
from collections import deque
from math import floor, ceil
import re

res = 0
a = []
with open("data/data1", "r") as f:
  #a = [x.strip() for x in f.read().split(',')]
  for l in f:
    #[a,b] = l.split(" ")
    m = re.search(r"(\d+)-(\d+) (\w)\: (\w+)",l)
    g = [group for group in m.groups()]
    count = 0
    x1 = g[3][int(g[0])-1] == g[2]
    x2 = g[3][int(g[1])-1] == g[2]
    if x1 != x2:
      res += 1
    #a.append(int(l))

  #for x in a:
  #  res += a

  #for i in range(len(a)):
  #  res += a[i]

print(res)