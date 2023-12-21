#!/usr/bin/pypy3

a = []
with open("data/data21", "r") as f:
  for l in f:
    a.append(l.strip())


for i in range(len(a)):
  for j in range(len(a[0])):
    if a[i][j] == "S":
      s = (i,j)
      break

steps = 0
end_x = (26501365-65)//131
q = set([s])
y = 0
dy = 0
ddy = 0
while True:
  qn = set()
  for (i,j) in q:
    if a[(i-1)%len(a)][(j)%len(a[0])] != "#":
      qn.add(((i-1),j))
    if a[(i+1)%len(a)][(j)%len(a[0])] != "#":
      qn.add((i+1,j))
    if a[(i)%len(a)][(j-1)%len(a[0])] != "#":
      qn.add((i,j-1))
    if a[(i)%len(a)][(j+1)%len(a[0])] != "#":
      qn.add((i,j+1))
  q = qn
  steps += 1
  # Part 1
  if steps == 64:
    print(len(q))
  # Part 2
  # It takes exactly 131 steps between a tile in one grid to the next
  # 26501365 % 131 = 65
  # So starting at step 65 and looking at every 131 steps if we find a cycle we can use that to calculate to the end
  # The function is a 2nd degree polynomial so we are looking for a constant 2nd derivative
  if (steps-65) % 131 == 0: 
    sx = (steps-65) // 131
    old_y = y
    y = len(q)
    old_dy = dy
    dy = y - old_y
    old_ddy = ddy
    ddy = dy - old_dy
    #print(f"steps = {steps} ({sx}), y = {y}, dy = {dy}, ddy = {ddy}")
    if old_ddy == ddy:
      for i in range(end_x-sx):
        dy += ddy
        y += dy
      print(y)
      break