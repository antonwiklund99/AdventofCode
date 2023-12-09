#!/usr/bin/pypy3

a = []
with open("data/data9", "r") as f:
  for l in f:
    a.append([int(x) for x in l.strip().split(" ")])

# Part 1
res = 0
for l in a:
  diff = [l]
  j = 1
  while diff[j-1].count(0) != len(diff[j-1]):
    diff.append([])
    for i in range(len(diff[j-1])-1):
      diff[j].append(diff[j-1][i+1]-diff[j-1][i])
    j += 1
  j -= 1
  diff[j].append(0)
  j -= 1
  while j >= 0:
    diff[j].append(diff[j+1][-1]+diff[j][-1])
    j -= 1
  res += diff[0][-1]
print(res)

# Part 2
res = 0
for l in a:
  diff = [l]
  j = 1
  while diff[j-1].count(0) != len(diff[j-1]):
    diff.append([])
    for i in range(len(diff[j-1])-1):
      diff[j].append(diff[j-1][i+1]-diff[j-1][i])
    j += 1
  j -= 1
  diff[j].insert(0,0)
  j -= 1
  while j >= 0:
    diff[j].insert(0, diff[j][0]-diff[j+1][0])
    j -= 1
  res += diff[0][0]
print(res)