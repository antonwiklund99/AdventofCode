#!/usr/bin/pypy3
import math

def lcm(*integers):
  a = integers[0]
  for b in integers[1:]:
    a = (a * b) // math.gcd(a, b)
  return a

a = []
with open("data/data8", "r") as f:
  for l in f:
    a.append(l.strip())

instr = a[0]
d = {}
for l in a[2:]:
  [src,dst] = l.split(" = ")
  [left,right] = dst.replace("(","").replace(")","").split(",")
  d[src] = (left.strip(),right.strip())

# Part 1
h = "AAA"
i = 0
res = 0
while h != "ZZZ":
  h = d[h][0] if instr[i] == "L" else d[h][1]
  res += 1
  i = (i + 1) % len(instr)
print(res)

# Part 2
starts = [k for k in d.keys() if k[-1] == "A"]
cycles = {}
for start in starts:
  cycles[start] = []
  i = 0
  s = 0
  h = start
  done = False
  while not done:
    h = d[h][0] if instr[i] == "L" else d[h][1]
    s += 1
    done = False
    if h[-1] == "Z":
      cycles[start].append((s,i))
      for (_,j) in cycles[start][:-1]:
        if i == j:
          done = True
          break
    i = (i + 1) % len(instr)
# All cycles repeat directly so we can just use the first found Z
x = [cycles[k][0][0] for k in cycles.keys()]
print(lcm(*x))
