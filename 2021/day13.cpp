#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>
#include "util.h"
using namespace std;

vector<pair<int,int>> points;
vector<pair<char,int>> folds;
int maxX = 0, maxY = 0;

void part1() {
  bool grid[maxY+1][maxX+1];
  int x = maxX, y = maxY;
  for (int i = 0; i <= y; i++) {
    for (int j = 0; j <= x; j++) {
      grid[i][j] = false;
    }
  }
  for (auto p : points) {
    grid[p.second][p.first] = true;
  }
  pair<char,int> fold = folds[0];
  if (fold.first == 'x') {
    for (int r = 0; r <= y; r++) {
      for (int i = 1; i <= x - fold.second; i++) {
        int rx = fold.second + i;
        int lx = fold.second - i;
        grid[r][lx] = grid[r][lx] || grid[r][rx];
      }
    }
    x = fold.second - 1;
  } else {
    for (int c = 0; c <= x; c++) {
      for (int i = 1; i <= y - fold.second; i++) {
        int dy = fold.second + i;
        int uy = fold.second - i;
        grid[uy][c] = grid[uy][c] || grid[dy][c];
      }
    }
    y = fold.second - 1;
  }
  int res = 0;
  for (int i = 0; i <= y; i++) {
    for (int j = 0; j <= x; j++) {
      if (grid[i][j]) res++; 
    }
  }
  cout << res << endl;
}

void part2() {
  bool grid[maxY+1][maxX+1];
  int x = maxX, y = maxY;
  for (int i = 0; i <= y; i++) {
    for (int j = 0; j <= x; j++) {
      grid[i][j] = false;
    }
  }
  for (auto p : points) {
    grid[p.second][p.first] = true;
  }
  for (auto fold : folds) {
    if (fold.first == 'x') {
      for (int r = 0; r <= y; r++) {
        for (int i = 1; i <= x - fold.second; i++) {
          int rx = fold.second + i;
          int lx = fold.second - i;
          grid[r][lx] = grid[r][lx] || grid[r][rx];
        }
      }
      x = fold.second - 1;
    } else {
      for (int c = 0; c <= x; c++) {
        for (int i = 1; i <= y - fold.second; i++) {
          int dy = fold.second + i;
          int uy = fold.second - i;
          grid[uy][c] = grid[uy][c] || grid[dy][c];
        }
      }
      y = fold.second - 1;
    }
  }
  for (int i = 0; i <= y; i++) {
    for (int j = 0; j <= x; j++) {
      cout << (grid[i][j] ? "#" : " ");
    }
    cout << endl;
  }
}

int main() {
  std::fstream data_file ("data/data13.txt", std::ios::in);
  string line;
  bool parsingCords = true;
  while (getline(data_file, line)) {
    if (line.size() == 0) {
      parsingCords = false;
    } else if (parsingCords) {
      vector<string> splitted = split(line,',');
      int x = stoi(splitted[0]);
      int y = stoi(splitted[1]);
      maxX = max(maxX, x);
      maxY = max(maxY, y);
      points.push_back(pair<int,int>(x,y));
    } else {
      regex pattern = regex ("fold along (x|y)=(\\d+)");
      std::smatch sm = match(pattern, line);
      folds.push_back(pair<char,int>(sm[1] == "x" ? 'x' : 'y', stoi(sm[2])));
    }
  }
  data_file.close();

  part1();
  part2();
}