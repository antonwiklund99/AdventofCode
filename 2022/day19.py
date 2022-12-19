import re

with open("data/data19", "r") as f:
  sections = f.read().split("\n")

blueprints = []
for section in sections:
  blueprint = {}
  m = re.search(r"ore robot costs (\d+) ore", section)
  assert(m)
  blueprint["ore"] = {"ore": int(m.group(1))}
  m = re.search(r"clay robot costs (\d+) ore", section)
  assert(m)
  blueprint["clay"] = {"ore": int(m.group(1))}
  m = re.search(r"obsidian robot costs (\d+) ore and (\d+) clay", section)
  assert(m)
  blueprint["obsidian"] = {"ore": int(m.group(1)), "clay": int(m.group(2))}
  m = re.search(r"geode robot costs (\d+) ore and (\d+) obsidian", section)
  assert(m)
  blueprint["geode"] = {"ore": int(m.group(1)), "obsidian": int(m.group(2))}
  blueprints.append(blueprint)

def best(blueprint, t, ore, clay, obsidian, geode, ore_robots, clay_robots, obsidian_robots, geode_robots):
  if t == 0:
    return geode
  cache_key = (ore << 56) + (clay << 48) + (obsidian << 40) + (geode << 32) + (ore_robots << 24) + (clay_robots << 16) + (obsidian_robots << 8) + geode_robots
  if t > 20:
    print(f"t = {t}")
  if cache_key in cache[t]:
    return cache[t][cache_key]
  res = -1
  new_ore = ore + ore_robots
  new_clay = clay + clay_robots
  new_obsidian = obsidian + obsidian_robots
  new_geode = geode + geode_robots 
  if blueprint["geode"]["ore"] <= ore and blueprint["geode"]["obsidian"] <= obsidian:
    res = max(res, best(blueprint, t-1, new_ore-blueprint["geode"]["ore"], new_clay, new_obsidian-blueprint["geode"]["obsidian"], new_geode, ore_robots, clay_robots, obsidian_robots, geode_robots+1))
  elif blueprint["obsidian"]["ore"] <= ore and blueprint["obsidian"]["clay"] <= clay:
    res = max(res, best(blueprint, t-1, new_ore-blueprint["obsidian"]["ore"], new_clay-blueprint["obsidian"]["clay"], new_obsidian, new_geode, ore_robots, clay_robots, obsidian_robots+1, geode_robots))
  else:
    if blueprint["ore"]["ore"] <= ore:
      res = max(res, best(blueprint, t-1, new_ore-blueprint["ore"]["ore"], new_clay, new_obsidian, new_geode, ore_robots+1, clay_robots, obsidian_robots, geode_robots))
    if blueprint["clay"]["ore"] <= ore:
      res = max(res, best(blueprint, t-1, new_ore-blueprint["clay"]["ore"], new_clay, new_obsidian, new_geode, ore_robots, clay_robots+1, obsidian_robots, geode_robots))
  res = max(res, best(blueprint, t-1, new_ore, new_clay, new_obsidian, new_geode, ore_robots, clay_robots, obsidian_robots, geode_robots))
  cache[t][cache_key] = res
  return res

# Part 1
cache = [{} for t in range(24+1)]
res = 0
for (i,b) in enumerate(blueprints):
  s = best(b, 24, 0, 0, 0, 0, 1, 0, 0, 0)
  res += s * (i+1)
  cache = [{} for t in range(24+1)]
  print(f"{i} {str(b)} -> {s}")
print(res)

# Part 2
res = 1
cache = [{} for t in range(32+1)]
for (i,b) in enumerate(blueprints[2:3]):
  cache = [{} for t in range(32+1)]
  s = best(b, 32, 0, 0, 0, 0, 1, 0, 0, 0)
  res *= s
  print(f"{i} {str(b)} -> {s}")
print(res)