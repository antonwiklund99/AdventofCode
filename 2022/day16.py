import re

with open("data/data16", "r") as f:
  lines = [x.strip() for x in f.readlines()]

neighbours = {}
flow_rates = {}
valves = []
for line in lines:
  m = re.search(r"Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? (.+)", line)
  flow_rates[m.group(1)] = int(m.group(2))
  neighbours[m.group(1)] = m.group(3).split(", ")
  if flow_rates[m.group(1)] != 0:
    valves.append(m.group(1))

dist = {}
for key in (valves + ["AA"]):
  dist[key] = {}
  visited = set([key])
  stack = [key]
  steps = 0
  while len(stack) > 0:
    new_stack = []
    steps += 1
    for h in stack:
      for n in neighbours[h]:
        if not n in visited:
          if n in valves:
            dist[key][n] = steps
          visited.add(n)
          new_stack.append(n)
    stack = new_stack

def best_rec(head, t, stack):
  if t <= 0:
    return 0
  elif len(stack) == 0:
    return t * flow_rates[head]
  res = 0
  for i in range(len(stack)):
    n = stack.pop(0)
    res = max(res, best_rec(n, t - dist[head][n] - 1, stack))
    stack.append(n)
  return res + t * flow_rates[head]

print(best_rec("AA", 30, valves))

cached_results = {}
def best_rec2(h1, h2, t1, t2, stack):
  if len(stack) % 13 == 0 and len(stack) != 0:
    print(stack)

  if t1 <= 0 and t2 <= 0:
    return 0
  elif len(stack) == 0:
    return 0
  
  cache_key = "".join(sorted(stack))
  if t1 < t2:
    time_key = t1 + (t2 << 8)
    head_key = h1 + h2
  else:
    time_key = t2 + (t1 << 8)
    head_key = h2 + h1
  if cache_key in cached_results and time_key in cached_results[cache_key] and head_key in cached_results[cache_key][time_key]:
    return cached_results[cache_key][time_key][head_key]

  res = 0
  for i in range(len(stack)):
    n = stack.pop(0)
    nt1 = t1 - dist[h1][n] - 1
    nt2 = t2 - dist[h2][n] - 1
    if nt1 > 0:
      res = max(res, nt1*flow_rates[n] + best_rec2(n, h2, nt1, t2, stack))
    if nt2 > 0:
      res = max(res, nt2*flow_rates[n] + best_rec2(h1, n, t1, nt2, stack))
    stack.append(n)

  if not cache_key in cached_results:
    cached_results[cache_key] = {}
  if not time_key in cached_results[cache_key]:
    cached_results[cache_key][time_key] = {}
  cached_results[cache_key][time_key][head_key] = res
  return res

print(best_rec2("AA", "AA", 26, 26, valves))