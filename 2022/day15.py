import re

with open("data/data15", "r") as f:
  lines = [x.strip() for x in f.readlines()]

# Part 1
row_index = 2000000
row = set()
occupied = set()
for line in lines:
  m = re.search(r"Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)", line)
  [x1,y1,x2,y2] = [int(x) for x in [m.group(1),m.group(2), m.group(3) ,m.group(4)]]
  if y1 == row_index:
    occupied.add(x1)
  if y2 == row_index:
    occupied.add(x2)
  x_diff = abs(x1-x2)
  y_diff = abs(y1-y2)
  dist = x_diff + y_diff
  if y1 <= row_index and row_index <= y1 + dist:
    for i in range(dist-(row_index-y1)+1):
      row.add(x1+i)
      row.add(x1-i)
  elif y1 >= row_index and row_index >= y1 - dist:
    for i in range(dist-(y1-row_index)+1):
      row.add(x1+i)
      row.add(x1-i)
for x in occupied:
  if x in row:
    row.remove(x)
print(len(row))

# Part 2
max_cord = 4000000
areas = []
for line in lines:
  m = re.search(r"Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)", line)
  areas.append([int(x) for x in [m.group(1),m.group(2), m.group(3) ,m.group(4)]])

res = -1
for y in range(max_cord+1):
  if y % 1000000 == 0:
    print(y)

  line_segments = []
  for area in areas:
    [x1,y1,x2,y2] = area
    x_diff = abs(x1-x2)
    y_diff = abs(y1-y2)
    dist = x_diff + y_diff
    if y1 <= y and y <= y1 + dist or y1 >= y and y >= y1 - dist:
      start = max(x1-(dist-abs(y-y1)),0)
      end = min(x1+(dist-abs(y-y1)),max_cord)
      line_segments.append((start,end))
  line_segments.sort(key=lambda l:l[0])
  (start,end) = line_segments[0]
  if (start != 0):
    print("start != 0 " + str(start) + " " + str(y))
    res = (start + 1)*4000000+y
    break
  for i in range(1,len(line_segments)):
    (s,e) = line_segments[i]
    if s > end:
      print("s > end " + str(end) + " " + str(s) + " " + str(y))
      res = (end + 1)*4000000+y
      break
    end = max(end, e)
    if end >= max_cord:
      break
  if res != -1:
    break
  if end < max_cord:
    print("end < max_cord " + str(end) + " " + str(y))
    res = (end + 1)*4000000+y
    break
print(res)