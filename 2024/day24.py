#!/usr/bin/pypy3
import functools
import operator
import re

values = {}
gates = {}
with open("data/data24", "r") as f:
    [init_str, gates_str] = f.read().strip().split("\n\n")
    for line in init_str.splitlines():
        [k, v] = line.split(": ")
        values[k.strip()] = int(v.strip())
    for line in gates_str.splitlines():
        m = re.match(
            r"([a-z0-9]+) (XOR|AND|OR) ([a-z0-9]+) -> ([a-z0-9]+)", line)
        gates[m.group(4)] = (m.group(1), m.group(3), m.group(2))
    values_orig = values.copy()


def rec(k, values, depth, do_print=False):
    if k in values:
        if do_print:
            print("  " * depth + f"{k} => {values[k]}")
        return values[k]
    (w1, w2, op) = gates[k]
    x1 = rec(w1, values, depth + 1, do_print)
    x2 = rec(w2, values, depth + 1, do_print)
    res = -1
    if op == "XOR":
        res = x1 ^ x2
    elif op == "AND":
        res = x1 & x2
    elif op == "OR":
        res = x1 | x2
    assert res != -1
    if do_print:
        print("  " * depth + f"{k} => {x1} ({w1}) {op} {x2} ({w2}) = {res}")
    values[k] = res
    return res


# Part 1
res = 0
for k in filter(lambda k: k[0] == "z", gates.keys()):
    v = rec(k, values, 0)
    shift = int(k[1:])
    res |= (v << shift)
print(res)

# Part 2
values = values_orig
x = 0
y = 0
for k in values.keys():
    shift = int(k[1:])
    if k[0] == "x":
        x |= (values[k] << shift)
    elif k[0] == "y":
        y |= (values[k] << shift)

swaps = [("mkk", "z10"), ("qbw", "z14"), ("cvp", "wjb"), ("wcb", "z34")]  # Found manually going through output
for (k1, k2) in swaps:
    tmp = gates[k1]
    gates[k1] = gates[k2]
    gates[k2] = tmp

expected = x + y
res = 0
for k in sorted(filter(lambda k: k[0] == "z", gates.keys())):
    v = rec(k, values, 0, True)
    shift = int(k[1:])
    print(f"{k} = {v}, expected = {(expected >> shift) & 1}")
    res |= (v << shift)

print(f"x={x:0b}")
print(f"y={y:0b}")
print(f"z={res:0b}")
print(f"e={expected:0b}")
print(",".join(sorted(functools.reduce(operator.iconcat, swaps))))
