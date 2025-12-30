import os
import serial
import argparse
import time

DATA_DIR =  os.path.join(os.path.dirname(__file__), "../data/")

def day1(ser, use_sample):
    ser.xonxoff = False # TODO: fix
    input_file = "sample1" if use_sample else "data1"
    with open(os.path.join(DATA_DIR, input_file), "r") as f:
        for line in f:
            ser.write(line[0].encode('utf-8'))
            ser.write(int(line[1:]).to_bytes(2, byteorder='little'))
        ser.write(b"\xff\xff\xff")
    res1 = int.from_bytes(ser.read(2), byteorder='little')
    res2 = int.from_bytes(ser.read(2), byteorder='little')
    print(f"Part 1: {res1}")
    print(f"Part 2: {res2}")

def day2(ser, use_sample):
    input_file = "sample2" if use_sample else "data2"
    with open(os.path.join(DATA_DIR, input_file), "r") as f:
        line = f.read().strip()
    for p in line.split(","):
        [a,b] = [int(x) for x in p.split("-")]
        ser.write(a.to_bytes(5, byteorder="little"))
        ser.write(b.to_bytes(5, byteorder="little"))

    # TODO: fix
    time.sleep(1)
    ser.xonxoff = False

    ser.write(10 * b"\xff")
    res1 = int.from_bytes(ser.read(5), byteorder='little')
    res2 = int.from_bytes(ser.read(5), byteorder='little')
    print(f"Part 1: {res1}")
    print(f"Part 2: {res2}")

def day3(ser, use_sample):
    ser.xonxoff = False # TODO
    input_file = "sample3" if use_sample else "data3"
    with open(os.path.join(DATA_DIR, input_file), "r") as f:
        text = f.read()

    ser.write(len(text.splitlines()[0].strip()).to_bytes(1, byteorder="little"))
    ser.write(text.strip().encode("utf-8") + b"\xff")

    res1 = int.from_bytes(ser.read(4), byteorder='little')
    res2 = int.from_bytes(ser.read(8), byteorder='little')
    print(f"Part 1: {res1}")
    print(f"Part 2: {res2}")


if __name__ == "__main__":
    ser = serial.Serial("/dev/ttyUSB1", 115200, timeout=1, xonxoff=True)

    parser = argparse.ArgumentParser(description='Send input data to fpga and read solution.')
    parser.add_argument("day", type=int, choices=[1,2,3])
    parser.add_argument("--sample", action="store_true", help="Send sample data")
    args = parser.parse_args()

    if args.day == 1:
        day1(ser, args.sample)
    elif args.day == 2:
        day2(ser, args.sample)
    elif args.day == 3:
        day3(ser, args.sample)

    ser.close()
