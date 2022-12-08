with open("data/data8", "r") as f:
  lines = [x.strip() for x in f.readlines()]

# Part 1
res = 0
for (i,line) in enumerate(lines):
  for (j,c) in enumerate(line.strip()):
    if (i == 0 or j == 0 or i == len(lines) - 1 or j == len(line) - 1):
      res += 1
    else:
      if max([int(x) for x in line[:j]]) < int(c):
        res += 1
      elif max([int(x) for x in line[j+1:]]) < int(c):
        res += 1
      elif max([int(x[j]) for x in lines[:i]]) < int(c):
        res += 1
      elif max([int(x[j]) for x in lines[i+1:]]) < int(c):
        res += 1
print(res)

# Part2
res = 0
for (i,line) in enumerate(lines):
  for (j,c) in enumerate(line):
    p = 1
    if i == 0 or j == 0 or i == len(lines) - 1 or j == len(line) - 1:
      p = 0
    else:
      # up
      t = 0
      for l in reversed(lines[:i]):
        t += 1
        if int(l[j]) >= int(c):
          break
      p *= t
      t = 0
      # down
      for l in lines[i+1:]:
        t += 1
        if int(l[j]) >= int(c):
          break
      p *= t
      t = 0
      # left
      for c2 in reversed(line[:j]):
        t += 1
        if int(c2) >= int(c):
          break
      p *= t
      t = 0
      # right
      for c2 in line[j+1:]:
        t += 1
        if int(c2) >= int(c):
          break
      p *= t
      res = max(res, p)
print(res)