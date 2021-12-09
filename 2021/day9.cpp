#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <queue>
#include "util.h"
using namespace std;

vector<string> lines;

void part1() {
  int res = 0;
  for (int i = 0; i < lines.size(); i++) {
    for (int j = 0; j < lines[i].size(); j++) {
      if ((i == 0 || lines[i-1][j] > lines[i][j]) && (i+1 == lines.size() || lines[i+1][j] > lines[i][j]) &&
          (j == 0 || lines[i][j-1] > lines[i][j]) && (j+1 == lines[i].size() || lines[i][j+1] > lines[i][j])) {
        res += lines[i][j] - '0' + 1;
      }
    }
  }
  cout << res << endl;
}

void part2() {
  bool seen[lines.size()][lines[0].size()];
  vector<int> basins{-1,-1,-1};
  for (int i = 0; i < lines.size(); i++) {
    for (int j = 0; j < lines[i].size(); j++) {
      if (!seen[i][j] && lines[i][j] != '9') {
        int size = 1;
        queue<pair<int,int>> q;
        q.push(pair<int,int>(i,j));
        seen[i][j] = true;
        while (q.size() != 0) {
          pair<int,int> p = q.front();
          int pi = p.first, pj = p.second;
          q.pop();
          if (pi != 0 && lines[pi-1][pj] != '9' && !seen[pi-1][pj]) {
            size++;
            seen[pi-1][pj] = true;
            q.push(pair<int,int>(pi-1,pj));
          }
          if (pi+1 != lines.size() && lines[pi+1][pj] != '9' && !seen[pi+1][pj]) {
            size++;
            seen[pi+1][pj] = true;
            q.push(pair<int,int>(pi+1,pj));
          }
          if (pj != 0 && lines[pi][pj-1] != '9' && !seen[pi][pj-1]) {
            size++;
            seen[pi][pj-1] = true;
            q.push(pair<int,int>(pi,pj-1));
          }
          if (pj+1 != lines[0].size() && lines[pi][pj+1] != '9' && !seen[pi][pj+1]) {
            size++;
            seen[pi][pj+1] = true;
            q.push(pair<int,int>(pi,pj+1));
          }
        }
        if (size > basins[2]) {
          basins[0] = basins[1];
          basins[1] = basins[2];
          basins[2] = size;
        } else if (size > basins[1]) {
          basins[0] = basins[1];
          basins[1] = size;
        } else if (size > basins[0]) {
          basins[0] = size;
        }
      }
    }
  }
  cout << basins[0]*basins[1]*basins[2] << endl;
}

int main() {
  std::fstream data_file ("data/data9.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    lines.push_back(line);
  }
  data_file.close();

  part1();
  part2();
}