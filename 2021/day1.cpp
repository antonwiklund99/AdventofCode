#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include "util.h"
using std::vector; using std::cout; using std::endl;
using std::string; using std::stoi;

vector<int> nums;

void part1() {
  int res = 0;
  for (int i = 1; i < nums.size(); i++) {
    if (nums[i] > nums[i-1]) res++;
  }
  cout << res << endl;
}

void part2() {
  int res = 0;
  int last = nums[0];
  for (int i = 3; i < nums.size(); i++) {
    if (nums[i] > last) res++;
    last = nums[i-2];
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data1.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    nums.push_back(stoi(line));
  }
  data_file.close();

  part1();
  part2();
}