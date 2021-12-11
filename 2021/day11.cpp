#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include "util.h"
using namespace std;

vector<vector<int>> grid;

int flash(int r, int c) {
  grid[r][c] = 0;
  int flashCount = 1;
  for (int dr = -1; dr <= 1; dr++) {
    for (int dc = -1; dc <= 1; dc++) {
      int i = r + dr, j = c + dc;
      if ((dr != 0 || dc != 0) && i >= 0 && i < grid.size() && j >= 0 && j < grid[0].size() && grid[i][j] != 0) {
        grid[i][j]++;
        if (grid[i][j] > 9) {
          flashCount += flash(i,j);
        }
      }
    }
  }
  return flashCount;
}

void part1() {
  int res = 0;
  for (int step = 1; step <= 100; step++) {
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid[i].size(); j++) {
        grid[i][j]++;
      }
    }
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid[i].size(); j++) {
        if (grid[i][j] > 9) {
          res += flash(i,j);
        }
      }
    }
  }
  cout << res << endl;
}

void part2() {
  for (int step = 1;; step++) {
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid[i].size(); j++) {
        grid[i][j]++;
      }
    }
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid[i].size(); j++) {
        if (grid[i][j] > 9) {
          flash(i,j);
        }
      }
    }
    bool allFlash = true;
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid[i].size(); j++) {
        if (grid[i][j] != 0) {
          allFlash = false;
          break;
        }
      }
      if (!allFlash) break;
    }
    if (allFlash) {
      cout << 100+step << endl;
      return;
    }
  }
}

int main() {
  std::fstream data_file ("data/data11.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    vector<int> row;
    for (auto c : line) {
      row.push_back(c - '0');
    }
    grid.push_back(row);
  }
  data_file.close();

  part1();
  part2();
}