#!/usr/bin/pypy3
import re
from utils import is_number

with open("data/data4.txt", "r") as f:
    ps = f.read().split("\n\n")

# Part 1
res = 0
all_fields = ["byr", "iyr", "eyr", "hgt", "ecl", "hcl", "pid"]
for p in ps:
    fs = [f.split(":") for f in p.split()]
    ks = set([k for [k, _] in fs])
    if all([f in ks for f in all_fields]):
        res += 1
print(res)

# Part 2
res = 0
for p in ps:
    fs = {}
    for f in p.split():
        [k, v] = f.split(":")
        fs[k] = v
    if all([f in fs.keys() for f in all_fields]) \
       and is_number(fs["byr"]) and (1920 <= int(fs["byr"]) <= 2002) \
       and is_number(fs["iyr"]) and (2010 <= int(fs["iyr"]) <= 2020) \
       and is_number(fs["eyr"]) and (2020 <= int(fs["eyr"]) <= 2030) \
       and re.match(r"#[A-Fa-f0-9]{6}$", fs["hcl"]) \
       and re.match(r"(amb|blu|brn|gry|grn|hzl|oth)$", fs["ecl"]) \
       and re.match(r"[0-9]{9}$", fs["pid"]) \
       and is_number(fs["hgt"][:-2]) \
       and ((fs["hgt"][-2:] == "cm" and 150 <= int(fs["hgt"][:-2]) <= 193) or
            (fs["hgt"][-2:] == "in" and 59 <= int(fs["hgt"][:-2]) <= 76)):
        res += 1
print(res)
