#!/usr/bin/pypy3
from collections import deque

a = []
with open("data/data10", "r") as f:
  for l in f:
    a.append(l.strip())

for i in range(len(a)):
  for j in range(len(a[i])):
    if a[i][j] == "S":
      s = (i,j)

# Part 1
steps = 1
q = []
up = False
down = False
left = False
right = False
if a[s[0]][s[1]-1] == "-" or a[s[0]][s[1]-1] == "L" or a[s[0]][s[1]-1] == "F":
  q.append((s[0],s[1]-1))
  left = True
if a[s[0]][s[1]+1] == "-" or a[s[0]][s[1]+1] == "7" or a[s[0]][s[1]+1] == "J":
  q.append((s[0],s[1]+1))
  right = True
if a[s[0]-1][s[1]] == "|" or a[s[0]-1][s[1]] == "F" or a[s[0]-1][s[1]] == "7":
  q.append((s[0]-1,s[1]))
  up = True
if a[s[0]+1][s[1]] == "|" or a[s[0]+1][s[1]] == "L" or a[s[0]+1][s[1]] == "J":
  q.append((s[0]+1,s[1]))
  down = True
prev = [s for _ in range(len(q))]
loop = set([s] + q)
while len(q) > 0:
  q_new = []
  prev_new = []
  for (idx,h) in enumerate(q):
    (i,j) = h
    p = prev[idx]
    if a[i][j] == "|":
      if p == (i-1,j):
        prev_new.append((i,j))
        q_new.append((i+1,j))
      else:
        prev_new.append((i,j))
        q_new.append((i-1,j))
    elif a[i][j] == "-":
      if p == (i,j-1):
        prev_new.append((i,j))
        q_new.append((i,j+1))
      else:
        prev_new.append((i,j))
        q_new.append((i,j-1))
    elif a[i][j] == "L":
      if p == (i-1,j):
        prev_new.append((i,j))
        q_new.append((i,j+1))
      else:
        prev_new.append((i,j))
        q_new.append((i-1,j))
    elif a[i][j] == "J":
      if p == (i-1,j):
        prev_new.append((i,j))
        q_new.append((i,j-1))
      else:
        prev_new.append((i,j))
        q_new.append((i-1,j))
    elif a[i][j] == "7":
      if p == (i+1,j):
        prev_new.append((i,j))
        q_new.append((i,j-1))
      else:
        prev_new.append((i,j))
        q_new.append((i+1,j))
    elif a[i][j] == "F":
      if p == (i+1,j):
        prev_new.append((i,j))
        q_new.append((i,j+1))
      else:
        prev_new.append((i,j))
        q_new.append((i+1,j))
  prev = prev_new
  q = [x for x in q_new if not x in loop]
  for x in q:
    loop.add(x)
  steps += 1
print(steps-1)

# Part 2
new_loop = set()
for (i,j) in loop:
  new_loop.add((i*2,j*2))
  if a[i][j] == "-" or (a[i][j] == "S" and left and right):
    new_loop.add((i*2,j*2-1))
    new_loop.add((i*2,j*2+1))
  elif a[i][j] == "|" or (a[i][j] == "S" and up and down):
    new_loop.add((i*2-1,j*2))
    new_loop.add((i*2+1,j*2))
  elif a[i][j] == "L" or (a[i][j] == "S" and up and right):
    new_loop.add((i*2-1,j*2))
    new_loop.add((i*2,j*2+1))
  elif a[i][j] == "J" or (a[i][j] == "S" and up and left):
    new_loop.add((i*2-1,j*2))
    new_loop.add((i*2,j*2-1))
  elif a[i][j] == "7" or (a[i][j] == "S" and down and left):
    new_loop.add((i*2+1,j*2))
    new_loop.add((i*2,j*2-1))
  elif a[i][j] == "F" or (a[i][j] == "S" and down and right):
    new_loop.add((i*2+1,j*2))
    new_loop.add((i*2,j*2+1))
loop = new_loop

outside = set()
q = deque()
q.append((-1,-1))
while len(q) > 0:
  (i,j) = q.popleft()
  if i+1 < len(a)*2+1 and not (i+1,j) in loop and not (i+1,j) in outside:
    outside.add((i+1,j))
    q.append((i+1,j))
  if i-1 > -2 and not (i-1,j) in loop and not (i-1,j) in outside:
    outside.add((i-1,j))
    q.append((i-1,j))
  if j+1 < len(a[0])*2+1 and not (i,j+1) in loop and not (i,j+1) in outside:
    outside.add((i,j+1))
    q.append((i,j+1))
  if j-1 > -2 and not (i,j-1) in loop and not (i,j-1) in outside:
    outside.add((i,j-1))
    q.append((i,j-1))

res = 0
for i in range(len(a)):
  for j in range(len(a[0])):
    p = (i*2,j*2)
    if not p in loop and not p in outside:
      res += 1
print(res)
