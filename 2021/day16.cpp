#include <vector>
#include <string>
#include <map>
#include <set>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <unordered_set>
#include <unordered_map>
#include <regex>
#include "util.h"
using std::vector; using std::cout; using std::endl;
using std::string; using std::stoi; using std::regex;

vector<int> nums;

void part1() {

}

void part2() {

}

int main() {
  std::fstream data_file ("data/data16.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    nums.push_back(stoi(line));
  }
  data_file.close();

  part1();
  part2();
}