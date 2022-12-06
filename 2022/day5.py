import re

with open("data/data5", "r") as f:
  lines = f.readlines()

row_index = 0
while (not lines[row_index].strip()[0].isdigit()):
  row_index += 1
nbr_stacks = len(lines[row_index].split())

def part1():
  stacks = [[] for _ in range(nbr_stacks)]
  stack_indexes = [i for i in range(0, len(lines[row_index])) if lines[row_index][i].isdigit()]

  for i in range(row_index-1, -1, -1):
    for (si,j) in enumerate(stack_indexes):
      if not lines[i][j].isspace():
        stacks[si].append(lines[i][j])

  for i in range(row_index + 2, len(lines)):
    m = re.match(r"move (\d+) from (\d) to (\d)", lines[i])
    assert(m)
    n = int(m.group(1))
    f = int(m.group(2))-1
    t = int(m.group(3))-1

    for j in range(n):
      c = stacks[f].pop()
      stacks[t].append(c)

  res = [s[len(s)-1] for s in stacks]
  print("".join(res))

def part2():
  stacks = [[] for _ in range(nbr_stacks)]
  stack_indexes = [i for i in range(0, len(lines[row_index])) if lines[row_index][i].isdigit()]

  for i in range(row_index-1, -1, -1):
    for (si,j) in enumerate(stack_indexes):
      if not lines[i][j].isspace():
        stacks[si].append(lines[i][j])

  for i in range(row_index + 2, len(lines)):
    m = re.match(r"move (\d+) from (\d) to (\d)", lines[i])
    assert(m)
    n = int(m.group(1))
    f = int(m.group(2))-1
    t = int(m.group(3))-1

    for c in stacks[f][-n:]:
      stacks[t].append(c)
    stacks[f] = stacks[f][0:-n]

  res = [s[len(s)-1] for s in stacks]
  print("".join(res))

part1()
part2()