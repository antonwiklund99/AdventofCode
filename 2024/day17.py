#!/usr/bin/pypy3

with open("data/data17", "r") as f:
    [regs_str, program_str] = f.read().strip().split("\n\n")
    regs_str = regs_str.splitlines()
    A = int(regs_str[0].split(":")[1].strip())
    B = int(regs_str[1].split(":")[1].strip())
    C = int(regs_str[2].split(":")[1].strip())
    op_codes = [int(x.strip())
                for x in program_str.split(":")[1].strip().split(",")]

# Part 1
i = 0
output = []
while i < len(op_codes):
    assert 0 <= op_codes[i] <= 7
    x = -1
    incr_pc = True
    if op_codes[i+1] == 0:
        x = 0
    elif op_codes[i+1] == 1:
        x = 1
    elif op_codes[i+1] == 2:
        x = 2
    elif op_codes[i+1] == 3:
        x = 3
    elif op_codes[i+1] == 4:
        x = A
    elif op_codes[i+1] == 5:
        x = B
    elif op_codes[i+1] == 6:
        x = C
    if op_codes[i] == 0:
        assert x != -1
        A = A // 2**x
    elif op_codes[i] == 1:
        B = B ^ op_codes[i+1]
    elif op_codes[i] == 2:
        assert x != -1
        B = x % 8
    elif op_codes[i] == 3:
        if A != 0:
            i = op_codes[i+1]
            incr_pc = False
    elif op_codes[i] == 4:
        B = B ^ C
    elif op_codes[i] == 5:
        assert x != -1
        output.append(str(x % 8))
    elif op_codes[i] == 6:
        assert x != -1
        B = A // 2**x
    elif op_codes[i] == 7:
        assert x != -1
        C = A // 2**x
    if incr_pc:
        i += 2
print(",".join(output))

# Part 2
possible_as = [0]
for loop_idx in range(len(op_codes) - 1, -1, -1):
    next_as = []
    for possible_a in sorted(possible_as):
        for A_START in range(8 * possible_a, 8 * (possible_a + 1)):
            if A_START == 0:
                continue
            A = A_START
            B = 0
            C = 0
            i = 0
            while i < len(op_codes):
                x = -1
                incr_pc = True
                if op_codes[i+1] == 0:
                    x = 0
                elif op_codes[i+1] == 1:
                    x = 1
                elif op_codes[i+1] == 2:
                    x = 2
                elif op_codes[i+1] == 3:
                    x = 3
                elif op_codes[i+1] == 4:
                    x = A
                elif op_codes[i+1] == 5:
                    x = B
                elif op_codes[i+1] == 6:
                    x = C
                if op_codes[i] == 0:
                    A = A // 2**x
                elif op_codes[i] == 1:
                    B = B ^ op_codes[i+1]
                elif op_codes[i] == 2:
                    B = x % 8
                elif op_codes[i] == 3:
                    if A != 0:
                        i = op_codes[i+1]
                        incr_pc = False
                elif op_codes[i] == 4:
                    B = B ^ C
                elif op_codes[i] == 5:
                    if x % 8 == op_codes[loop_idx]:
                        next_as.append(A_START)
                    break
                elif op_codes[i] == 6:
                    B = A // 2**x
                elif op_codes[i] == 7:
                    C = A // 2**x
                if incr_pc:
                    i += 2
    possible_as = next_as
print(sorted(possible_as)[0])
