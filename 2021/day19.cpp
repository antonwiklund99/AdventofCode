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
#include <cmath>
#include <cctype>
#include <queue>
#include <stack>
#include "util.h"
using namespace std;

vector<int> nums;

void part1() {

}

void part2() {

}

/*
regex pattern = regex ("(\\d+)-(\\d+) ([a-z]): ([a-z]+)");
std::smatch sm = match(pattern, line);
*/

int main() {
  std::fstream data_file ("data/data19.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    nums.push_back(stoi(line));
  }
  data_file.close();

  part1();
  part2();
}