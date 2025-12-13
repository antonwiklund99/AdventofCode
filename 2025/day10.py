#!/usr/bin/pypy3
import math
from itertools import product

with open("data/data10", "r") as f:
  a = []
  for l in f:
    [ls, rest] = l[1:].strip().split("]")
    [bs, js] = rest[:-1].split("{")
    a.append(([c == "#" for c in ls.strip()],
              [[int(x) for x in c[1:-1].split(",")] for c in bs.strip().split()],
              [int(c) for c in js.split(",")]))

# Part 1
def rec1(cl, bi):
    if l == cl:
        return 0
    if bi >= len(b):
        return 100_000_000
    x0 = rec1(cl, bi+1)
    for i in b[bi]:
      cl[i] = not cl[i]  
    x1 = 1 + rec1(cl, bi+1)
    for i in b[bi]:
      cl[i] = not cl[i]  
    return min(x0, x1)

res = 0
for (l,b,j) in a:
    res += rec1([False for _ in range(len(l))], 0)
print(res)

# Part 2
def print_systems(vars, eqs):
    print("VARIABLES:")
    for key in sorted(vars.keys()):
        t = []
        for i in range(len(vars[key])-1):
            if vars[key][i] != 0:
                t.append(f"{vars[key][i]}*x_{i}")
        t.append(f"{vars[key][-1]}")
        print(f"x_{key} = " + " + ".join(t))
    print("EQUATIONS")
    for eq in eqs:
        t = []
        for i in range(len(eq)-1):
            if eq[i] != 0:
                t.append(f"{eq[i]}*x_{i}")
        t.append(str(eq[-1]))
        print(f"0 = " + " + ".join(t))

def combs(xs):
    rs = [range(x + 1) for x in xs]
    yield from product(*rs)

res = 0 
for (l,b,jl) in a:
    # Create equation system
    N_VARS = len(b)
    N_EQS = len(jl)
    vars = {}
    eqs = [[0.0]*N_VARS + [-jl[i]] for i in range(N_EQS)]
    for i in range(N_VARS):
        for x in b[i]:
            eqs[x][i] = 1

    # Solve as much of equation system as possible
    for _ in range(N_EQS):
        eq = eqs.pop()
        i = 0
        while i < N_VARS and math.isclose(eq[i], 0.0, abs_tol=1e-8):
            i += 1
        if i >= N_VARS:
            continue
        ki = eq[i]
        eq[i] = 0
        for j in range(N_VARS+1):
            eq[j] = -eq[j] / ki
        vars[i] = eq
        for e in eqs:
            ki = e[i]
            e[i] = 0
            for j in range(N_VARS+1):
                if i != j:
                    e[j] += ki*eq[j]
        for key in vars:
            v = vars[key]
            ki = v[i]
            v[i] = 0
            for j in range(N_VARS+1):
                if i != j:
                    v[j] += ki*eq[j]

    # Iterate through all possible values of remaining variables
    uvars = []
    ulims = []
    for i in range(N_VARS):
        if i not in vars:
            uvars.append(i)
            ulims.append(min(map(lambda j: jl[j], b[i])))
    best = 100_000_000
    for s in combs(ulims):
        svars = {}
        bad_solution = False
        for (i,v) in enumerate(uvars):
            svars[v] = s[i]
        for i in vars:
            s = vars[i][-1]
            for j in range(len(vars[i])-1):
                if not math.isclose(vars[i][j], 0.0, abs_tol=1e-8):
                    s += vars[i][j] * svars[j]
            # check that variable is positive integer
            if not math.isclose(s, round(s), abs_tol=1e-8) or (s < 0.0 and not math.isclose(s, 0.0, abs_tol=1e-8)):
                bad_solution = True
                break
            svars[i] = int(round(s))
        if not bad_solution:
            s = sum(svars.values())
            if s < best:
                best = s
    assert best != 100_000_000
    res += best
print(res)
