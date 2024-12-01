#!/usr/bin/pypy3
import re

a = []
with open("data/data7.txt", "r") as f:
    for l in f:
        a.append(l.strip())

m = {}
for line in a:
    head_bag = re.search(r"(\w+ \w+) bags contain", line).group(1)
    bags = re.findall(r"(\d+) (\w+ \w+) bags?", line)
    m[head_bag] = bags

# Part 1
def has_shiny_gold(bag):
    if bag == "shiny gold":
        return True
    for (_, k) in m[bag]:
        if has_shiny_gold(k):
            return True
    return False

res = 0
for bag in m.keys():
    if bag != "shiny gold":
        if has_shiny_gold(bag):
            res += 1
print(res)

# Part 2
def num_bags(bag):
    r = 0
    for (n, k) in m[bag]:
        r += int(n) * (1 + num_bags(k))
    return r

print(num_bags("shiny gold"))
