#!/usr/bin/pypy3

res = 0
res2 = 0
p = 1
a = []
i = 0
j = 0
with open("data/data5", "r") as f:
  for l in f:
    a.append(l.strip())

conv_dst = {}
conv_range = {}
while i < len(a):
  a[i] = a[i].strip()
  if len(a[i]) == 0 or a[i].isspace():
    i += 1
    continue
  s = a[i].split(" ")
  if s[0] == "seeds:":
    seeds = [int(x) for x in s[1:]]
    i += 1
  else:
    [src,dst] = s[0].split("-to-")
    conv_dst[src] = dst
    conv_range[src] = []
    i += 1
    while i < len(a) and len(a[i].strip()) > 0:
      [start_dst, start_src, length] = [int(x) for x in a[i].split(" ")]
      conv_range[src].append((start_dst,start_src,length))
      i += 1

# Part 1
res = -1
for s in seeds:
  head = "seed"
  v = s
  while head != "location":
    dst = conv_dst[head]
    dst_v = v
    for (dst_start,src_start,length) in conv_range[head]:
      if v >= src_start and v < src_start + length:
        dst_v = dst_start + (v - src_start)
    v = dst_v
    head = dst
  if res == -1 or res > v:
    res = v
print(res)

# Part 2
def search(head, start, l1):
  if head == "location":
    return [start]

  dst = conv_dst[head]
  r = []
  used = []
  for (dst_start, src_start,l2) in conv_range[head]:
    if (start < src_start and start+l1 > src_start) or  \
       (start >= src_start and start < src_start+l2) or \
       (start+l1 >= src_start and start+l1 < src_start+l2):
      src_s = max(src_start,start)
      src_l = min(src_start+l2,start+l1) - src_s
      used.append((src_s,src_l))
      r += search(dst, dst_start + (src_s - src_start), src_l)

  if len(used) == 0:
    return search(dst, start, l1)

  used = sorted(used)
  i = 0
  while i < len(used):
    (s,l) = used[i]
    if i+1 < len(used):
      ns = used[i+1][0]
    else:
      ns = start + l1
    if s+l < ns-1:
      r += search(dst, s+l, s+l-ns)
    i += 1
  if used[0][0] != start:
    r += search(dst, start, used[0][0]-start)

  return r

res = -1
i = 0
while i < len(seeds):
  r = search("seed", seeds[i], seeds[i+1])
  for v in r:
    if res == -1 or v < res:
      res = v
  i += 2

print(res)