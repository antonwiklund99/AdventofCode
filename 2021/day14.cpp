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

string start;
unordered_map<string,string> rules;

void part1() {
  int res = 0;
  string pol = start;
  for (int n = 0; n < 10; n++) {
    string newPol = "";
    for (int i = 0; i < pol.size()-1; i++) {
      string k = {pol[i],pol[i+1]};
      if (rules.find(k) != rules.end()) {
        newPol += pol[i] + rules[k];
      } else {
        newPol += pol[i];
      }
    }
    newPol += pol[pol.size()-1];
    pol = newPol;
  }
  unordered_map<char,int> hist;
  for (char c : pol) hist[c]++;
  int biggest = -1, smallest = -1; 
  for (auto p : hist) {
    if (biggest == -1 || smallest == -1) {
      biggest = p.second;
      smallest = p.second;
    } else {
      biggest = max(biggest, p.second);
      smallest = min(smallest, p.second);
    }
  }
  cout << biggest - smallest << endl;
}

void part2() {
  unordered_map<char,long> hist;
  unordered_map<string,long> pairs;
  for (int i = 0; i < start.size(); i++) {
    hist[start[i]]++;
    if (i < start.size() - 1) pairs[{start[i],start[i+1]}]++;
  }
  for (int n = 0; n < 40; n++) {
    unordered_map<string,long> newPairs;
    for (auto p : pairs) {
      char added = rules[p.first][0];
      hist[added] += p.second;
      newPairs[{p.first[0],added}] += p.second;
      newPairs[{added,p.first[1]}] += p.second;
    }
    pairs = newPairs;
  }
  long biggest = -1, smallest = -1; 
  for (auto p : hist) {
    if (biggest == -1 || smallest == -1) {
      biggest = p.second;
      smallest = p.second;
    } else {
      biggest = max(biggest, p.second);
      smallest = min(smallest, p.second);
    }
  }
  cout << biggest - smallest << endl;
}

int main() {
  std::fstream data_file ("data/data14.txt", std::ios::in);
  string line;
  getline(data_file, line);
  start = line;
  getline(data_file, line);
  while (getline(data_file, line)) {
    regex pattern = regex ("([A-Z]+) -> ([A-Z])");
    std::smatch sm = match(pattern, line);
    rules[sm[1]] = sm[2];
  }
  data_file.close();

  part1();
  part2();
}