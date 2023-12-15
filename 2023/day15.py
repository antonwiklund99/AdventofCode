#!/usr/bin/pypy3

with open("data/data15", "r") as f:
  a = [x.strip() for x in f.read().split(',')]

# Part 1
res = 0
for l in a:
  x = 0
  for c in l:
    x = ((x + ord(c)) * 17) % 256
  res += x
print(res)

# Part 2
b = [[] for _ in range(255)]
for l in a:
  if "=" in l:
    op = "="
    [s,v] = l.split("=")
  elif l[-1] == "-":
    op = "-"
    s = l[:-1]
  x = 0
  for c in s:
    x = ((x + ord(c)) * 17) % 256
  i = 0
  while i < len(b[x]):
    if b[x][i][0] == s:
      break
    i += 1
  if op == "=" and i < len(b[x]):
    b[x][i] = (s,int(v))
  elif op == "=":
    b[x].append((s,int(v)))
  elif i < len(b[x]):
    b[x] = b[x][:i] + b[x][i+1:]

res = 0
for i in range(len(b)):
  for j in range(len(b[i])):
    res += (i+1)*(j+1)*(b[i][j][1])
print(res)