with open("data/data9", "r") as f:
  lines = [x.strip() for x in f.readlines()]

# Part 1
visited = set([(0,0)])
h = (0,0)
t = (0,0)
for line in lines:
  [d,n] = line.split(" ")
  n = int(n)
  for i in range(n):
    prev_h = h
    if d == "U":
      h = (h[0],h[1]+1)
    elif d == "D":
      h = (h[0],h[1]-1)
    elif d == "L":
      h = (h[0]-1,h[1])
    elif d == "R":
      h = (h[0]+1,h[1])
    
    if abs(h[0]-t[0]) > 1 or abs(h[1]-t[1]) > 1:
      t = prev_h
      visited.add(t)
print(len(visited))

# Part 2
visited = set([(0,0)])
k = [(0,0)]*10
for line in lines:
  [d,n] = line.split(" ")
  for i in range(int(n)):
    if d == "U":
      k[0] = (k[0][0],k[0][1]+1)
    elif d == "D":
      k[0] = (k[0][0],k[0][1]-1)
    elif d == "L":
      k[0]= (k[0][0]-1,k[0][1])
    elif d == "R":
      k[0] = (k[0][0]+1,k[0][1])
    
    for j in range(1,10):
      # right
      if k[j][0] - k[j-1][0] > 1:
        if k[j][1] > k[j-1][1]:
          k[j] = (k[j][0]-1,k[j][1]-1)
        elif k[j][1] < k[j-1][1]:
          k[j] = (k[j][0]-1,k[j][1]+1)
        else:
          k[j] = (k[j][0]-1,k[j][1])
      # left
      elif k[j][0] - k[j-1][0] < -1:
        if k[j][1] > k[j-1][1]:
          k[j] = (k[j][0]+1,k[j][1]-1)
        elif k[j][1] < k[j-1][1]:
          k[j] = (k[j][0]+1,k[j][1]+1)
        else:
          k[j] = (k[j][0]+1,k[j][1])
      # up
      elif k[j][1] - k[j-1][1] > 1:
        if k[j][0] > k[j-1][0]:
          k[j] = (k[j][0]-1,k[j][1]-1)
        elif k[j][0] < k[j-1][0]:
          k[j] = (k[j][0]+1,k[j][1]-1)
        else:
          k[j] = (k[j][0],k[j][1]-1)
      # down
      elif k[j][1] - k[j-1][1] < -1:
        if k[j][0] > k[j-1][0]:
          k[j] = (k[j][0]-1,k[j][1]+1)
        elif k[j][0] < k[j-1][0]:
          k[j] = (k[j][0]+1,k[j][1]+1)
        else:
          k[j] = (k[j][0],k[j][1]+1)

      if j == 9:
        visited.add(k[j])

print(len(visited))