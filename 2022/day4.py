with open("data/data4", "r") as f:
  lines = f.readlines()

def part1():
  res = 0
  for l in lines:
    [e1,e2] = l.strip().split(",")
    [l1,u1] = [int(x) for x in e1.split("-")]
    [l2,u2] = [int(x) for x in e2.split("-")]
    if l1 >= l2 and u1 <= u2:
      res += 1
    elif l2 >= l1 and u2 <= u1:
      res += 1
  print(res)

def part2():
  res = 0
  for l in lines:
    [e1,e2] = l.strip().split(",")
    [l1,u1] = [int(x) for x in e1.split("-")]
    [l2,u2] = [int(x) for x in e2.split("-")]
    if l1 >= l2 and l1 <= u2:
      res += 1
    elif u1 >= l2 and u1 <= u2:
      res += 1
    elif l2 >= l1 and l2 <= u1:
      res += 1
    elif u2 >= l1 and u2 <= u1:
      res += 1
  print(res)

part1()
part2()