#!/usr/bin/pypy3

with open("data/data19", "r") as f:
  e = False
  w = []
  ps = []
  for l in f:
    if len(l.strip()) == 0:
      e = True
    elif e:
      ps.append(l.strip())
    else:
      w.append(l.strip())

flows = {}
for l in w:
  [n,f] = l.split("{")
  f = f.replace("}","").split(",")
  flows[n] = f

# Part 1
res = 0
for p in ps:
  es = p.replace("{","").replace("}","").split(",")
  part = {}
  for e in es:
    [n,v] = e.split("=")
    part[n] = int(v)
  h = "in"
  while True:
    for (i,f) in enumerate(flows[h]):
      if i == len(flows[h])-1:
        h = f
        break
      (c,d) = f.split(":")
      if ">" in c:
        [n,v] = c.split(">")
        if part[n] > int(v):
          h = d
          break
      elif "<" in c:
        [n,v] = c.split("<")
        if part[n] < int(v):
          h = d
          break
    if h == "A":
      res += part["x"] + part["m"] + part["a"] + part["s"]
      break
    elif h == "R":
      break
print(res)

# Part 2
def rec(h,hi,p):
  if h == "R":
    return 0
  elif h == "A":
    return (p["x"][1]-p["x"][0]+1)*(p["m"][1]-p["m"][0]+1)*(p["a"][1]-p["a"][0]+1)*(p["s"][1]-p["s"][0]+1)
  f = flows[h][hi]
  if hi == len(flows[h])-1:
    return rec(f,0,p)
  (c,d) = f.split(":")
  res = 0
  if ">" in c:
    [n,v] = c.split(">")
    v = int(v)
    (l,u) = p[n]
    if v > l and v < u:
      p[n] = (l,v)
      res += rec(h, hi+1, p)
      p[n] = (v+1,u)
      res += rec(d, 0, p)
      p[n] = (l,u)
    elif v < l:
      res = rec(d, 0, p)
    else:
      res = rec(h, hi+1, p)
  elif "<" in c:
    [n,v] = c.split("<")
    v = int(v)
    (l,u) = p[n]
    if v > l and v < u:
      p[n] = (l,v-1)
      res += rec(d, 0, p)
      p[n] = (v,u)
      res += rec(h, hi+1, p)
      p[n] = (l,u)
    elif v > u:
      res = rec(d, 0, p)
    else:
      res = rec(h, hi+1, p)
  return res

p = {
  "x": (1,4000),
  "m": (1,4000),
  "a": (1,4000),
  "s": (1,4000)
}
print(rec("in", 0, p))