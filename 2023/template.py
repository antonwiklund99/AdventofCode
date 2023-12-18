#!/usr/bin/pypy3
import itertools
from heapq import heappop, heappush, heapify
from collections import deque
from math import floor, ceil
import re

res = 0
res2 = 0
p = 1
a = []
i = 0
j = 0
with open("data/dataXX", "r") as f:
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

print(res)