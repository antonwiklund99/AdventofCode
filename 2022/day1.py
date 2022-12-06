sum = 0
elfs = []
res = 0
with open("data/data1", "r") as f:
  for l in f:
    if len(l.strip()) > 0:
      sum += int(l)
    else:
      res = max(res, sum)
      elfs.append(sum)
      sum = 0
if sum != 0:
  elfs.append(sum)
  res = max(res, sum)
print(res)
elfs.sort(reverse=True)
print(elfs[0] + elfs[1] + elfs[2])