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
#include "util.h"
using namespace std;

typedef pair<Point3D,Point3D> CUBE;

vector<CUBE> rects;
vector<bool> isOn;

CUBE overlap(CUBE r1, CUBE r2) {
  int x1 = max(r1.first.x, r2.first.x);
  int x2 = min(r1.second.x, r2.second.x);
  int y1 = max(r1.first.y, r2.first.y);
  int y2 = min(r1.second.y, r2.second.y);
  int z1 = max(r1.first.z, r2.first.z);
  int z2 = min(r1.second.z, r2.second.z);
  return CUBE(Point3D(x1,y1,z1), Point3D(x2,y2,z2));
};

int rectSize(CUBE rect) {
  return (rect.second.x - rect.first.x + 1)*(rect.second.y - rect.first.y + 1)*(rect.second.z - rect.first.z + 1);
}

void removeCube(CUBE cube, CUBE cubeToRemove, vector<CUBE>& cubesVector) {
  bool onLeftX = cube.first.x == cubeToRemove.first.x;
  bool onRightX = cube.second.x == cubeToRemove.second.x;
  bool onLeftY = cube.first.x == cubeToRemove.first.x;
  bool onRightY = cube.second.x == cubeToRemove.second.x;
  bool onLeftZ = cube.first.x == cubeToRemove.first.x;
  bool onRightZ = cube.second.x == cubeToRemove.second.x;

  if (!onLeftX) {
    auto start = Point3D(cube.first.x, cubeToRemove.first.y, cubeToRemove.first.z);
    auto end = Point3D(cubeToRemove.first.x, cubeToRemove.second.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX) {
    auto start = Point3D(cubeToRemove.second.x, cubeToRemove.first.y, cubeToRemove.first.z);
    auto end = Point3D(cube.second.x, cubeToRemove.second.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftY) {
    auto start = Point3D(cubeToRemove.first.x, cube.first.y, cubeToRemove.first.z);
    auto end = Point3D(cubeToRemove.second.x, cubeToRemove.first.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightY) {
    auto start = Point3D(cubeToRemove.first.x, cubeToRemove.second.y, cubeToRemove.first.z);
    auto end = Point3D(cubeToRemove.second.x, cube.second.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftZ) {
    auto start = Point3D(cubeToRemove.first.x, cubeToRemove.first.y, cube.first.z);
    auto end = Point3D(cubeToRemove.second.x, cubeToRemove.second.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightZ) {
    auto start = Point3D(cubeToRemove.first.x, cubeToRemove.first.y, cubeToRemove.second.z);
    auto end = Point3D(cubeToRemove.second.x, cubeToRemove.second.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }

  if (!onLeftX && !onLeftY) {
    auto start = Point3D(cube.first.x, cube.first.y, cubeToRemove.first.z);
    auto end = Point3D(cubeToRemove.first.x, cubeToRemove.first.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftX && !onRightY) {
    auto start = Point3D(cube.first.x, cubeToRemove.second.y, cubeToRemove.first.z);
    auto end = Point3D(cubeToRemove.first.x, cube.second.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftX && !onLeftZ) {
    auto start = Point3D(cube.first.x, cubeToRemove.first.y, cube.first.z);
    auto end = Point3D(cubeToRemove.first.x, cubeToRemove.second.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftX && !onRightZ) {
    auto start = Point3D(cube.first.x, cubeToRemove.first.y, cubeToRemove.second.z);
    auto end = Point3D(cubeToRemove.first.x, cubeToRemove.second.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }

  if (!onRightX && !onLeftY) {
    auto start = Point3D(cubeToRemove.second.x, cube.first.y, cubeToRemove.first.z);
    auto end = Point3D(cube.second.x, cubeToRemove.first.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX && !onRightY) {
    auto start = Point3D(cubeToRemove.second.x, cubeToRemove.second.y, cubeToRemove.first.z);
    auto end = Point3D(cube.second.x, cube.second.y, cubeToRemove.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX && !onLeftZ) {
    auto start = Point3D(cubeToRemove.second.x, cubeToRemove.first.y, cube.first.z);
    auto end = Point3D(cube.second.x, cubeToRemove.second.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX && !onRightZ) {
    auto start = Point3D(cubeToRemove.second.x, cubeToRemove.first.y, cubeToRemove.second.z);
    auto end = Point3D(cube.second.x, cubeToRemove.second.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }

  if (!onLeftY && !onLeftZ) {
    auto start = Point3D(cubeToRemove.first.x, cube.first.y, cube.first.z);
    auto end = Point3D(cubeToRemove.second.x, cubeToRemove.first.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftY && !onRightZ) {
    auto start = Point3D(cubeToRemove.first.x, cube.first.y, cubeToRemove.second.z);
    auto end = Point3D(cubeToRemove.second.x, cubeToRemove.first.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightY && !onLeftZ) {
    auto start = Point3D(cubeToRemove.first.x, cubeToRemove.second.y, cube.first.z);
    auto end = Point3D(cubeToRemove.second.x, cube.second.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightY && !onRightZ) {
    auto start = Point3D(cubeToRemove.first.x, cubeToRemove.second.y, cubeToRemove.second.z);
    auto end = Point3D(cubeToRemove.second.x, cube.second.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }

  if (!onLeftX && !onLeftY && !onLeftZ) {
    auto start = Point3D(cube.first.x, cube.first.y, cube.first.z);
    auto end = Point3D(cubeToRemove.first.x, cubeToRemove.first.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX && !onLeftY && !onLeftZ) {
    auto start = Point3D(cubeToRemove.second.x, cube.first.y, cube.first.z);
    auto end = Point3D(cube.second.x, cubeToRemove.first.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftX && !onRightY && !onLeftZ) {
    auto start = Point3D(cube.first.x, cubeToRemove.second.y, cube.first.z);
    auto end = Point3D(cubeToRemove.first.x, cube.second.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftX && !onLeftY && !onRightZ) {
    auto start = Point3D(cube.first.x, cube.first.y, cubeToRemove.second.z);
    auto end = Point3D(cubeToRemove.first.x, cubeToRemove.first.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX && !onRightY && !onLeftZ) {
    auto start = Point3D(cubeToRemove.second.x, cubeToRemove.second.y, cube.first.z);
    auto end = Point3D(cube.second.x, cube.second.y, cubeToRemove.first.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX && !onLeftY && !onRightZ) {
    auto start = Point3D(cubeToRemove.second.x, cube.first.y, cubeToRemove.second.z);
    auto end = Point3D(cube.second.x, cubeToRemove.first.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onLeftX && !onRightY && !onRightZ) {
    auto start = Point3D(cube.first.x, cubeToRemove.second.y, cubeToRemove.second.z);
    auto end = Point3D(cubeToRemove.first.x, cube.second.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
  if (!onRightX && !onRightY && !onRightZ) {
    auto start = Point3D(cubeToRemove.second.x, cubeToRemove.second.y, cubeToRemove.second.z);
    auto end = Point3D(cube.second.x, cube.second.y, cube.second.z);
    cubesVector.push_back(CUBE(start, end));
  }
}

void part1() {
  vector<CUBE> cubes;
  for (int i = 0; i < rects.size(); i++) {
    if (rects[i].first.x > 50 || rects[i].first.x < -50 || rects[i].first.y > 50 || rects[i].first.y < -50 || rects[i].first.z > 50 || rects[i].first.z < -50) {
      break;
    }

    if (isOn[i]) {
      vector<CUBE> nonOverlapingCubes = {rects[i]};
      for (auto cube : cubes) {
        vector<CUBE> newCubes;
        for (auto newCube : nonOverlapingCubes) {
          auto ovr = overlap(cube, newCube);
          if (ovr.first.x <= ovr.second.x && ovr.first.y <= ovr.second.y && ovr.first.z <= ovr.second.z) {
            removeCube(newCube, ovr, newCubes);
          } else {
            newCubes.push_back(newCube);
          }
        }
        nonOverlapingCubes = newCubes;
      }
      for (auto cube : nonOverlapingCubes) cubes.push_back(cube);
    } else {
      vector<CUBE> newCubes;
      for (auto cube : cubes) {
        auto ovr = overlap(cube, rects[i]);
        if (ovr.first.x <= ovr.second.x && ovr.first.y <= ovr.second.y && ovr.first.z <= ovr.second.z) {
          removeCube(cube, ovr, newCubes);
        } else {
          newCubes.push_back(cube);
        }
      }
      cubes = newCubes;
    }
    cout << "After " << (isOn[i] ? "on " : "off ") << rects[i].first << " " << rects[i].second << endl;
    int sum = 0;
    for (auto cube : cubes) {
      sum += rectSize(cube);
      cout << cube.first << " -> " << cube.second << endl;
    }
    cout << sum << endl;
  }
}

/*
for (int x = startPoints[i].x; x <= endPoints[i].x; x++) {
  for (int y = startPoints[i].y; y <= endPoints[i].y; y++) {
    for (int z = startPoints[i].z; z <= endPoints[i].z; z++) {
      if (-50 <= x && x <= 50 && -50 <= y && y <= 50 && -50 <= z && z <= 50) {
        if (isOn[i]) {
          onPoints.insert(Point3D(x,y,z));
        } else {
          onPoints.erase(Point3D(x,y,z));
        }
      }
    }
  }
}
*/
void part2() {

}

int main() {
  std::fstream data_file ("data/data22.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    regex pattern = regex ("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)");
    std::smatch sm = match(pattern, line);
    isOn.push_back(sm[1] == "on");
    auto start = Point3D(stoi(sm[2]), stoi(sm[4]), stoi(sm[6]));
    auto end = Point3D(stoi(sm[3]), stoi(sm[5]), stoi(sm[7]));
    rects.push_back(CUBE(start,end));
  }
  data_file.close();

  part1();
  part2();
}