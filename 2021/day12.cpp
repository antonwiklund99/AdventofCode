#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <unordered_set>
#include <unordered_map>
#include <cctype>
#include "util.h"
using namespace std;

unordered_map<string,vector<string>> edges;

bool isSmall(const string& cave) {
  return islower(cave[0]);
}

int pathsToEnd1(const string& from, unordered_set<string>& smallVisited) {
  if (from == "end") {
    return 1;
  }
  int paths = 0;
  for (auto neighbour : edges[from]) {
    if (smallVisited.find(neighbour) == smallVisited.end()) {
      if (isSmall(neighbour)) smallVisited.insert(neighbour);
      paths += pathsToEnd1(neighbour, smallVisited);
      if (isSmall(neighbour)) smallVisited.erase(neighbour);
    }
  }
  return paths;
}

int pathsToEnd2(const string& from, unordered_set<string>& smallVisited, bool usedTwice) {
  if (from == "end") {
    return 1;
  }
  int paths = 0;
  for (auto neighbour : edges[from]) {
    if (smallVisited.find(neighbour) == smallVisited.end()) {
      if (isSmall(neighbour)) smallVisited.insert(neighbour);
      paths += pathsToEnd2(neighbour, smallVisited, usedTwice);
      if (isSmall(neighbour)) smallVisited.erase(neighbour);
    } else if (!usedTwice && neighbour != "start") {
      paths += pathsToEnd2(neighbour, smallVisited, true);
    }
  }
  return paths;
}

void part1() {
  int res = 0;
  unordered_set<string> smallVisited{"start"};
  cout << pathsToEnd1("start", smallVisited) << endl;
}

void part2() {
  int res = 0;
  unordered_set<string> smallVisited{"start"};
  cout << pathsToEnd2("start", smallVisited, false) << endl;
}

int main() {
  std::fstream data_file ("data/data12.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    auto splitted = split(line,'-');
    edges[splitted[0]].push_back(splitted[1]);
    edges[splitted[1]].push_back(splitted[0]);
  }
  data_file.close();

  part1();
  part2();
}