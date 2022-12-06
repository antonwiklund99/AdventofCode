p1 = 0
p2 = 0
with open("data/data2", "r") as f:
  for l in f:
    [a,b] = l.split(" ")
    a = a.strip()
    b = b.strip()

    if b == "X":
      p1 += 1
      if a == "A":
        p1 += 3
      elif a == "C":
        p1 += 6
    elif b == "Y":
      p1 += 2
      if a == "B":
        p1 += 3
      elif a == "A":
        p1 += 6
    elif b == "Z":
      p1 += 3
      if a == "C":
        p1 += 3
      elif a == "B":
        p1 += 6

    if b == "X":
      if a == "A":
        p2 += 3
      elif a == "B":
        p2 += 1
      else:
        p2 += 2
    elif b == "Y":
      p2 += 3
      if a == "A":
        p2 += 1
      elif a == "B":
        p2 += 2
      else:
        p2 += 3
    elif b == "Z":
      p2 += 6
      if a == "A":
        p2 += 2
      elif a == "B":
        p2 += 3
      else:
        p2 += 1

print(p1)
print(p2)