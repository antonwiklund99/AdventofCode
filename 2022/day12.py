with open("data/data12", "r") as f:
  lines = [x.strip() for x in f.readlines()]

possible_starts = []
for i in range(len(lines)):
  for j in range(len(lines[i])):
    if lines[i][j] == "S":
      start = (j,i)
      possible_starts.append(start)
    elif lines[i][j] == "E":
      end = (j,i)
    elif lines[i][j] == "a":
      possible_starts.append((j,i))

# Fill out min distance from each tile to end (reverse bfs)
dist = [[-1]*len(lines[0]) for _ in range(len(lines))]
dist[end[1]][end[0]] = 0
h = [[c for c in line] for line in lines]
h[start[1]][start[0]] = "a"
h[end[1]][end[0]] = "z"
stack = [end]
steps = 0
while len(stack) > 0:
  new_stack = []
  steps += 1
  for (x,y) in stack:
    if x != 0 and dist[y][x-1] == -1 and ord(h[y][x-1]) >= ord(h[y][x]) - 1:
      dist[y][x-1] = steps
      new_stack.append((x-1,y))
    if x != len(h[0])-1 and dist[y][x+1] == -1 and ord(h[y][x+1]) >= ord(h[y][x]) - 1:
      dist[y][x+1] = steps
      new_stack.append((x+1,y))
    if y != 0 and dist[y-1][x] == -1 and ord(h[y-1][x]) >= ord(h[y][x]) - 1:
      dist[y-1][x] = steps
      new_stack.append((x,y-1))
    if y != len(h)-1 and dist[y+1][x] == -1 and ord(h[y+1][x]) >= ord(h[y][x]) - 1:
      dist[y+1][x] = steps
      new_stack.append((x,y+1))
  stack = new_stack

# Part 1
print(dist[start[1]][start[0]])

# Part 2
res = 1000000
for s in possible_starts:
  d = dist[s[1]][s[0]]
  if d != -1 and d < res:
    res = d
print(res)