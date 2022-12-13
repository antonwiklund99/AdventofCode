import functools

with open("data/data13", "r") as f:
  lines = [x.strip() for x in f.readlines()]

def parse(s,i):
  res = []
  assert(s[i] == "[")
  i += 1
  while i < len(s) and s[i] != "]":
    if s[i] == "[":
      (i,l) = parse(s,i)
      i += 1
      res.append(l)
    elif s[i].isdigit():
      n = 0
      while i < len(s) and s[i].isdigit():
        n = n*10 + int(s[i])
        i += 1
      res.append(n)
    else:
      i += 1
  return (i,res)

def compare(p1,p2):
  if type(p1) == int and type(p2) == int:
    if p1 < p2:
      return 1
    elif p1 > p2:
      return -1
    else:
      return 0

  if type(p1) == int:
    p1 = [p1]
  if type(p2) == int:
    p2 = [p2]
    
  for i in range(min(len(p1), len(p2))):
    comp = compare(p1[i],p2[i])
    if comp != 0:
      return comp
  
  if len(p1) < len(p2):
    return 1
  elif len(p1) > len(p2):
    return -1
  else:
    return 0

packets = []
for i in range(len(lines)):
  if i % 3 != 2:
    (i,l) = parse(lines[i],0)
    packets.append(l)

# Part 1
res = 0
for i in range(0, len(packets), 2):
  if compare(packets[i],packets[i+1]) != -1:
    res += i//2 + 1
print(res)

# Part 2
res = 1
packets.append([[2]])
packets.append([[6]])
packets.sort(key=functools.cmp_to_key(compare), reverse=True)
for (i,p) in enumerate(packets):
  if p == [[2]]:
    res *= i+1
  elif p == [[6]]:
    res *= i+1
print(res)