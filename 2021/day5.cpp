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
using namespace std;

/*
string line = "1-3 a: abcde";
regex pattern = regex ("(\\d+)-(\\d+) ([a-z]): ([a-z]+)");
std::smatch sm = match(pattern, line);
cout << sm[1] << " " << sm[2] << " " << sm[3] << " " << sm[4] << endl;
*/

vector<int> nums;
vector<string> lines;

void part1() {
  int res = 0;
  for (int i = 0; i < nums.size(); i++) {

  }
  cout << res << endl;
}

void part2() {

}

int main() {
  std::fstream data_file ("data/data5.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    nums.push_back(stoi(line));
    lines.push_back(line);
  }
  data_file.close();

  part1();
  part2();
}