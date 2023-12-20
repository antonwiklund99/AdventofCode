#!/usr/bin/pypy3
import math

def lcm(*integers):
  a = integers[0]
  for b in integers[1:]:
    a = (a * b) // math.gcd(a, b)
  return a

a = []
with open("data/data20", "r") as f:
  for l in f:
    a.append(l.strip())

d = {}
v = {}
inputs = {}
for l in a:
  [src,dst] = l.split("->")
  src = src.strip()
  dst = [ds.strip() for ds in dst.split(", ")]
  if src != "broadcaster":
    d[src[1:]] = (src[0], dst)
    v[src[1:]] = False
    src = src[1:]
  else:
    d[src] = ("b", dst)
    v[src] = False
  for ds in dst:
    if not ds in inputs:
      inputs[ds] = {}
    inputs[ds][src] = False
    if not ds in d:
      d[ds] = ("o",[])
dvi_orig = (d.copy(), v.copy(), inputs.copy())

# Part 1
hp = 0
lp = 0
for i in range(1000):
  ps = [False]
  hs = ["broadcaster"]
  while len(ps) > 0:
    new_ps = []
    new_hs = []
    for j in range(len(ps)):
      src = hs[j]
      p = ps[j]
      if not p:
        lp += 1
      else:
        hp += 1
      (op,dst) = d[src]
      if op == "b":
        for ds in dst:
          inputs[ds][src] = p
          new_hs.append(ds)
          new_ps.append(p)
      elif op == "%" and not p:
        v[src] = not v[src]
        for ds in dst:
          inputs[ds][src] = v[src]
          new_hs.append(ds)
          new_ps.append(v[src])
      elif op == "&":
        all_true = True
        for k in inputs[src].keys():
          if not inputs[src][k]:
            all_true = False
            break
        v[src] = not all_true
        for ds in dst:
          inputs[ds][src] = v[src]
          new_hs.append(ds)
          new_ps.append(v[src])
    ps = new_ps
    hs = new_hs
print(lp*hp)

# Part 2 (rx depends on &kj so find cycles for all kj's inputs that give a high pulse)
(d,v,inputs) = dvi_orig
done = False
cycles = []
found_cycles = set()
s = 0
while len(cycles) < 4:
  ps = [False]
  hs = ["broadcaster"]
  s += 1
  while len(ps) > 0:
    new_ps = []
    new_hs = []
    for j in range(len(ps)):
      src = hs[j]
      p = ps[j]
      if src == "kj":
        for k in inputs["kj"].keys():
          if inputs["kj"][k] and not k in found_cycles:
            cycles.append(s)
            found_cycles.add(k)
      (op,dst) = d[src]
      if op == "b":
        for ds in dst:
          inputs[ds][src] = p
          new_hs.append(ds)
          new_ps.append(p)
      elif op == "%" and not p:
        v[src] = not v[src]
        for ds in dst:
          inputs[ds][src] = v[src]
          new_hs.append(ds)
          new_ps.append(v[src])
      elif op == "&":
        all_true = True
        for k in inputs[src].keys():
          if not inputs[src][k]:
            all_true = False
            break
        v[src] = not all_true
        for ds in dst:
          inputs[ds][src] = v[src]
          new_hs.append(ds)
          new_ps.append(v[src])
    ps = new_ps
    hs = new_hs
print(lcm(*cycles))