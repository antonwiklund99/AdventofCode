#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include "util.h"
using std::vector; using std::cout; using std::endl;
using std::string; using std::stoi;

vector<string> lines;

void part1() {
  int x = 0;
  int y = 0;
  for (int i = 0; i < lines.size(); i++) {
    vector<string> splitted = split(lines[i], ' ');
    string dir = splitted[0];
    int n = stoi(splitted[1]);
    if (dir == "forward") x += n;
    else if (dir == "up") y -= n;
    else if (dir == "down") y += n;
  }
  cout << x*y << endl;
}

void part2() {
  int x = 0;
  int y = 0;
  int a = 0;
  for (int i = 0; i < lines.size(); i++) {
    vector<string> splitted = split(lines[i], ' ');
    string dir = splitted[0];
    int n = stoi(splitted[1]);
    if (dir == "forward") {
      x += n;
      y += a*n;
    }
    else if (dir == "up") a -= n;
    else if (dir == "down") a += n;
  }
  cout << x*y << endl;
}

int main() {
  std::fstream data_file ("data/data2.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    lines.push_back(line);
  }
  data_file.close();

  part1();
  part2();
}