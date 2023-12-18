#!/usr/bin/pypy3
from collections import deque

def area(corners):
  n = len(corners)
  area = 0.0
  for i in range(n):
    j = (i + 1) % n
    area += corners[i][0] * corners[j][1]
    area -= corners[j][0] * corners[i][1]
  area = abs(area) / 2.0
  return area

a = []
with open("data/data18", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1 (stupid way)
e = set([(0,0)])
p = (0,0)
for l in a:
  [d,s,c] = l.split(" ")
  for i in range(int(s)):
    if d == "R":
      p = (p[0]+1,p[1])
    elif d == "L":
      p = (p[0]-1,p[1])
    elif d == "U":
      p = (p[0],p[1]-1)
    elif d == "D":
      p = (p[0],p[1]+1)
    e.add(p)
q = deque([(1,1)])
while len(q) > 0:
  (x,y) = q.popleft()
  if not (x-1,y) in e:
    e.add((x-1,y))
    q.append((x-1,y))
  if not (x+1,y) in e:
    e.add((x+1,y))
    q.append((x+1,y))
  if not (x,y+1) in e:
    e.add((x,y+1))
    q.append((x,y+1))
  if not (x,y-1) in e:
    e.add((x,y-1))
    q.append((x,y-1))
print(len(e))

# Part 2
e = [(0,0)]
p = (0,0)
o = 0
for l in a:
  [d,s,c] = l.split(" ")
  s = int("0x" + c[2:7], 16)
  o += s
  if c[7] == "0":
    p = (p[0]+s,p[1])
  elif c[7] == "2":
    p = (p[0]-s,p[1])
  elif c[7] == "1":
    p = (p[0],p[1]+s)
  elif c[7] == "3":
    p = (p[0],p[1]-s)
  e.append(p)
print(area(e)+o/2+1)