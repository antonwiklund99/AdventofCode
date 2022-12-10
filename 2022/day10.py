with open("data/data10", "r") as f:
  lines = [x.strip() for x in f.readlines()]

x = 1
samples = []
for line in lines:
  samples.append(x)
  if not "noop" in line:
    samples.append(x)
    [instr,val] = line.split(" ")
    x += int(val)
  if len(samples) > 240:
    break
  
print(20*samples[19]+60*samples[59]+100*samples[99]+140*samples[139]+180*samples[179]+220*samples[219])

row = ""
for i in range(240):
  x = samples[i]
  col = i % 40
  if col == 0 and i != 0:
    print(row)
    row = ""
  if col == x - 1 or col == x or col == x + 1:
    row += "#"
  else:
    row += " "
print(row)