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
vector<string> lines;

void part1() {
  int res = 0;
  for (int i = 0; i < lines.size(); i++) {

  }
  cout << res << endl;
}

void part2() {

}

/*
regex pattern = regex ("(\\d+)-(\\d+) ([a-z]): ([a-z]+)");
std::smatch sm = match(pattern, line);
*/

int main() {
  std::fstream data_file ("data/data17.txt", std::ios::in);
  string line;
  /*
  getline(data_file, line);
  for (auto s : split(line,',')) {
    nums.push_back(stoi(s));
  }
  */
  while (getline(data_file, line)) {
    nums.push_back(stoi(line));
    lines.push_back(line);
  }
  data_file.close();

  part1();
  part2();
}