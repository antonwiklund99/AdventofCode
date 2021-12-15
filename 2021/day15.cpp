#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <queue>
#include "util.h"
using namespace std;

vector<string> lines;

struct Node {
  int risk;
  pair<int,int> cords;
};

class NodeCompare {
  public:
    bool operator() (Node a, Node b) {
      return a.risk > b.risk;
    }
};

void part1() {
  bool visited[lines.size()][lines[0].size()];
  for (int i = 0; i < lines.size();i++) {
    for (int j = 0; j < lines[0].size();j++) {
      visited[i][j] = false;
    }
  }
  priority_queue<Node, vector<Node>, NodeCompare> q;
  q.push(Node{0,pair<int,int>(0,0)});
  while (true) {
    Node p = q.top();
    pair<int,int> cords = p.cords;
    q.pop();
    if (cords.first == lines[0].size()-1 && cords.second == lines.size()-1) {
      cout << p.risk << endl;
      return;
    }
    if (visited[cords.second][cords.first]) continue;
    visited[cords.second][cords.first] = true;
    if (cords.second - 1 > 0 && !visited[cords.second-1][cords.first]) {
      Node n;
      n.cords = pair<int,int>(cords.first,cords.second-1);
      n.risk = p.risk + lines[cords.second-1][cords.first] - '0';
      q.push(n);
    }
    if (cords.second + 1 < lines.size() && !visited[cords.second+1][cords.first]) {
      Node n;
      n.cords = pair<int,int>(cords.first,cords.second+1);
      n.risk = p.risk + lines[cords.second+1][cords.first] - '0';
      q.push(n);
    }
    if (cords.first - 1 > 0 && !visited[cords.second][cords.first-1]) {
      Node n;
      n.cords = pair<int,int>(cords.first-1,cords.second);
      n.risk = p.risk + lines[cords.second][cords.first-1] - '0';
      q.push(n);
    }
    if (cords.first + 1 < lines[0].size() && !visited[cords.second][cords.first+1]) {
      Node n;
      n.cords = pair<int,int>(cords.first+1,cords.second);
      n.risk = p.risk + lines[cords.second][cords.first+1] - '0';
      q.push(n);
    }
  }
}

void part2() {
  int R = lines.size()*5;
  int C = lines[0].size()*5;
  int OR = lines.size();
  int OC = lines[0].size();
  int grid[R][C];
  bool visited[R][C];
  for (int i = 0; i < R;i++) {
    for (int j = 0; j < C;j++) {
      visited[i][j] = false;
    }
  }
  for (int r = 0; r < 5; r++) {
    for (int c = 0; c < 5; c++) {
      for (int i = 0; i < OR; i++) {
        for (int j = 0; j < OC; j++) {
          if (r == 0 && c == 0) {
            grid[i][j] = lines[i][j]-'0';
          } else if (r == 0) {
            grid[i][j+c*OC] = grid[i][j+(c-1)*OC]+1;
            if (grid[i][j+c*OC] > 9) grid[i][j+c*OC] = 1;
          } else {
            grid[i+r*OR][j+c*OC] = grid[i+(r-1)*OR][j+c*OC]+1;
            if (grid[i+r*OR][j+c*OC] > 9) grid[i+r*OR][j+c*OC] = 1;
          }
        }
      }
    }
  }
  priority_queue<Node, vector<Node>, NodeCompare> q;
  q.push(Node{0,pair<int,int>(0,0)});
  while (true) {
    Node p = q.top();
    pair<int,int> cords = p.cords;
    q.pop();
    if (cords.first == C-1 && cords.second == R-1) {
      cout << p.risk << endl;
      return;
    }
    if (visited[cords.second][cords.first]) continue;
    visited[cords.second][cords.first] = true;
    if (cords.second - 1 > 0 && !visited[cords.second-1][cords.first]) {
      Node n;
      n.cords = pair<int,int>(cords.first,cords.second-1);
      n.risk = p.risk + grid[cords.second-1][cords.first];
      q.push(n);
    }
    if (cords.second + 1 < R && !visited[cords.second+1][cords.first]) {
      Node n;
      n.cords = pair<int,int>(cords.first,cords.second+1);
      n.risk = p.risk + grid[cords.second+1][cords.first];
      q.push(n);
    }
    if (cords.first - 1 > 0 && !visited[cords.second][cords.first-1]) {
      Node n;
      n.cords = pair<int,int>(cords.first-1,cords.second);
      n.risk = p.risk + grid[cords.second][cords.first-1];
      q.push(n);
    }
    if (cords.first + 1 < C && !visited[cords.second][cords.first+1]) {
      Node n;
      n.cords = pair<int,int>(cords.first+1,cords.second);
      n.risk = p.risk + grid[cords.second][cords.first+1];
      q.push(n);
    }
  }
}

int main() {
  std::fstream data_file ("data/data15.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    lines.push_back(line);
  }
  data_file.close();

  part1();
  part2();
}