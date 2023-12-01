#!/usr/bin/pypy3
import itertools
from collections import deque
from math import floor, ceil

res = 0
with open("data/data1", "r") as f:
  #a = [x.strip() for x in f.read().split(',')]
  x = []
  for l in f:
    #[a,b] = l.split(" ")
    x.append(int(l))

  for i in range(len(x)):
    for j in range(len(x)):
      for k in range(len(x)):
        if j != i and k != j:
          if x[i]+x[j]+x[k] == 2020:
            print(x[i]*x[j]*x[k])
            break