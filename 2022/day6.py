with open("data/data6", "r") as f:
  lines = f.readlines()
  line = lines[0]

for i in range(len(line)):
  if len(set(line[i:i+4])) == 4:
    print(i+4)
    break

for i in range(len(line)):
  if len(set(line[i:i+14])) == 14:
    print(i+14)
    break