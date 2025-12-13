#!/usr/bin/pypy3
import functools
from collections import defaultdict

a = defaultdict(list)
with open("data/data11", "r") as f:
  for l in f:
      [n0, ns] = [s.strip() for s in l.split(":")]
      for n1 in ns.split():
          a[n0].append(n1)

@functools.lru_cache
def paths_between(n, end):
    if n == end:
        return 1
    r = 0
    for n1 in a[n]:
        r += paths_between(n1, end)
    return r

# Part 1
print(paths_between("you", "out"))

# Part 2
fft_out = paths_between("fft", "out")
dac_out = paths_between("dac", "out")
fft_dac = paths_between("fft", "dac")
dac_fft = paths_between("dac", "fft")
svr_dac = paths_between("svr", "dac")
svr_fft = paths_between("svr", "fft")
print(svr_fft * fft_dac * dac_out + svr_dac * dac_fft * fft_out)
