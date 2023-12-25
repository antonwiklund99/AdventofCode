#!/usr/bin/pypy3
import networkx as nx

a = []
with open("data/data25", "r") as f:
  for l in f:
    a.append(l.strip())

g = nx.Graph()
for l in a:
  [src,dsts] = l.split(": ")
  src = src.strip()
  for dst in dsts.strip().split(" "):
    g.add_edge(src,dst)

for e in nx.minimum_edge_cut(g):
  g.remove_edge(*e)
res = 1
for c in nx.connected_components(g):
  res *= len(c)
print(res)