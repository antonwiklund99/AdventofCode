from queue import Queue

with open("data/data18", "r") as f:
  lines = [x.strip() for x in f.readlines()]

cords = set()
max_x = 0
min_x = 0
max_y = 0
min_y = 0
max_z = 0
min_z = 0
for (i,line) in enumerate(lines):
  [x,y,z] = [int(n) for n in line.split(",")]
  cords.add((x,y,z))
  max_x = x if i == 0 else max(max_x,x)
  min_x = x if i == 0 else min(min_x,x)
  max_y = y if i == 0 else max(max_y,y)
  min_y = y if i == 0 else min(min_y,y)
  max_z = z if i == 0 else max(max_z,z)
  min_z = z if i == 0 else min(min_z,z)

# Part 1
res = 0
for (x,y,z) in cords:
  if not (x+1,y,z) in cords:
    res += 1
  if not (x-1,y,z) in cords:
    res += 1
  if not (x,y+1,z) in cords:
    res += 1
  if not (x,y-1,z) in cords:
    res += 1
  if not (x,y,z+1) in cords:
    res += 1
  if not (x,y,z-1) in cords:
    res += 1
print(res)

# Part 2
reachable = [[[False for z in range(min_z-1,max_z+2)] for y in range (min_y-1,max_y+2)] for x in range(min_x-1,max_x+2)]
q = Queue()
q.put((min_x-1,min_y-1,min_z-1))
reachable[min_x-1][min_y-1][min_z-1] = True
while not q.empty():
  (x,y,z) = q.get()
  if x <= max_x and not reachable[x+1][y][z] and not (x+1,y,z) in cords:
    reachable[x+1][y][z] = True
    q.put((x+1,y,z))
  if x >= min_x and not reachable[x-1][y][z] and not (x-1,y,z) in cords:
    if x-1 >= 0:
      reachable[x-1][y][z] = True
    q.put((x-1,y,z))
  if y <= max_y and not reachable[x][y+1][z] and not (x,y+1,z) in cords:
    reachable[x][y+1][z] = True
    q.put((x,y+1,z))
  if y >= min_y and not reachable[x][y-1][z] and not (x,y-1,z) in cords:
    if y-1 >= 0:
      reachable[x][y-1][z] = True
    q.put((x,y-1,z))
  if z <= max_z and not reachable[x][y][z+1] and not (x,y,z+1) in cords:
    if z-1 >= 0:
      reachable[x][y][z+1] = True
    q.put((x,y,z+1))
  if z >= min_z and not reachable[x][y][z-1] and not (x,y,z-1) in cords:
    reachable[x][y][z-1] = True
    q.put((x,y,z-1))
res = 0
for (x,y,z) in cords:
  if not (x+1,y,z) in cords and reachable[x+1][y][z]:
    res += 1
  if not (x-1,y,z) in cords and reachable[x-1][y][z]:
    res += 1
  if not (x,y+1,z) in cords and reachable[x][y+1][z]:
    res += 1
  if not (x,y-1,z) in cords and reachable[x][y-1][z]:
    res += 1
  if not (x,y,z+1) in cords and reachable[x][y][z+1]:
    res += 1
  if not (x,y,z-1) in cords and reachable[x][y][z-1]:
    res += 1
print(res)