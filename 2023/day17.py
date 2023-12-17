#!/usr/bin/pypy3
from heapq import heappop, heappush, heapify

a = []
with open("data/data17", "r") as f:
  for l in f:
    a.append(l.strip())

RIGHT=0
LEFT=1
DOWN=2
UP=3

# Part 1
visited = [[[[False for d in range(4)] for c in range(4)] for j in range(len(a[0]))] for i in range(len(a))] # O_O
q = [(0,0,0,RIGHT,0),(0,0,0,DOWN,0)]  # (steps,i,j,dir,steps_same_dir)
heapify(q)
while True:
  (s,i,j,d,c) = heappop(q)
  if visited[i][j][c][d]:
    continue
  visited[i][j][c][d] = True
  if i == len(a)-1 and j == len(a[0])-1:
    print(s)
    break
  if d != DOWN and (d != UP or c < 3) and i-1 >= 0 and not visited[i-1][j][c+1 if d == UP else 1][UP]:
    heappush(q, (s+int(a[i-1][j]), i-1, j, UP,c+1 if d == UP else 1))
  if d != UP and (d != DOWN or c < 3) and i+1 < len(a) and not visited[i+1][j][c+1 if d == DOWN else 1][DOWN]:
    heappush(q,(s+int(a[i+1][j]), i+1, j,DOWN, c+1 if d == DOWN else 1))
  if d != RIGHT and (d != LEFT or c < 3) and j-1 >= 0 and not visited[i][j-1][c+1 if d == LEFT else 1][LEFT]:
    heappush(q,(s+int(a[i][j-1]), i, j-1, LEFT,c+1 if d == LEFT else 1))
  if d != LEFT and (d != RIGHT or c < 3) and j+1 < len(a[0]) and not visited[i][j+1][c+1 if d == RIGHT else 1][RIGHT]:
    heappush(q,(s+int(a[i][j+1]), i,j+1,RIGHT,c+1 if d == RIGHT else 1))

# Part 2
visited = [[[[False for d in range(4)] for c in range(11)] for j in range(len(a[0]))] for i in range(len(a))] # O_O
q = [(0,0,0,RIGHT,0),(0,0,0,DOWN,0)] # (steps,i,j,dir,steps_same_dir)
heapify(q)
while True:
  (s,i,j,d,c) = heappop(q)
  if visited[i][j][c][d]:
    continue
  visited[i][j][c][d] = True
  if i == len(a)-1 and j == len(a[0])-1 and c >= 4:
    print(s)
    break
  if d != DOWN and ((d != UP and c >= 4) or (d == UP and c < 10)) and i-1 >= 0 and not visited[i-1][j][c+1 if d == UP else 1][UP]:
    heappush(q, (s+int(a[i-1][j]), i-1, j, UP,c+1 if d == UP else 1))
  if d != UP and ((d != DOWN and c >= 4) or (d == DOWN and c < 10)) and i+1 < len(a) and not visited[i+1][j][c+1 if d == DOWN else 1][DOWN]:
    heappush(q,(s+int(a[i+1][j]), i+1, j,DOWN, c+1 if d == DOWN else 1))
  if d != RIGHT and ((d != LEFT and c >= 4) or (d == LEFT and c < 10)) and j-1 >= 0 and not visited[i][j-1][c+1 if d == LEFT else 1][LEFT]:
    heappush(q,(s+int(a[i][j-1]), i, j-1, LEFT,c+1 if d == LEFT else 1))
  if d != LEFT and ((d != RIGHT and c >= 4) or (d == RIGHT and c < 10)) and j+1 < len(a[0]) and not visited[i][j+1][c+1 if d == RIGHT else 1][RIGHT]:
    heappush(q,(s+int(a[i][j+1]), i,j+1,RIGHT,c+1 if d == RIGHT else 1))