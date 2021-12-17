#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <regex>
#include <cmath>
#include "util.h"
using namespace std;

// Always to the right (positive)
int minX, maxX;
// Always down (negative)
int minY, maxY;

void part1() {
  int res = -1;
  int xLowest = 1;
  int n = 1;
  while (n <= minX) {
    xLowest++;
    n += xLowest;
  }
  int stepsDx0 = INT32_MAX;
  for (int initX = xLowest; initX <= maxX; initX++) {
    int x = 0;
    int dx = initX;
    int steps = 0;
    while (x <= maxX) {
      if (dx == 0) {
        if (minX <= x && x <= maxX) {
          stepsDx0 = min(stepsDx0, steps);
        }
        break;
      }
      x += dx;
      steps++;
      if (dx > 0) dx--;
      else if (dx < 0) dx++;
    }
  }
  for (int initY = 0; initY < 1000; initY++) {
    int y = 0;
    int dy = initY;
    int peak = 0;
    int steps = 0;
    while (y >= minY) {
      peak = max(peak, y);
      if (minY <= y && y <= maxY && steps >= stepsDx0) {
        res = max(res,peak);
        break;
      }
      steps++;
      y += dy;
      dy--;
    }
  }
  cout << res << endl;
}

void part2() {
  int res = 0;
  int xLowest = 1;
  int n = 1;
  while (n <= minX) {
    xLowest++;
    n += xLowest;
  }
  for (int initX = xLowest; initX <= maxX; initX++) {
    for (int initY = minY; initY < 200; initY++) {
      int x = 0, y = 0;
      int dx = initX, dy = initY;
      while (x <= maxX && y >= minY) {
        if (minX <= x && x <= maxX && minY <= y && y <= maxY) {
          res++;
          break;
        }
        x += dx;
        y += dy;
        dy--;
        if (dx > 0) dx--;
        else if (dx < 0) dx++;
      }
    }
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data17.txt", std::ios::in);
  string line;
  getline(data_file, line);
  regex pattern = regex ("target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)");
  std::smatch sm = match(pattern, line);
  minX = stoi(sm[1]);
  maxX = stoi(sm[2]);
  minY = stoi(sm[3]);
  maxY = stoi(sm[4]);
  data_file.close();

  part1();
  part2();
}