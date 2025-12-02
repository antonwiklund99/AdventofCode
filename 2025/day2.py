#!/usr/bin/pypy3

with open("data/data2", "r") as f:
  a = [x.strip() for x in f.read().split(',')]

# Part 1
res = 0
for e in a:
    [x,y] = [int(x) for x in e.split("-")]
    for n in range(x,y+1):
        n_str = str(n)
        if n_str[:len(n_str)//2] == n_str[len(n_str)//2:]:
            res += n
print(res)

# Part 2
res = 0
for e in a:
    [x,y] = [int(x) for x in e.split("-")]
    for n in range(x,y+1):
        n_str = str(n)
        for l in range(1, len(n_str)//2+1):
            if n_str == n_str[:l]*(len(n_str)//l):
                res += n
                break
print(res)
