#!/usr/bin/pypy3
import functools
import sys

sys.setrecursionlimit(15000)

a = []
with open("data/data23", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1
@functools.lru_cache(maxsize=None)
def dfs1(h):
  if h == (len(a)-1,len(a[0])-2):
    return 0
  (i,j) = h
  res = -1
  if not visited[i+1][j] and a[i+1][j] != "#" and a[i+1][j] != "^":
    visited[i+1][j] = True
    res = max(res,1+dfs1((i+1,j)))
    visited[i+1][j] = False
  if i-1 >= 0 and not visited[i-1][j] and a[i-1][j] != "#" and a[i-1][j] != "v":
    visited[i-1][j] = True
    res = max(res,1+dfs1((i-1,j)))
    visited[i-1][j] = False
  if not visited[i][j+1] and a[i][j+1] != "#" and a[i][j+1] != "<":
    visited[i][j+1] = True
    res = max(res,1+dfs1((i,j+1)))
    visited[i][j+1] = False
  if not visited[i][j-1] and a[i][j-1] != "#" and a[i][j-1] != "<":
    visited[i][j-1] = True
    res = max(res,1+dfs1((i,j-1)))
    visited[i][j-1] = False
  return res

visited = [[False for j in range(len(a[0]))] for i in range(len(a))]
print(dfs1((0,1)))

# Part 2
def dfs2(i,j):
  if i == len(a)-1 and j == len(a[0])-2:
    return 0
  res = -1
  for (ni,nj) in paths[(i,j)].keys():
    if not visited[ni][nj]:
      visited[ni][nj] = True
      r = dfs2(ni,nj)
      if r != -1:
        res = max(res, paths[(i,j)][(ni,nj)] + r)
      visited[ni][nj] = False
  return res

crossroads = set([(0,1),(len(a)-1,len(a[0])-2)])
for i in range(1,len(a)-1):
  for j in range(1,len(a[0])-1):
    p = 0
    if a[i][j] == "." and a[i-1][j] != "." and a[i+1][j] != "." and a[i][j-1] != "." and a[i][j+1] != ".":
      crossroads.add((i,j))
paths = {k:{} for k in crossroads}
for (i,j) in crossroads:
  visited = set([(i,j)])
  q = [(i,j)]
  steps = 0
  while len(q) > 0:
    nq = []
    steps += 1
    for (hi,hj) in q:
      for d in [(1,0),(-1,0),(0,1),(0,-1)]:
        (ni,nj) = (hi+d[0],hj+d[1])
        if 0 <= ni < len(a) and not (ni,nj) in visited and a[ni][nj] != "#":
          visited.add((ni,nj))
          if (ni,nj) in crossroads:
            paths[(i,j)][(ni,nj)] = steps
            paths[(ni,nj)][(i,j)] = steps
          else:
            nq.append((ni,nj))
    q = nq
visited = [[False for j in range(len(a[0]))] for i in range(len(a))]
print(dfs2(0,1))