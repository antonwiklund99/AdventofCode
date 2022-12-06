with open("data/data3", "r") as f:
  lines = f.readlines()

def part1():
  res = 0
  for l in lines:
    c1 = l[0:len(l)//2]
    c2 = l[len(l)//2:]
    common = set()
    for x in c1:
      for y in c2:
        if x == y:
          common.add(x)
    for x in common:
      if x.isupper():
        res += ord(x) - ord('A') + 27
      else:
        res += ord(x) - ord('a') + 1
  print(res)

def part2():
  res = 0
  group = set()
  for (i,l) in enumerate(lines):
    common = set(l.strip())
    if i % 3 == 0:
      group = common
    else:
      new_group = set()
      for x in common:
        if x in group:
          new_group.add(x)
      group = new_group
      if i % 3 == 2:
        assert(len(group) == 1)
        x = list(group)[0]
        if x.isupper():
          res += ord(x) - ord('A') + 27
        else:
          res += ord(x) - ord('a') + 1
  print(res)

part1()
part2()