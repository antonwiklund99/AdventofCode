with open("data/data22", "r") as f:
  lines = [x.replace('\n',"") for x in f.readlines()]

R = 0
D = 1
L = 2
U = 3
x_len = max([len(line) for line in lines[:-2]])
y_len = len(lines[:-2])
grid = [line + " "*(x_len-len(line)) for line in lines[:-2]]
moves = lines[-1].strip()

# Part 1
right_jumps = {}
left_jumps = {}
up_jumps = {}
down_jumps = {}
for i in range(y_len):
  start = 0
  while grid[i][start].isspace():
    start += 1
  if i == 0:
    start_pos = (i,start)
  end = start + 1
  while end < x_len and not grid[i][end].isspace():
    end += 1
  left_jumps[(i,start)] = (i,end-1)
  right_jumps[(i,end-1)] = (i,start)
for i in range(x_len):
  start = 0
  while grid[start][i].isspace():
    start += 1
  end = start + 1
  while end < y_len and not grid[end][i].isspace():
    end += 1
  up_jumps[(start,i)] = (end-1,i)
  down_jumps[(end-1,i)] = (start,i)

pos = start_pos
dir = R
i = 0
while i < len(moves):
  if moves[i] == "R":
    dir = (dir + 1) % 4
    i += 1
  elif moves[i] == "L":
    dir = (dir - 1) % 4
    i += 1
  else:
    n = 0
    while i < len(moves) and moves[i].isdigit():
      n = n*10 + int(moves[i])
      i += 1
    for j in range(1,n+1):
      if dir == R:
        new_pos = right_jumps.get(pos,(pos[0],pos[1]+1))
      elif dir == L:
        new_pos = left_jumps.get(pos,(pos[0],pos[1]-1))
      elif dir == U:
        new_pos = up_jumps.get(pos,(pos[0]-1,pos[1]))
      elif dir == D:
        new_pos = down_jumps.get(pos,(pos[0]+1,pos[1]))
      if grid[new_pos[0]][new_pos[1]] == "#":
        break
      pos = new_pos
print(1000*(pos[0]+1) + 4*(pos[1]+1) + dir)

# Part 1
right_jumps = {}
left_jumps = {}
up_jumps = {}
down_jumps = {}
side = 50
for i in range(side):
  """
  # Tile 1 Up
  up_jumps[(0,side*2 + i)] = ((side,side-1-i), D)
  up_jumps[(side,side-1-i)] = ((0,side*2 + i), D)
  # Tile 1 Left
  left_jumps[(i,side*2)] = ((side,side + i), D)
  up_jumps[(side,side + i)] = ((i,side*2), R)
  # Tile 1 Right
  right_jumps[(i,side*3-1)] = ((3*side-1-i,4*side-1),L)
  right_jumps[(3*side-1-i,4*side-1)] = ((i,side*3-1),L)
  # Tile 2 Left
  left_jumps[(side+i,0)] = ((side*3-1,side*4-1-i), U)
  right_jumps[(side*3-1,side*4-1-i)] = ((side+i,0), R)
  # Tile 2 Down
  down_jumps[(2*side-1,i)] = ((3*side-1,3*side-1-i),U)
  down_jumps[(3*side-1,3*side-1-i)] = ((2*side-1,i),U)
  # Tile 3 Down
  down_jumps[(2*side-1,side+i)] = ((3*side-1-i,2*side), R)
  left_jumps[(3*side-1-i,2*side)] = ((2*side-1,side+i), U)
  # Tile 4 Right
  right_jumps[(side+i,3*side-1)] = ((2*side,4*side-1-i), D)
  up_jumps[(2*side,4*side-1-i)] = ((side+i,3*side-1), L)
  """
  # Tile 1 Up, Tile 6 Left
  up_jumps[(0,side+i)] = ((3*side+i,0), R)
  left_jumps[(3*side+i,0)] = ((0,side+i), D)
  # Tile 1 Left, Tile 5 Left
  left_jumps[(i,side)] = ((3*side-1-i,0), R)
  left_jumps[(3*side-1-i,0)] = ((i,side), R)
  # Tile 2 Right, Tile 4 Right
  right_jumps[(side-1-i,3*side-1)] = ((2*side+i,2*side-1), L)
  right_jumps[(2*side+i,2*side-1)] = ((side-1-i,3*side-1), L)
  # Tile 2 Down, Tile 3 Right
  down_jumps[(side-1,2*side+i)] = ((side+i,2*side-1), L)
  right_jumps[(side+i,2*side-1)] = ((side-1,2*side+i), U)
  # Tile 3 Left, Tile 5 Up
  left_jumps[(side+i,side)] = ((side*2,i), D)
  up_jumps[(side*2,i)] = ((side+i,side), R)
  # Tile 2 Up, Tile 6 Down
  up_jumps[(0,side*2+i)] = ((side*4-1, i), U)
  down_jumps[(side*4-1, i)] = ((0,side*2+i), D)
  # Tile 4 Down, Tile 6 Right
  down_jumps[(side*3-1,side+i)] = ((side*3+i,side-1), L)
  right_jumps[(side*3+i,side-1)] = ((side*3-1,side+i), U)
pos = start_pos
dir = R
i = 0
while i < len(moves):
  if moves[i] == "R":
    dir = (dir + 1) % 4
    i += 1
  elif moves[i] == "L":
    dir = (dir - 1) % 4
    i += 1
  else:
    n = 0
    while i < len(moves) and moves[i].isdigit():
      n = n*10 + int(moves[i])
      i += 1
    for j in range(1,n+1):
      if dir == R:
        if pos in right_jumps:
          (new_pos,new_dir) = right_jumps[pos]
        else:
          new_pos = (pos[0],pos[1]+1)
          new_dir = dir
      elif dir == L:
        if pos in left_jumps:
          (new_pos,new_dir) = left_jumps.get(pos)
        else:
          new_pos = (pos[0],pos[1]-1)
          new_dir = dir
      elif dir == U:
        if pos in up_jumps:
          (new_pos,new_dir) = up_jumps.get(pos)
        else:
          new_pos = (pos[0]-1,pos[1])
          new_dir = dir
      elif dir == D:
        if pos in down_jumps:
          (new_pos,new_dir) = down_jumps.get(pos)
        else:
          new_pos = (pos[0]+1,pos[1])
          new_dir = dir
      if grid[new_pos[0]][new_pos[1]] == "#":
        break
      pos = new_pos
      dir = new_dir
print(1000*(pos[0]+1) + 4*(pos[1]+1) + dir)