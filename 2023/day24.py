#!/usr/bin/pypy3
import z3

def line_intersection(line1, line2):
  xdiff = (line1[0][0] - line1[1][0], line2[0][0] - line2[1][0])
  ydiff = (line1[0][1] - line1[1][1], line2[0][1] - line2[1][1])

  def det(a, b):
      return a[0] * b[1] - a[1] * b[0]

  div = det(xdiff, ydiff)
  if div == 0:
    return None

  d = (det(*line1), det(*line2))
  x = det(d, xdiff) / div
  y = det(d, ydiff) / div
  return x, y

a = []
with open("data/data24", "r") as f:
  for l in f:
    a.append(l.strip())

h = []
ls = []
for l in a:
  [ps,vs] = l.split("@")
  [x,y,z] = [int(p) for p in ps.strip().split(", ")]
  [dx,dy,dz] = [int(d) for d in vs.strip().split(", ")]
  h.append((x,y,z,dx,dy,dz))

# Part 1
res = 0
start = 2e14
end = 4e14
for i in range(len(h)):
  (x1,y1,z1,dx1,dy1,dz1) = h[i]
  dt1 = (start-x1)/dx1
  dt2 = (end-x1)/dx1
  l1 = ((x1+dt1*dx1,y1+dt1*dy1),(x1+dt2*dx1,y1+dt2*dy1))
  for j in range(i+1,len(h)):
    (x2,y2,z2,dx2,dy2,dz2) = h[j]
    dt21 = (start-x2)/dx2
    dt22 = (end-x2)/dx2
    l2 = ((x2+dt21*dx2,y2+dt21*dy2),(x2+dt22*dx2,y2+dt22*dy2))
    intersect = line_intersection(l1,l2)
    if intersect and start <= intersect[0] <= end and start <= intersect[1] <= end and \
       ((dx1 <= 0 and intersect[0] <= x1) or (dx1 >= 0 and intersect[0] >= x1)) and \
       ((dx2 <= 0 and intersect[0] <= x2) or (dx2 >= 0 and intersect[0] >= x2)):
      res += 1
print(res)

# Part 2 (https://microsoft.github.io/z3guide/programming/Z3%20Python%20-%20Readonly/Introduction/#solvers)
s = z3.Solver()
(x,y,z,dx,dy,dz) = z3.Ints("x y z dx dy dz")
for i in range(4):
  (xi,yi,zi,dxi,dyi,dzi) = h[i]
  t = z3.Int(f"t{i}")
  s.add(x + dx*t == xi + dxi*t)
  s.add(y + dy*t == yi + dyi*t)
  s.add(z + dz*t == zi + dzi*t)
  s.add(t >= 0)
s.check()
m = s.model()
print(m.eval(x).as_long()+m.eval(y).as_long()+m.eval(z).as_long())