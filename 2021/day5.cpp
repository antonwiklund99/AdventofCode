#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <unordered_map>
#include <regex>
#include <cmath>
#include "util.h"
using namespace std;

vector<pair<Point2D,Point2D>> edges;

void part1() {
  unordered_map<Point2D,int> overlaps;
  for (int i = 0; i < edges.size(); i++) {
    pair<Point2D,Point2D> edge = edges[i];
    if (edge.first.x == edge.second.x) {
      for (int y = min(edge.first.y,edge.second.y); y <= max(edge.first.y,edge.second.y); y++) {
        Point2D p = Point2D((int) edge.first.x, y);
        overlaps[p]++;
      }
    } else if (edge.first.y == edge.second.y) {
      for (int x = min(edge.first.x,edge.second.x); x <= max(edge.first.x,edge.second.x); x++) {
        Point2D p = Point2D(x, (int) edge.first.y);
        overlaps[p]++;
      }
    }
  }
  cout << count_if(overlaps.begin(), overlaps.end(), [](auto p) { return p.second > 1; }) << endl;
}

void part2() {
  unordered_map<Point2D,int> overlaps;
  for (int i = 0; i < edges.size(); i++) {
    pair<Point2D,Point2D> edge = edges[i];
    if (edge.first.x == edge.second.x) {
      for (int y = min(edge.first.y,edge.second.y); y <= max(edge.first.y,edge.second.y); y++) {
        Point2D p = Point2D((int) edge.first.x, y);
        overlaps[p]++;
      }
    } else if (edge.first.y == edge.second.y) {
      for (int x = min(edge.first.x,edge.second.x); x <= max(edge.first.x,edge.second.x); x++) {
        Point2D p = Point2D(x, (int) edge.first.y);
        overlaps[p]++;
      }
    } else {
      Point2D start = edge.first.x < edge.second.x ? edge.first : edge.second;
      Point2D end = edge.first.x < edge.second.x ? edge.second : edge.first;
      int dy = start.y < end.y ? 1 : -1;
      int x = start.x, y = start.y;
      while (x <= end.x) {
        Point2D p = Point2D(x,y);
        overlaps[p]++;
        x++;
        y += dy;
      }
    }
  }
  cout << count_if(overlaps.begin(), overlaps.end(), [](auto p) { return p.second > 1; }) << endl;
}

int main() {
  std::fstream data_file ("data/data5.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    regex pattern = regex ("(\\d+),(\\d+) -> (\\d+),(\\d+)");
    std::smatch sm = match(pattern, line);
    Point2D start = Point2D(stoi(sm[1]),stoi(sm[2]));
    Point2D end = Point2D(stoi(sm[3]),stoi(sm[4]));
    edges.push_back(pair(start,end));
  }
  data_file.close();

  part1();
  part2();
}