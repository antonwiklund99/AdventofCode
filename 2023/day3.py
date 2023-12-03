#!/usr/bin/pypy3
import itertools
from collections import deque
from math import floor, ceil
import re

res = 0
a = []

with open("data/data3", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1
for i in range(len(a)):
  j = 0
  while j < len(a[i]):
    if a[i][j].isdigit():
      good = False
      k = j
      while k < len(a[i]) and a[i][k].isdigit():
        k += 1
      n = int(a[i][j:k])
      for x in range(max(0,j-1),min(len(a[i]),k+1)):
        if (i != 0 and a[i-1][x] != ".") or (i != len(a)-1 and a[i+1][x] != "."):
          good = True
          break
      if good or (j-1 >= 0 and a[i][j-1] != ".") or (k < len(a[i]) and a[i][k] != "."):
        res += n
      j = k
    else:
      j += 1
print(res)

# Part 2
d = {}
for i in range(len(a)):
  for j in range(len(a[i])):
    d[(i,j)] = []
for i in range(len(a)):
  j = 0
  while j < len(a[i]):
    if a[i][j].isdigit():
      k = j
      while k < len(a[i]) and a[i][k].isdigit():
        k += 1
      n = int(a[i][j:k])
      for x in range(max(0,j-1),min(len(a[i]),k+1)):
        if i != 0 and a[i-1][x] != ".":
          d[(i-1,x)].append(n)
        if i != len(a)-1 and a[i+1][x] != ".":
          d[(i+1,x)].append(n)
      if j-1 >= 0 and a[i][j-1] != ".":
        d[(i,j-1)].append(n)
      if k < len(a[i]) and a[i][k] != ".":
        d[(i,k)].append(n)
      j = k
    else:
      j += 1
res = 0
for k in d.keys():
  if len(d[k]) == 2:
    res += d[k][0]*d[k][1]
print(res)