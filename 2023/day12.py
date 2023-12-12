#!/usr/bin/pypy3

a = []
with open("data/data12", "r") as f:
  for l in f:
    a.append(l.strip())

cache = {}
def rec(springs, pairs_left, current_count):
  if len(springs) == 0:
    if (len(pairs_left) == 0 and current_count == 0) or (len(pairs_left) == 1 and current_count == pairs_left[0]):
      return 1
    else:
      return 0

  cache_key = springs + str(pairs_left) + str(current_count)
  if cache_key in cache:
    return cache[cache_key]

  if springs[0] == ".":
    if current_count == 0:
      ret = rec(springs[1:], pairs_left, current_count)
    elif len(pairs_left) == 0 or pairs_left[0] != current_count:
      ret = 0
    else:
      ret = rec(springs[1:], pairs_left[1:], 0)
  elif springs[0] == "#":
    current_count += 1
    if len(pairs_left) == 0 or current_count > pairs_left[0]:
      ret = 0
    else:
      ret = rec(springs[1:], pairs_left, current_count)
  elif springs[0] == "?":
    ret = 0
    # Try '#'
    if len(pairs_left) > 0 and pairs_left[0] > current_count:
      ret += rec(springs[1:], pairs_left, current_count + 1)
    # Try '.'
    if len(pairs_left) == 0 or current_count == 0:
      ret += rec(springs[1:], pairs_left, current_count)
    elif pairs_left[0] == current_count:
      ret += rec(springs[1:], pairs_left[1:], 0)

  cache[cache_key] = ret
  return ret

# Part 1
res = 0
for l in a:
  [x,y] = l.split(" ")
  b = [int(i) for i in y.split(",")]
  res += rec(x,b,0)
print(res)

# Part 2
res = 0
for l in a:
  [x,y] = l.split(" ")
  b = [int(i) for i in y.split(",")]
  pairs = [b[i % len(b)] for i in range(5*len(b))]
  springs = ""
  for i in range(len(x)*5):
    if i % len(x) == 0 and i != 0:
      springs += "?"
    springs += x[i % len(x)]
  res += rec(springs,pairs,0)
print(res)