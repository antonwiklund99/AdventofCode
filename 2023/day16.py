#!/usr/bin/pypy3
from collections import deque

a = []
with open("data/data16", "r") as f:
  for l in f:
    a.append(l.strip())

def search(sp,sd):
  b = deque([(sp,sd)])
  ed = {}
  while len(b) > 0:
    (p,d) = b.pop()
    (dy,dx) = d
    (y,x) = p
    if x >= len(a[0]) or x < 0 or y >= len(a) or y < 0:
      continue
    if (y,x) in ed:
      if d in ed[(y,x)]:
        continue
    else:
      ed[(y,x)] = []
    ed[(y,x)].append(d)
    if a[y][x] == "\\":
      if dy == 0:
        b.append(((y+dx,x), (dx,0)))
      else:
        b.append(((y,x+dy), (0,dy)))
    elif a[y][x] == "/":
      if dy == 0:
        b.append(((y-dx,x), (-dx,0)))
      else:
        b.append(((y,x-dy), (0,-dy)))
    elif a[y][x] == "|":
      if dy == 0:
        b.append(((y-1,x), (-1,0)))
        b.append(((y+1,x), (1,0)))
      else:
        b.append(((y+dy,x+dx), d))
    elif a[y][x] == "-":
      if dx == 0:
        b.append(((y,x-1), (0,-1)))
        b.append(((y,x+1), (0,1)))
      else:
        b.append(((y+dy,x+dx), d))
    elif a[y][x] == ".":
      b.append(((y+dy,x+dx), d))
  return len(ed.keys())

# Part 1
res = search((0,0),(0,1))
print(res)

# Part 2
res = max(search((0,0),(1,0)),res)
res = max(search((0,len(a[0])-1),(0,-1)),res)
res = max(search((0,len(a[0])-1),(1,0)),res)
res = max(search((len(a)-1,len(a[0])-1),(0,-1)),res)
res = max(search((len(a)-1,len(a[0])-1),(-1,0)),res)
res = max(search((len(a)-1,0),(0,1)),res)
res = max(search((len(a)-1,0),(-1,0)),res)
for i in range(1,len(a[0])-1):
  res = max(search((0,i), (1,0)),res)
  res = max(search((len(a)-1,i), (-1,0)),res)
for i in range(1,len(a)-1):
  res = max(search((i,0), (0,1)),res)
  res = max(search((i,len(a[0])-1), (0,-1)),res)
print(res)