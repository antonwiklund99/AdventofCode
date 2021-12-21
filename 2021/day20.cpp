#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <unordered_map>
#include <cmath>
#include "util.h"
using namespace std;

bool algo[512];
unordered_map<Point2D,bool> startPixels;

void printPixels(const unordered_map<Point2D,bool> pixels) {
  int xStart = INT32_MAX;
  int xEnd = INT32_MIN;
  int yStart = INT32_MAX;
  int yEnd = INT32_MIN;
  for (auto p : pixels) {
    xStart = min(xStart, (int) p.first.x);
    xEnd = max(xEnd, (int) p.first.x);
    yStart = min(yStart, (int) p.first.y);
    yEnd = max(yEnd, (int) p.first.y);
  }
  cout << "x = [" << xStart << "," << xEnd << "] y = [" << yStart << "," << yEnd << "]" << endl;
  for (int y = yStart; y <= yEnd; y++) {
    for (int x = xStart; x <= xEnd; x++) {
      Point2D p(x,y);
      if (pixels.find(p) != pixels.end()) {
        if (pixels.at(p)) cout << '#';
        else cout << ".";
      } else {
        cout << '-';
      }
    }
    cout << endl;
  }
}

void part1() {
  unordered_map<Point2D,bool> pixels = startPixels;
  bool infValue = false;
  for (int n = 0; n < 2; n++) {
    unordered_map<Point2D,bool> newPixels;
    int nextInfValue = infValue ? algo[511] : algo[0];
    int xStart = INT32_MAX;
    int xEnd = INT32_MIN;
    int yStart = INT32_MAX;
    int yEnd = INT32_MIN;
    for (auto p : pixels) {
      xStart = min(xStart, (int) p.first.x);
      xEnd = max(xEnd, (int) p.first.x);
      yStart = min(yStart, (int) p.first.y);
      yEnd = max(yEnd, (int) p.first.y);
    }
    for (int x = xStart; x <= xEnd; x++) {
      for (int y = yStart; y <= yEnd; y++) {
        int index = 0;
        Point2D p(x,y);
        for (int i = -1; i <= 1; i++) {
          for (int j = -1; j <= 1; j++) {
            index <<= 1;
            Point2D np = Point2D(p.x + j, p.y + i);
            if ((pixels.find(np) != pixels.end() && pixels[np]) || (infValue && (np.x < xStart || np.x > xEnd || np.y < yStart || np.y > yEnd))) {
              index++;
            }
          }
        }
        if (algo[index]) {
          newPixels[p] = true;
          for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
              Point2D np(p.x+j,p.y+i);
              if (newPixels.find(np) == newPixels.end()) {
                if (np.x < xStart || np.x > xEnd || np.y < yStart || np.y > yEnd) {
                  newPixels[np] = nextInfValue;
                } else {
                  newPixels[np] = false;
                }
              }
            }
          }
        }
      }
    }
    pixels = newPixels;
    infValue = nextInfValue;
  }
  int res = 0;
  for (auto p : pixels) {
    if (p.second) res++;
  }
  cout << res << endl;
}

void part2() {
  unordered_map<Point2D,bool> pixels = startPixels;
  bool infValue = false;
  for (int n = 0; n < 50; n++) {
    unordered_map<Point2D,bool> newPixels;
    int nextInfValue = infValue ? algo[511] : algo[0];
    int xStart = INT32_MAX;
    int xEnd = INT32_MIN;
    int yStart = INT32_MAX;
    int yEnd = INT32_MIN;
    for (auto p : pixels) {
      xStart = min(xStart, (int) p.first.x);
      xEnd = max(xEnd, (int) p.first.x);
      yStart = min(yStart, (int) p.first.y);
      yEnd = max(yEnd, (int) p.first.y);
    }
    for (int x = xStart; x <= xEnd; x++) {
      for (int y = yStart; y <= yEnd; y++) {
        int index = 0;
        Point2D p(x,y);
        for (int i = -1; i <= 1; i++) {
          for (int j = -1; j <= 1; j++) {
            index <<= 1;
            Point2D np = Point2D(p.x + j, p.y + i);
            if ((pixels.find(np) != pixels.end() && pixels[np]) || (infValue && (np.x < xStart || np.x > xEnd || np.y < yStart || np.y > yEnd))) {
              index++;
            }
          }
        }
        if (algo[index]) {
          newPixels[p] = true;
          for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
              Point2D np(p.x+j,p.y+i);
              if (newPixels.find(np) == newPixels.end()) {
                if (np.x < xStart || np.x > xEnd || np.y < yStart || np.y > yEnd) {
                  newPixels[np] = nextInfValue;
                } else {
                  newPixels[np] = false;
                }
              }
            }
          }
        }
      }
    }
    pixels = newPixels;
    infValue = nextInfValue;
  }
  int res = 0;
  for (auto p : pixels) {
    if (p.second) res++;
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data20.txt", std::ios::in);
  string line;
  getline(data_file, line);
  for (int i = 0; i < 512; i++) algo[i] = line[i] == '#';
  getline(data_file, line);
  int y = 0;
  while (getline(data_file, line)) {
    for (int x = 0; x < line.size(); x++) {
      startPixels[Point2D(x,y)] = line[x] == '#';
      for (int i = -2; i <= 2; i++) {
        for (int j = -2; j <= 2; j++) {
          if (startPixels.find(Point2D(x+i,y+j)) == startPixels.end()) {
            startPixels[Point2D(x+i,y+j)] = false;
          }
        }
      }
    }
    y++;
  }
  data_file.close();

  part1();
  part2();
}