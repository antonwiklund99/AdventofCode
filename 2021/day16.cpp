#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>
#include <climits>
#include "util.h"
using namespace std;

string line;

class BitGenerator {
  public:
    int usedBits = 0;

    BitGenerator(string hex) {
      this->hex = hex;
    }

    int getBits(int bits) {
      int n = 0;
      while (bits > 0) {
        if (mask == 0) {
          // Read a byte at a time
          currentByte = stoi(hex.substr(0,2),0,16);
          hex = hex.substr(2);
          mask = 1 << 7;
        }
        n = (n << 1) + ((bool) (currentByte & mask));
        mask >>= 1;
        bits--;
        usedBits++;
      }
      return n;
    }

  private:
    string hex;
    uint8_t currentByte = 0;
    uint8_t mask = 0;
};

struct Packet {
  int ver;
  int type;
  long value;
  vector<Packet*> subpackets;
};

Packet* getPacket(BitGenerator& generator) {
  Packet* packet = new Packet();
  packet->ver = generator.getBits(3);
  packet->type = generator.getBits(3);
  packet->subpackets = vector<Packet*>();
  if (packet->type == 4) {
    long n = 0;
    bool last = false;
    while (!last) {
      last = !generator.getBits(1);
      n = (n << 4) + generator.getBits(4);
    }
    packet->value = n;
  } else {
    int lenType = generator.getBits(1);
    if (lenType == 0) {
      int bits = generator.getBits(15);
      int originalBits = generator.usedBits;
      while (generator.usedBits < originalBits + bits) {
        packet->subpackets.push_back(getPacket(generator));
      }
    } else {
      int subs = generator.getBits(11);
      for (int i = 0; i < subs; i++) {
        packet->subpackets.push_back(getPacket(generator));
      }
    }
  }
  return packet;
}

int sumVersions(Packet* packet) {
  int sum = packet->ver;
  for (auto p : packet->subpackets) {
    sum += sumVersions(p);
  }
  return sum;
}

long valueOf(Packet* packet) {
  long res;
  switch (packet->type) {
  case 0:
    res = 0;
    for (auto p : packet->subpackets) {
      res += valueOf(p);
    }
    break;
  case 1:
    res = 1;
    for (auto p : packet->subpackets) {
      res *= valueOf(p);
    }
    break;
  case 2:
    res = __LONG_MAX__;
    for (auto p : packet->subpackets) {
      res = min(res,valueOf(p));
    }
    break;
  case 3:
    res = -1;
    for (auto p : packet->subpackets) {
      res = max(res,valueOf(p));
    }
    break;
  case 4:
    res = packet->value;
    break;
  case 5:
    res = valueOf(packet->subpackets[0]) > valueOf(packet->subpackets[1]);
    break;
  case 6:
    res = valueOf(packet->subpackets[0]) < valueOf(packet->subpackets[1]);
    break;
  case 7:
    res = valueOf(packet->subpackets[0]) == valueOf(packet->subpackets[1]);
    break;
  default:
    break;
  }
  return res;
}

void part1() {
  BitGenerator generator(line);
  Packet* topPacket = getPacket(generator);
  cout << sumVersions(topPacket) << endl;
}

void part2() {
  BitGenerator generator(line);
  Packet* topPacket = getPacket(generator);
  cout << valueOf(topPacket) << endl;
}

int main() {
  std::fstream data_file ("data/data16.txt", std::ios::in);
  getline(data_file, line);
  data_file.close();

  part1();
  part2();
}