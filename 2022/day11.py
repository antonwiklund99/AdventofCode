from copy import deepcopy
from math import floor

with open("data/data11", "r") as f:
  ms = [monke.strip().split("\n") for monke in f.read().split("\n\n")]

class Monke:
  def __init__(self, items, op, op_num, test_num, true_to, false_to):
    self.items = items
    if op == "*":
      self.op = lambda x : x * (x if op_num == "old" else int(op_num))
    else:
      self.op = lambda x : x + (x if op_num == "old" else int(op_num))
    self.test = lambda x : x % test_num == 0
    self.test_num = test_num
    self.true_to = true_to
    self.false_to = false_to
    self.inspects = 0

# Parse
monkeys = []
for m in ms:
  items = [int(x) for x in m[1].replace("Starting items: ", "").replace(" ", "").split(",")]
  op_str = m[2].strip().replace("Operation: new = old ", "").replace(" ", "")
  test_num = int(m[3].strip().replace("Test: divisible by", "").replace(" ", ""))
  true_to = int(m[4].strip().replace("If true: throw to monkey", "").replace(" ", ""))
  false_to = int(m[5].strip().replace("If false: throw to monkey", "").replace(" ", ""))
  monkeys.append(Monke(items, op_str[0], op_str[1:], test_num, true_to, false_to))
monkeys_original = deepcopy(monkeys)

# Part 1
for round in range(20):
  for m in monkeys:
    for item in m.items:
      level = floor(m.op(item)/3)
      m.inspects += 1
      if m.test(level):
        monkeys[m.true_to].items.append(level)
      else:
        monkeys[m.false_to].items.append(level)
    m.items = []
sorted_monke = sorted(monkeys, key=lambda m:m.inspects)
print(sorted_monke[-1].inspects * sorted_monke[-2].inspects)

# Part 2
monkeys = monkeys_original
common_multiple = 1
for m in monkeys:
  common_multiple *= m.test_num
for round in range(10000):
  for m in monkeys:
    for item in m.items:
      level = m.op(item) % common_multiple
      m.inspects += 1
      if m.test(level):
        monkeys[m.true_to].items.append(level)
      else:
        monkeys[m.false_to].items.append(level)
    m.items = []
sorted_monke = sorted(monkeys, key=lambda m:m.inspects)
print(sorted_monke[-1].inspects * sorted_monke[-2].inspects)