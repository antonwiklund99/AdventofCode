import math

with open("data/data25", "r") as f:
  lines = [x.strip() for x in f.readlines()]

s = 0
for line in lines:
  n = 0
  for c in line:
    if c == '-':
      c = -1
    elif c == '=':
      c = -2
    n = n*5 + int(c)
  s += n
res = ""
for i in range(math.floor(math.log(s,5)),-1,-1):
  x = round(s/5**i)
  s -= x*5**i
  if x == -2:
    res += "="
  elif x == -1:
    res += "-"
  else:
    res += str(x)
print(res)
