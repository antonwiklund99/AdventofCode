import math
import functools

def index_where(arr, func):
    for i in range(len(arr)):
        if func(arr[i]):
            return i
    return "FAIL"


def dist(p1, p2):
    dx = p1[0] - p2[0]
    dy = p1[1] - p2[1]
    dz = p1[2] - p2[2]
    return math.sqrt(dx*dx + dy*dy + dz*dz)
