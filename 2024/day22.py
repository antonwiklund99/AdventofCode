#!/usr/bin/pypy3
from collections import defaultdict

a = []
with open("data/data22", "r") as f:
    for line in f:
        a.append(line.strip())

# Part 1
res = 0
seq_score = defaultdict(int)
for n_str in a:
    n = int(n_str.strip())
    seq = []
    seens_seqs = set()
    for _ in range(2000):
        prev_n = n % 10
        x = n * 64
        n = (n ^ x) % 16777216
        x = n // 32
        n = (n ^ x) % 16777216
        x = n * 2048
        n = (n ^ x) % 16777216
        # Part 2
        seq.append(str((n % 10) - prev_n))
        if len(seq) > 4:
            seq.pop(0)
        if len(seq) == 4:
            k = ",".join(seq)
            if k not in seens_seqs:
                seens_seqs.add(k)
                seq_score[k] += n % 10
    res += n
print(res)

# Part 2
res = 0
for k in seq_score.keys():
    res = max(res, seq_score[k])
print(res)
