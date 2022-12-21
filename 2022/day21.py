from functools import lru_cache

with open("data/data21", "r") as f:
  lines = [x.strip() for x in f.readlines()]

monkeys = {}
for line in lines:
  [m,rest] = line.split(": ")
  if rest.isdigit():
    monkeys[m] = int(rest)
  else:
    [m1,op,m2] = rest.split(" ")
    monkeys[m] = (m1,m2,op)

@lru_cache(maxsize=None)
def calc(m):
  if type(monkeys[m]) == int:
    return monkeys[m]
  (m1,m2,op) = monkeys[m]
  if op == "+":
    return calc(m1) + calc(m2)
  elif op == "-":
    return calc(m1) - calc(m2)
  elif op == "/":
    return calc(m1) // calc(m2)
  elif op == "*":
    return calc(m1) * calc(m2)

@lru_cache(maxsize=None)
def contains_humn(m):
  if m == "humn":
    return True
  if type(monkeys[m]) == int:
    return False
  (m1,m2,_) = monkeys[m]
  return contains_humn(m1) or contains_humn(m2)

def calc_humn(m,n):
  if m == "humn":
    return n
  (m1,m2,op) = monkeys[m]
  assert(contains_humn(m1) ^ contains_humn(m2))
  if contains_humn(m1):
    new_m = m1
    calc_n = calc(m2)
  else:
    new_m = m2
    calc_n = calc(m1)
  if op == "+":
    return calc_humn(new_m, n-calc_n)
  elif op == "-":
    return calc_humn(new_m, n+calc_n if new_m == m1 else calc_n-n)
  elif op == "/":
    return calc_humn(new_m, n*calc_n if new_m == m1 else calc_n//n)
  elif op == "*":
    return calc_humn(new_m, n//calc_n)

print(calc("root"))
monkeys["root"] = (monkeys["root"][0],monkeys["root"][1],"-")
print(calc_humn("root",0))