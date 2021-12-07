#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>
#include "util.h"
using namespace std;

vector<int> nums;
int biggest = -1;

void part1() {
  int res = 1000000000;
  for (int i = 0; i < biggest; i++) {
    int sum = 0;
    for (int j = 0; j < nums.size(); j++) {
      sum += abs(i-nums[j]);
    }
    res = min(sum,res);
  }
  cout << res << endl;
}

void part2() {
  int res = 1000000000;
  for (int i = 0; i < biggest; i++) {
    int sum = 0;
    for (int j = 0; j < nums.size(); j++) {
      int n = abs(i-nums[j]);
      sum += n*(n+1)/2;
    }
    res = min(sum,res);
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data7.txt", std::ios::in);
  string line;
  getline(data_file, line);
  for (auto s : split(line,',')) {
    nums.push_back(stoi(s));
    biggest = max(stoi(s),biggest);
  }
  data_file.close();

  part1();
  part2();
}