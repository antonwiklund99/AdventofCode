import os
import serial
import argparse
import time

DATA_DIR =  os.path.join(os.path.dirname(__file__), "../data/")

def day1(ser, use_sample):
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

def reset_usb_device(device_path):
    """Reset USB device without unplugging"""
    try:
        # Find the USB device
        import subprocess
        # Get bus and device number
        result = subprocess.run(
            ['udevadm', 'info', '--query=property', device_path],
            capture_output=True, text=True
        )
        
        bus = None
        dev = None
        for line in result.stdout.split('\n'):
            if 'BUSNUM=' in line:
                bus = line.split('=')[1]
            if 'DEVNUM=' in line:
                dev = line.split('=')[1]
        
        if bus and dev:
            # Use usbreset tool or write to authorized file
            usb_path = f"/dev/bus/usb/{bus.zfill(3)}/{dev.zfill(3)}"
            # This requires root or udev rules
            import fcntl
            USBDEVFS_RESET = 21780
            with open(usb_path, 'w') as f:
                fcntl.ioctl(f, USBDEVFS_RESET, 0)
            time.sleep(2)
            return True
    except Exception as e:
        print(f"USB reset failed: {e}")
        return False


if __name__ == "__main__":
    try:
        ser = serial.Serial("/dev/ttyUSB1", 115200, timeout=1, xonxoff=True)
    except:
        print("try reset")
        reset_usb_device("/dev/ttyUSB1")
        ser = serial.Serial("/dev/ttyUSB1", 115200, timeout=1, xonxoff=True)
    
    parser = argparse.ArgumentParser(description='Send input data to fpga and read solution.')
    parser.add_argument("day", type=int, choices=[1])
    parser.add_argument("--sample", action="store_true", help="Send sample data")
    args = parser.parse_args()
    
    if args.day == 1:
        day1(ser, args.sample)

    ser.close()
