#!/usr/bin/pypy3
import functools

a = []
with open("data/data21", "r") as f:
    for line in f:
        a.append(line.strip())

digit_to_pos = {
    "7": (0, 0),
    "8": (0, 1),
    "9": (0, 2),
    "4": (1, 0),
    "5": (1, 1),
    "6": (1, 2),
    "1": (2, 0),
    "2": (2, 1),
    "3": (2, 2),
    "0": (3, 1),
    "A": (3, 2),
}
dir_to_pos = {
    "^": (0, 1),
    "A": (0, 2),
    "<": (1, 0),
    "v": (1, 1),
    ">": (1, 2)
}


def str_combinations(a, b, r):
    if len(a) == 0 and len(b) == 0:
        yield r
    if len(a) > 0:
        for s in str_combinations(a[1:], b, r + a[0]):
            yield s
    if len(b) > 0:
        for s in str_combinations(a, b[1:], r + b[0]):
            yield s


def move(i, j, c):
    if c == "<":
        return (i, j - 1)
    elif c == ">":
        return (i, j + 1)
    elif c == "^":
        return (i - 1, j)
    elif c == "v":
        return (i + 1, j)
    elif c == "A":
        return (i, j)
    assert False


@functools.lru_cache(maxsize=None)
def shortest(d, prev_d, depth, max_depth):
    if depth == max_depth:
        return 1
    if depth == 0:
        (ci, cj) = digit_to_pos[prev_d]
        (wi, wj) = digit_to_pos[d]
    else:
        (ci, cj) = dir_to_pos[prev_d]
        (wi, wj) = dir_to_pos[d]
    (di, dj) = (wi - ci, wj - cj)
    seq_i = ("^" if di < 0 else "v") * abs(di)
    seq_j = ("<" if dj < 0 else ">") * abs(dj)
    res = -1
    for seq in str_combinations(seq_i, seq_j, ""):
        invalid = False
        seq_1 = seq + "A"
        res1 = 0
        (ii, jj) = (ci, cj)
        for i in range(len(seq_1)):
            (ii, jj) = move(ii, jj, seq_1[i])
            if (depth == 0 and (ii, jj) == (3, 0)) or (depth != 0 and (ii, jj) == (0, 0)):
                invalid = True
                break
            res1 += shortest(seq_1[i], "A" if i == 0 else seq_1[i - 1], depth + 1, max_depth)
        if not invalid and (res == -1 or res1 < res):
            res = res1
    assert res != -1
    return res


# Part 1
res = 0
for seq in a:
    res1 = 0
    for i in range(len(seq)):
        res1 += shortest(seq[i], "A" if i == 0 else seq[i - 1], 0, 3)
    res += int(seq[:-1]) * res1
print(res)

# Part 2
res = 0
for seq in a:
    res1 = 0
    for i in range(len(seq)):
        res1 += shortest(seq[i], "A" if i == 0 else seq[i - 1],  0, 26)
    res += int(seq[:-1]) * res1
print(res)
