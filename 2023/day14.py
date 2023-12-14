#!/usr/bin/pypy3

a = []
with open("data/data14", "r") as f:
  for l in f:
    a.append([c for c in l.strip()])

# Part 1
for i in range(len(a)):
  for j in range(len(a[i])):
    if a[i][j] == "O":
      for k in range(i-1,-1,-1):
        if a[k][j] == ".":
          a[k][j] = "O"
          a[k+1][j] = "."
        else:
          break
res = 0
for i in range(len(a)):
  for j in range(len(a[i])):
    if a[i][j] == "O":
      res += len(a)-i
print(res)

# Part 2
history = {}
for c in range(1000000000):
  # N
  for i in range(len(a)):
    for j in range(len(a[i])):
      if a[i][j] == "O":
        for k in range(i-1,-1,-1):
          if a[k][j] == ".":
            a[k][j] = "O"
            a[k+1][j] = "."
          else:
            break
  # W
  for i in range(len(a)):
    for j in range(len(a[i])):
      if a[i][j] == "O":
        for k in range(j-1,-1,-1):
          if a[i][k] == ".":
            a[i][k] = "O"
            a[i][k+1] = "."
          else:
            break
  # S
  for i in range(len(a)-1,-1,-1):
    for j in range(len(a[i])):
      if a[i][j] == "O":
        for k in range(i+1,len(a)):
          if a[k][j] == ".":
            a[k][j] = "O"
            a[k-1][j] = "."
          else:
            break
  # E
  for i in range(len(a)):
    for j in range(len(a[i])-1,-1,-1):
      if a[i][j] == "O":
        for k in range(j+1,len(a[i])):
          if a[i][k] == ".":
            a[i][k] = "O"
            a[i][k-1] = "."
          else:
            break
  a_str = "".join(["".join(a[i]) for i in range(len(a))])
  if a_str in history:
    if (1000000000 - history[a_str] - 1) % (c-history[a_str]) == 0:
      break
  else:
    history[a_str] = c
res = 0
for i in range(len(a)):
  for j in range(len(a[i])):
    if a[i][j] == "O":
      res += len(a)-i
print(res)