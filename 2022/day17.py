with open("data/data17", "r") as f:
  lines = [x.strip() for x in f.readlines()]
  dirs = [x for x in lines[0]]

grid = [[False for _ in range(0,7)] for __ in range(0,10000)]

def stuck(x, y ,rock):
  for (dx,dy) in rock:
    nx = x + dx
    ny = y + dy
    if nx < 0 or nx >= 7:
      return True
    if ny < 0:
      return True
    if grid[ny][nx]:
      return True
  return False

def place(x, y, rock):
  max_y = 0
  for (dx,dy) in rock:
    nx = x + dx
    ny = y + dy
    grid[ny][nx] = True
    max_y = max(max_y, ny)
  return max_y

rock_shapes = [
  [(0,0),(1,0),(2,0),(3,0)],
  [(0,1),(1,0),(1,1),(2,1),(1,2)],
  [(0,0),(1,0),(2,0),(2,1),(2,2)],
  [(0,0),(0,1),(0,2),(0,3)],
  [(0,0),(1,0),(0,1),(1,1)]
]

# Part 1
height = 0
rock_index = 0
dir_index = 0
for round in range(2022):
  x = 2
  y = height + 3
  rock = rock_shapes[rock_index]
  while True:
    d = dirs[dir_index]
    dir_index = (dir_index + 1) % len(dirs)
    new_x = x - 1 if d == "<" else x + 1
    if stuck(new_x,y,rock):
      if stuck(x,y-1,rock):
        height = max(height, place(x, y, rock)+1)
        break
    elif stuck(new_x,y-1,rock):
      height = max(height, place(new_x, y, rock)+1)
      break
    else:
      x = new_x
    y -= 1
  rock_index = (rock_index + 1) % len(rock_shapes)
print(height)

# Part 2
steps_til_repeat = len(rock_shapes)*len(dirs)
grid = [[False for _ in range(0,7)] for __ in range(0,1000000)]
height = 0
rock_index = 0
dir_index = 0
i = 0
while True:
  i += 1
  x = 2
  y = height + 3
  rock = rock_shapes[rock_index]
  while True:
    d = dirs[dir_index]
    dir_index = (dir_index + 1) % len(dirs)
    new_x = x - 1 if d == "<" else x + 1
    if stuck(new_x,y,rock):
      if stuck(x,y-1,rock):
        height = max(height, place(x, y, rock)+1)
        break
    elif stuck(new_x,y-1,rock):
      height = max(height, place(new_x, y, rock)+1)
      break
    else:
      x = new_x
    y -= 1
  rock_index = (rock_index + 1) % len(rock_shapes)
  if i > 0 and (all(grid[height-1]) or i == 1596 + 274 or i == 1596 + 1730 + 274):
    print(f"i = {i}, i % repeat = {i % steps_til_repeat} h = {height-1}")
    print("".join(["#" if b else "." for b in grid[height]]))
    print("".join(["#" if b else "." for b in grid[height-1]]))
    print("".join(["#" if b else "." for b in grid[height-2]]))
    print()
    if i > 10000:
      break

"""
Lite sketchy lösning
Från printen, kan man se att mönstret
.......
#######
.#..###
Upprepas hela tiden, vilket innebär att allt mellan det indexen också upprepas, från det kan man räkna ut h
start i = 1596
start värde = 2433
repeat varje i = 1730
h per iteration = 2647
antal iterationer = floor((1000000000000-1596)/1730)
i kvar = (1000000000000-1596) % 1730
värde ökning från i -> i + 274 = 413
total h = start värde + iterationer*h per iteration + värde ökning slut = 2433 + 578034681*2647 + 413 = 1530057803453
"""