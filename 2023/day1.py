#!/usr/bin/pypy3

res1 = 0
res2 = 0
a = []
with open("data/data1", "r") as f:
  for l in f:
    a.append(l.strip())

# Part 1
for i in range(len(a)):
  x = []
  for j in range(len(a[i])):
    if a[i][j].isdigit():
      x.append(int(a[i][j]))
  res1 += x[0]*10 + x[-1]

# Part 2
for i in range(len(a)):
  x = []
  for j in range(len(a[i])):
    if a[i][j].isdigit():
      x.append(int(a[i][j]))
    elif a[i][j:j+3] == "one":
      x.append(1)
    elif a[i][j:j+3] == "two":
      x.append(2)
    elif a[i][j:j+5] == "three":
      x.append(3)
    elif a[i][j:j+4] == "four":
      x.append(4)
    elif a[i][j:j+4] == "five":
      x.append(5)
    elif a[i][j:j+3] == "six":
      x.append(6)
    elif a[i][j:j+5] == "seven":
      x.append(7)
    elif a[i][j:j+5] == "eight":
      x.append(8)
    elif a[i][j:j+4] == "nine":
      x.append(9)
  res2 += x[0]*10 + x[-1]

print(res1)
print(res2)