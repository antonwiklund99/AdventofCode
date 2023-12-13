#!/usr/bin/pypy3

a = []
x = []
with open("data/data13", "r") as f:
  for l in f:
    if len(l.strip()) == 0:
      x.append(a)
      a = []
    else:
      a.append(l.strip())
x.append(a)

def find_line(a, sol_ignore=None):
  m_row = "".join(a)
  m_col = ""
  for j in range(len(a[0])):
    for i in range(len(a)):
      m_col += a[i][j]
  for i in range(1,len(a)):
    if i*100 == sol_ignore:
      continue
    di = min(len(a) - i, i)
    s1 = m_row[(i-di)*len(a[0]):i*len(a[0])]
    s2 = m_row[i*len(a[0]):(i+di)*len(a[0])][::-1]
    assert(len(s1) == len(s2))
    m = True
    for j in range(len(s1)//len(a[0])):
      if s1[j*len(a[0]):(j+1)*len(a[0])] != s2[j*len(a[0]):(j+1)*len(a[0])][::-1]:
        m = False
        break
    if m:
      return i*100
  for i in range(1,len(a[0])):
    if i == sol_ignore:
      continue
    di = min(len(a[0]) - i, i)
    s1 = m_col[(i-di)*len(a):i*len(a)]
    s2 = m_col[i*len(a):(i+di)*len(a)][::-1]
    assert(len(s1) == len(s2))
    m = True
    for j in range(len(s1)//len(a)):
      if s1[j*len(a):(j+1)*len(a)] != s2[j*len(a):(j+1)*len(a)][::-1]:
        m = False
        break
    if m:
      return i
  return 0

# Part 1
res = 0
for a in x:
  res += find_line(a)
print(res)

# Part 2
res = 0
for a in x:
  done = False
  og_line = find_line(a)
  for i in range(len((a))):
    for j in range(len(a[i])):
      c = a[i][j]
      a[i] = a[i][:j] + ("." if c == "#" else "#") + a[i][j+1:]
      r = find_line(a, og_line)
      if r != 0:
        res += r
        done = True
        break
      a[i] = a[i][:j] + c + a[i][j+1:]
    if done:
      break
  assert(done)
print(res)