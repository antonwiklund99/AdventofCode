#!/usr/bin/pypy3
a = []
with open("data/data11", "r") as f:
  for l in f:
    a.append(l.strip())

empty_rows = [i for (i,l) in enumerate(a) if l.count(".") == len(l)]
empty_col = []
for j in range(len(a[0])):
  col = [a[i][j] for i in range(len(a))]
  if col.count(".") == len(col):
    empty_col.append(j)

g = []
for i in range(len(a)):
  for j in range(len(a[i])):
    if a[i][j] != ".":
      g.append((i,j))

# Part 1
res = 0
for (i,start) in enumerate(g):
  for (j,end) in enumerate(g[i+1:]):
    res += abs(start[0] - end[0]) + abs(start[1] - end[1])
    for i in empty_rows:
      if i > min(start[0],end[0]) and i < max(start[0],end[0]):
        res += 1
    for j in empty_col:
      if j > min(start[1],end[1]) and j < max(start[1],end[1]):
        res += 1
print(res)

# Part 2
res = 0
for (i,start) in enumerate(g):
  for (j,end) in enumerate(g[i+1:]):
    res += abs(start[0] - end[0]) + abs(start[1] - end[1])
    for i in empty_rows:
      if i > min(start[0],end[0]) and i < max(start[0],end[0]):
        res += 1000000 - 1
    for j in empty_col:
      if j > min(start[1],end[1]) and j < max(start[1],end[1]):
        res += 1000000 - 1
print(res)