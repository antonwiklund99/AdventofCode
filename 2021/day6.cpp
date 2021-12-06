#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include "util.h"
using namespace std;

long occ1[9]{0};
long occ2[9]{0};

void part1() {
  for (int n = 0; n < 80; n++) {
    long zeroes = occ1[0];
    for (int i = 0; i < 8; i++) {
      occ1[i] = occ1[i+1];
    }
    occ1[6] += zeroes;
    occ1[8] = zeroes;
  }
  long res = 0;
  for (int i = 0; i < 9; i++) {
    res += occ1[i];
  }
  cout << res << endl;
}

void part2() {
  for (int n = 0; n < 256; n++) {
    long zeroes = occ2[0];
    for (int i = 0; i < 8; i++) {
      occ2[i] = occ2[i+1];
    }
    occ2[6] += zeroes;
    occ2[8] = zeroes;
  }
  long res = 0;
  for (int i = 0; i < 9; i++) {
    res += occ2[i];
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data6.txt", std::ios::in);
  string line;
  getline(data_file, line);
  for (auto s : split(line, ',')) {
    occ1[stoi(s)]++;
    occ2[stoi(s)]++;
  }
  data_file.close();

  part1();
  part2();
}