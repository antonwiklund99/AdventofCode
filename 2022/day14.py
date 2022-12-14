with open("data/data14", "r") as f:
  lines = [x.strip() for x in f.readlines()]

class Line:
  def __init__(self, x1, y1, x2, y2):
    self.x1 = min(x1,x2)
    self.x2 = max(x1,x2)
    self.y1 = min(y1,y2)
    self.y2 = max(y1,y2)
  
  def contains(self, x,y):
    if x == self.x1 or x == self.x2:
      return self.y1 <= y and y <= self.y2
    if y == self.y1 or y == self.y2:
      return self.x1 <= x and x <= self.x2
    return False

end_y = -1
rocks = []
for line in lines:
  points = [(int(x),int(y)) for (x,y) in [p.strip().split(",") for p in line.split("->")]]
  end_y = max(end_y, points[0][1])
  for i in range(len(points)-1):
    end_y = max(end_y, points[i+1][1])
    rocks.append(Line(points[i][0], points[i][1], points[i+1][0], points[i+1][1]))

# Part 1
prev_len = -1
sands = set()
while prev_len != len(sands):
  prev_len = len(sands)
  sp = (500,0)
  while sp[1] < end_y:
    if not ((sp[0],sp[1]+1) in sands or any(map(lambda l:l.contains(sp[0],sp[1]+1),rocks))):
      sp = (sp[0],sp[1]+1)
    elif not ((sp[0]-1,sp[1]+1) in sands or any(map(lambda l:l.contains(sp[0]-1,sp[1]+1),rocks))):
      sp = (sp[0]-1,sp[1]+1)
    elif not ((sp[0]+1,sp[1]+1) in sands or any(map(lambda l:l.contains(sp[0]+1,sp[1]+1),rocks))):
      sp = (sp[0]+1,sp[1]+1)
    else:
      sands.add(sp)
      break
print(prev_len)

# Part 2
prev_len = -1
sands = set()
while not (500,0) in sands:
  sp = (500,0)
  while True:
    if sp[1]+1 == end_y + 2:
      sands.add(sp)
      break
    elif not ((sp[0],sp[1]+1) in sands or any(map(lambda l:l.contains(sp[0],sp[1]+1),rocks))):
      sp = (sp[0],sp[1]+1)
    elif not ((sp[0]-1,sp[1]+1) in sands or any(map(lambda l:l.contains(sp[0]-1,sp[1]+1),rocks))):
      sp = (sp[0]-1,sp[1]+1)
    elif not ((sp[0]+1,sp[1]+1) in sands or any(map(lambda l:l.contains(sp[0]+1,sp[1]+1),rocks))):
      sp = (sp[0]+1,sp[1]+1)
    else:
      sands.add(sp)
      break
print(len(sands))