#!/usr/bin/pypy3

a = []
with open("data/data2", "r") as f:
    for line in f:
        a.append(line.strip())


def safe(xs):
    s = 0
    for i in range(len(xs)-1):
        diff = abs(xs[i] - xs[i+1])
        if diff > 3 or diff < 1:
            return False
        if s == 0:
            s = 1 if xs[i] < xs[i+1] else -1
        elif xs[i] < xs[i+1]:
            if s != 1:
                return False
        else:
            if s != -1:
                return False
    return True


# Part 1
res = 0
for line in a:
    xs = [int(x) for x in line.split()]
    if safe(xs):
        res += 1
print(res)

# Part 2
res = 0
for line in a:
    xs = [int(x) for x in line.split()]
    for i in range(len(xs)):
        if safe(xs[:i] + xs[i+1:]):
            res += 1
            break
print(res)
