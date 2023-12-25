#!/usr/bin/pypy3
from collections import defaultdict
import random

a = []
with open("data/data25", "r") as f:
  for l in f:
    a.append(l.strip())

# https://en.wikipedia.org/wiki/Karger%27s_algorithm
def kargers(eo):
  g = [set([k]) for k in eo.keys()]
  e = [set(eo[k]) for k in eo.keys()]
  while len(g) > 2:
    i = random.randint(0,len(g)-1)
    n = random.sample(e[i],1)[0]
    j = 0
    while not n in g[j]:
      j += 1
    g[i] = g[i].union(g[j])
    e[i] = set(n for n in e[i].union(e[j]) if not n in g[i])
    g.pop(j)
    e.pop(j)
  return g
    
e = defaultdict(list)
for l in a:
  [src,dsts] = l.split(": ")
  src = src.strip()
  for dst in dsts.strip().split(" "):
    e[src].append(dst)
    e[dst].append(src)

while True:
  g = kargers(e)
  er = []
  for n1 in g[0]:
    for n2 in g[1]:
      if n1 in e[n2]:
        er.append((n1,n2))
  if len(er) <= 3:
    print(len(g[0])*len(g[1]))
    break