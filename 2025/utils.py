def index_where(arr, func):
    for i in range(len(arr)):
        if func(arr):
            return i
    return "FAIL"


def is_number(s):
    for c in s:
        if not c.isdigit():
            return False
    return True
