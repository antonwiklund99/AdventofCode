#!/usr/bin/pypy3
import functools

a = []
with open("data/data7", "r") as f:
  for l in f:
    a.append(l.strip())

def compare(a1,a2):
  if a1[0] != a2[0]:
    return a1[0] - a2[0]
  c1 = a1[1]
  c2 = a2[1]
  for i in range(len(c1)):
    if val[c1[i]] > val[c2[i]]:
      return 1
    elif val[c1[i]] < val[c2[i]]:
      return -1
  print("ERROR")
  return 0

# Part 1
x = []
for l in a:
  [c,b] = l.split(" ")
  b = int(b)
  used = set()
  pairs = 0
  threes = 0
  fours = 0
  fives = 0
  for i in range(len(c)):
    if c[i] in used:
      continue
    m = c[i:].count(c[i])
    used.add(c[i])
    if m == 2:
      pairs += 1
    elif m == 3:
      threes = 1
    elif m == 4:
      fours = 1
    elif m == 5:
      fives = 1
  if fives > 0:
    x.append((6,c,b))
  elif fours > 0:
    x.append((5,c,b))
  elif threes > 0 and pairs > 0:
    x.append((4,c,b))
  elif threes > 0:
    x.append((3,c,b))
  elif pairs == 2:
    x.append((2,c,b))
  elif pairs == 1:
    x.append((1,c,b))
  else:
    x.append((0,c,b))

val = {
  'A': 12, 'K': 11, 'Q': 10, 'J': 9, 'T': 8, '9': 7, '8': 6, '7': 5, '6': 4, '5': 3, '4': 2, '3': 1,'2':0
}
x = sorted(x, key=functools.cmp_to_key(compare))
res = 0
for i in range(len(x)):
  res += (i+1)*x[i][2]
print(res)

# Part 2
x = []
for l in a:
  [c,b] = l.split(" ")
  b = int(b)
  used = set()
  pairs = 0
  threes = 0
  fours = 0
  fives = 0
  js = 0
  for i in range(len(c)):
    if c[i] == "J":
      js += 1
      continue
    if c[i] in used:
      continue
    m = c[i:].count(c[i])
    used.add(c[i])
    if m == 2:
      pairs += 1
    elif m == 3:
      threes = 1
    elif m == 4:
      fours = 1
    elif m == 5:
      fives = 1
  if fives > 0 or (fours > 0 and js == 1) or (threes > 0 and js == 2) or (pairs > 0 and js == 3) or js >= 4:
    x.append((6,c,b))
  elif fours > 0 or (threes > 0 and js == 1) or (pairs == 1 and js == 2) or js >= 3:
    x.append((5,c,b))
  elif (threes > 0 and pairs > 0) or (threes > 0 and js > 0) or (pairs == 2 and js == 1) or (pairs == 1 and js >= 2):
    x.append((4,c,b))
  elif threes > 0 or (pairs > 0 and js > 0) or js >= 2:
    x.append((3,c,b))
  elif pairs == 2 or (pairs == 1 and js >= 1):
    x.append((2,c,b))
  elif pairs == 1 or js >= 1:
    x.append((1,c,b))
  else:
    x.append((0,c,b))

val = {
  'A': 12, 'K': 11, 'Q': 10, 'J': -1, 'T': 8, '9': 7, '8': 6, '7': 5, '6': 4, '5': 3, '4': 2, '3': 1,'2':0
}
x = sorted(x, key=functools.cmp_to_key(compare))
res = 0
for i in range(len(x)):
  res += (i+1)*x[i][2]
print(res)