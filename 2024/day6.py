#!/usr/bin/pypy3

def rotate_90_degrees(d):
    if d == (0, -1):
        return (1, 0)
    elif d == (1, 0):
        return (0, 1)
    elif d == (0, 1):
        return (-1, 0)
    elif d == (-1, 0):
        return (0, -1)
    print("FAIL")
    return (-1, -1)


a = []
with open("data/data6", "r") as f:
    for line in f:
        a.append(line.strip())

x = -1
y = -1
d = (-100, -100)
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] == "^":
            x = j
            y = i
            d = (0, -1)
            break
        elif a[i][j] == ">":
            x = j
            y = i
            d = (1, 0)
            break
        elif a[i][j] == "v":
            x = j
            y = i
            d = (0, 1)
            break
        elif a[i][j] == "<":
            x = j
            y = i
            d = (-1, 0)
            break
x_orig = x
y_orig = y
d_orig = d

# Part 1
ps = set()
while 0 <= x < len(a[0]) and 0 <= y < len(a):
    ps.add((x, y))
    nx = x + d[0]
    ny = y + d[1]
    if 0 > nx or nx >= len(a[0]) or 0 > ny or ny >= len(a):
        break
    if a[ny][nx] == "#":
        d = rotate_90_degrees(d)
    else:
        x = nx
        y = ny
print(len(ps))

# Part 2
res = 0
for i in range(len(a)):
    for j in range(len(a[i])):
        if a[i][j] == ".":
            (x, y, d) = (x_orig, y_orig, d_orig)
            ps = set()
            found = False
            a[i] = a[i][0:j] + "#" + a[i][j+1:]
            while 0 <= x < len(a[0]) and 0 <= y < len(a):
                ps.add((x, y, d))
                nx = x + d[0]
                ny = y + d[1]
                if 0 > nx or nx >= len(a[0]) or 0 > ny or ny >= len(a):
                    break
                if a[ny][nx] == "#":
                    d = rotate_90_degrees(d)
                else:
                    x = nx
                    y = ny
                if (x, y, d) in ps:
                    found = True
                    break
            if found:
                res += 1
            a[i] = a[i][0:j] + "." + a[i][j+1:]
print(res)
