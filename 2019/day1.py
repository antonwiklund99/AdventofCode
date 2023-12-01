import itertools
from collections import deque
from math import floor, ceil

res1 = 0
res2 = 0
with open("data/data1", "r") as f:
  for l in f:
    #[a,b] = l.split(" ")
    n = int(l)
    res1 = floor(n/3) - 2
    while n > 0:
      n = floor(n/3) - 2
      if n > 0:
        res2 += n

print(res1)
print(res2)