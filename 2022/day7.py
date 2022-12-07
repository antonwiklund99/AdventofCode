with open("data/data7", "r") as f:
  lines = f.readlines()

dirs = {}
current_dir = []
i = 0
while i < len(lines):
  s = lines[i].strip().split(" ")
  i += 1
  if s[1] == "cd":
    if s[2] == "..":
      current_dir.pop()
    else:
      current_dir.append(s[2])
      assert(dirs.get("/".join(current_dir)) == None)
      dirs["/".join(current_dir)] = []
  elif s[1] == "ls":
    cur_dir_str = "/".join(current_dir)
    while i < len(lines) and lines[i][0] != "$":
      [size,name] = lines[i].strip().split(" ")
      if size.isdigit():
        dirs[cur_dir_str].append(int(size))
      else:
        dirs[cur_dir_str].append(name)
      i += 1

sizes = {}

def size_rec(dir):
  s = 0
  for x in dirs[dir]:
    if type(x) == int:
      s += x
    else:
      assert(type(x) == str)
      s += size_rec(dir + "/" + x)
  sizes[dir] = s
  return s

size_rec("/")

# Part 1
print(sum([v for (k,v) in sizes.items() if v <= 100000]))

# Part 2
best = 70000000
space_needed = 30000000 - (70000000 - sizes["/"])
for v in sizes.values():
  if v >= space_needed and v < best:
    best = v
print(best)