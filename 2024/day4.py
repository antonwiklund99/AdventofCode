#!/usr/bin/pypy3

a = []
with open("data/data4", "r") as f:
    for line in f.readlines():
        a.append(line.strip())


def has_word_in_dir(w, i, j, dx, dy):
    for (k, c) in enumerate(w):
        x = j + dx * k
        y = i + dy * k
        if x < 0 or x >= len(a[0]) or y < 0 or y >= len(a) or a[y][x] != c:
            return False
    return True


# Part 1
res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        for (dx, dy) in [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]:
            if has_word_in_dir("XMAS", i, j, dx, dy):
                res += 1
print(res)

# Part 2
res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        if (has_word_in_dir("MAS", i - 1, j - 1, 1, 1) or has_word_in_dir("MAS", i + 1, j + 1, -1, -1)) and \
           (has_word_in_dir("MAS", i + 1, j - 1, 1, -1) or has_word_in_dir("MAS", i - 1, j + 1, -1, 1)):
            res += 1
print(res)
