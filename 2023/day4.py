#!/usr/bin/pypy3
from functools import lru_cache

a = []
with open("data/data4", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1
ws = []
res = 0
for l in a:
  [x,y] = l.split(":")
  [w,o] = y.split("|")
  w = set(w.strip().split(" "))
  o = o.strip().split(" ")
  r = 0
  for i in o:
    if i in w and len(i) > 0:
      r += 1
  ws.append(r)
  if r > 0:
    res += 1 << (r-1) # 2^(r-1)
print(res)

# Part 2
@lru_cache(maxsize=None)
def rec(i):
  r = 1
  for j in range(i+1,min(len(a), i+ws[i]+1)):
    r += rec(j)
  return r

res = 0
for i in range(len(a)):
  res += rec(i)
print(res)