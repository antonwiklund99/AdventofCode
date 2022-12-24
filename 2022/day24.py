with open("data/data24", "r") as f:
  lines = [x.strip() for x in f.readlines()]

y_len = len(lines)-2
x_len = len(lines[0])-2
blizzards = []
for (y,line) in enumerate(lines[1:-1]):
  for (x,c) in enumerate(line[1:-1]):
    if c != '.':
      blizzards.append([x,y,c])

def search(start,end):
  t = 0
  q = set()
  q.add(start)
  while len(q) > 0:
    t += 1
    empty = [[True for x in range(x_len)] for y in range(y_len)]
    for b in blizzards:
      if b[2] == '^':
        b[1] = (b[1]-1) % y_len
      elif b[2] == 'v':
        b[1] = (b[1]+1) % y_len
      elif b[2] == '<':
        b[0] = (b[0]-1) % x_len
      elif b[2] == '>':
        b[0] = (b[0]+1) % x_len
      empty[b[1]][b[0]] = False
    nq = set()
    for (x,y) in q:
      if x == end[0] and (y+1 == end[1] or y-1 == end[1]):
        return t
      if x+1 < x_len and empty[y][x+1]:
        nq.add((x+1,y))
      if x-1 >= 0 and empty[y][x-1]:
        nq.add((x-1,y))
      if y+1 < y_len and empty[y+1][x]:
        nq.add((x,y+1))
      if y-1 >= 0 and empty[y-1][x]:
        nq.add((x,y-1))
      if empty[y][x]:
        nq.add((x,y))
    q = nq

t1 = search((0,0),(x_len-1,y_len))
print(t1)
print(t1 + search((x_len-1,y_len-1),(0,-1)) + search((0,0),(x_len-1,y_len)))