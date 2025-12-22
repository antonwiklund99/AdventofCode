import serial
import threading

# Configure the serial port
ser = serial.Serial('/dev/ttyUSB1', 115200, timeout=1, xonxoff=True)

def tx(n):
    for i in range(n):
        ser.write(str(i % 10).encode('utf-8'))

def rx(n):
    prev = -1
    i = 0
    while i < n:
        d = ser.read(1)
        if d == b'\x11' or d == b'\x13':
            print(f"xon/xoff: {d}")
            continue
        i += 1
        if int(d) != (int(prev) + 1) % 10:
            print(f"ERROR NOT SEQUENTIAL {i}: {prev} -> {d}")
        prev = d

t0 = threading.Thread(target=tx, args=(3000,))
t1 = threading.Thread(target=rx, args=(3000,))
t0.start()
t1.start()

t0.join()
t1.join()

# Close the serial port
ser.close()
