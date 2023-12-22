#!/usr/bin/pypy3
from copy import deepcopy

a = []
with open("data/data22", "r") as f:
  for l in f:
    a.append(l.strip())

class Brick:
  def __init__(self, idx, x1, y1, z1, x2, y2, z2):
    self.idx = idx
    self.x1 = x1
    self.y1 = y1
    self.z1 = z1
    self.x2 = x2
    self.y2 = y2
    self.z2 = z2
    self.x_max = max(x1,x2)
    self.y_max = max(y1,y2)
    self.x_min = min(x1,x2)
    self.y_min = min(y1,y2)
  
  def __repr__(self):
        return f'(({self.x1},{self.y1},{self.z1}), ({self.x2},{self.y2},{self.z2}))'

def overlap(b1,b2):
  x_overlap = b1.x_min <= b2.x_max and b1.x_max >= b2.x_min
  y_overlap = b1.y_min <= b2.y_max and b1.y_max >= b2.y_min
  return x_overlap and y_overlap

def fall(bs):
  made_change = True
  while made_change:
    made_change = False
    bs.sort(key=lambda b:min(b.z1,b.z2))
    for i in range(len(bs)):
      min_z1 = min(bs[i].z1,bs[i].z2)
      zdiff = min_z1
      if min_z1 <= 1:
        continue
      for j in range(i):
        min_z2 = min(bs[j].z1, bs[j].z2)
        max_z2 = max(bs[j].z1, bs[j].z2)
        if overlap(bs[i],bs[j]):
          zdiff = min(zdiff, min_z1-max_z2)
          assert(zdiff > 0)
      if zdiff > 1:
        bs[i].z1 -= zdiff - 1
        bs[i].z2 -= zdiff - 1
        made_change = True

bs = []
for (idx,l) in enumerate(a):
  [p1,p2] = l.split("~")
  p1 = [int(n) for n in p1.split(",")]
  p2 = [int(n) for n in p2.split(",")]
  bs.append(Brick(idx,p1[0],p1[1],p1[2],p2[0],p2[1],p2[2]))

# Part 1
fall(bs)
bs.sort(key=lambda b:max(b.z1,b.z2))
support = [[] for i in range(len(bs))]
supported_by = [[] for i in range(len(bs))]
for i in range(len(bs)):
  max_z1 = max(bs[i].z1,bs[i].z2)
  for j in range(i+1,len(bs)):
    min_z2 = min(bs[j].z1,bs[j].z2)
    if overlap(bs[i],bs[j]) and min_z2 == max_z1+1:
      support[i].append(j)
      supported_by[j].append(i)
res = 0
integral = []
for i in range(len(bs)):
  for s in support[i]:
    if len(supported_by[s]) == 1:
      integral.append(i)
      break
print(len(bs)-len(integral))

# Part 2 (dumb)
bs_orig = deepcopy(bs)
total_falls = 0
for (idx,i) in enumerate(integral):
  if idx > 0 and idx % 100 == 0:
    print(idx/len(integral))
  bs = bs[:i] + bs[i+1:]
  fall(bs)
  for j in range(len(bs_orig)):
    if i != j:
      for b in bs:
        if b.idx == bs_orig[j].idx:
          if bs_orig[j].z1 != b.z1:
            total_falls += 1
          break
  bs = deepcopy(bs_orig)
print(total_falls)
