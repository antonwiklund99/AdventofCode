from copy import deepcopy

with open("data/data23", "r") as f:
  lines = [x.strip() for x in f.readlines()]

dirs = ['n','s','w','e']
elfs = set()
for y in range(len(lines)):
  for x in range(len(lines[y])):
    if lines[y][x] == '#':
      elfs.add((x,y))
elfs_copy = deepcopy(elfs)

# Part 1
for r in range(10):
  new_elfs = set()
  for p in elfs:
    n = all([not (p[0]+i,p[1]-1) in elfs for i in range(-1,2)])
    s = all([not (p[0]+i,p[1]+1) in elfs for i in range(-1,2)])
    e = all([not (p[0]+1,p[1]+i) in elfs for i in range(-1,2)])
    w = all([not (p[0]-1,p[1]+i) in elfs for i in range(-1,2)])
    if (s and n and e and w) or not (s or n or e or w):
      new_elfs.add(p)
    else:
      for i in range(4):
        dir = dirs[(r+i) % 4]
        if dir == 'n' and n:
          new_pos = ((p[0],p[1]-1))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0],p[1]-2))
            new_pos = p
          break
        elif dir == 's' and s:
          new_pos = ((p[0],p[1]+1))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0],p[1]+2))
            new_pos = p
          break
        elif dir == 'w' and w:
          new_pos = ((p[0]-1,p[1]))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0]-2,p[1]))
            new_pos = p
          break
        elif dir == 'e' and e:
          new_pos = ((p[0]+1,p[1]))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0]+2,p[1]))
            new_pos = p
          break
      new_elfs.add(new_pos)
  assert(len(elfs) == len(new_elfs))
  elfs = new_elfs

x_min = min([x for (x,y) in elfs])
x_max = max([x for (x,y) in elfs])
y_min = min([y for (x,y) in elfs])
y_max = max([y for (x,y) in elfs])
res = 0
for y in range(y_min,y_max+1):
  for x in range(x_min,x_max+1):
    if not (x,y) in elfs:
      res += 1
print(res)

# Part 2
elfs = elfs_copy
done = False
r = 0
while not done:
  new_elfs = set()
  done = True
  for p in elfs:
    n = all([not (p[0]+i,p[1]-1) in elfs for i in range(-1,2)])
    s = all([not (p[0]+i,p[1]+1) in elfs for i in range(-1,2)])
    e = all([not (p[0]+1,p[1]+i) in elfs for i in range(-1,2)])
    w = all([not (p[0]-1,p[1]+i) in elfs for i in range(-1,2)])
    if (s and n and e and w) or not (s or n or e or w):
      new_elfs.add(p)
    else:
      done = False
      for i in range(4):
        dir = dirs[(r+i) % 4]
        if dir == 'n' and n:
          new_pos = ((p[0],p[1]-1))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0],p[1]-2))
            new_pos = p
          break
        elif dir == 's' and s:
          new_pos = ((p[0],p[1]+1))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0],p[1]+2))
            new_pos = p
          break
        elif dir == 'w' and w:
          new_pos = ((p[0]-1,p[1]))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0]-2,p[1]))
            new_pos = p
          break
        elif dir == 'e' and e:
          new_pos = ((p[0]+1,p[1]))
          if new_pos in new_elfs:
            new_elfs.remove(new_pos)
            new_elfs.add((p[0]+2,p[1]))
            new_pos = p
          break
      new_elfs.add(new_pos)
  assert(len(elfs) == len(new_elfs))
  elfs = new_elfs
  r += 1
print(r)