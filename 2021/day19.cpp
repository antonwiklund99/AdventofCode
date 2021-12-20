#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <unordered_set>
#include <unordered_map>
#include <cmath>
#include "util.h"
using namespace std;

vector<unordered_set<Point3D>> scans;
vector<Point3D> scannerPositions;

pair<bool,unordered_set<Point3D>*> tryMatch(const unordered_set<Point3D>& scan1, const unordered_set<Point3D>& scan2) {
  for (auto p1 : scan1) {
    for (auto p2 : scan2) {
      for (int x = 0; x < 4; x++) {
        for (int y = 0; y < 4; y++) {
          for (int z = 0; z < 4; z++) {
            // Assume p1 = p2
            int matches = 1;
            Point3D dv = p1.subbed(p2);
            for (Point3D p : scan2) {
              p.rotate90x(x);
              p.rotate90y(y);
              p.rotate90z(z);
              if (p != p2 && scan1.find(dv.added(p)) != scan1.end()) {
                matches++;
                if (matches >= 12) break;
              }
            }
            if (matches >= 12) {
              cout << "scanner found, pos " << dv << endl;
              scannerPositions.push_back(dv);
              unordered_set<Point3D>* adjusted = new unordered_set<Point3D>();
              for (Point3D p : scan2) {
                p.rotate90x(x);
                p.rotate90y(y);
                p.rotate90z(z);
                adjusted->insert(dv.added(p));
              }
              return pair<bool,unordered_set<Point3D>*>(true, adjusted);
            }
            p2.rotate90z();
          }
          p2.rotate90y();
        }
        p2.rotate90x();
      }
    }
  }
  return pair<bool,unordered_set<Point3D>*>(false, nullptr);
}

void part1() {
  vector<unordered_set<Point3D>*> found;
  bool alreadyFound[scans.size()];
  for (int i = 0; i < scans.size(); i++) {
    alreadyFound[i] = false;
  }
  found.push_back(&scans[0]);
  alreadyFound[0] = true;
  int i = 0;
  while (found.size() != scans.size()) {
    for (int j = 0; j < scans.size(); j++) {
      if (!alreadyFound[j]) {
        auto match = tryMatch(*found[i], scans[j]);
        if (match.first) {
          found.push_back(match.second);
          alreadyFound[j] = true;
          if (found.size() == scans.size()) break;
        }
      }
    }
    i++;
  }
  unordered_set<Point3D> beacons;
  for (auto scan : found) {
    for (auto p : *scan) {
      beacons.insert(p);
    }
  }
  cout << beacons.size() << endl;
}

void part2() {
  int res = 0;
  for (int i = 0; i < scannerPositions.size(); i++) {
    for (int j = i+1; j < scannerPositions.size(); j++) {
      Point3D p1 = scannerPositions[i];
      Point3D p2 = scannerPositions[j];
      res = max(res, abs(p1.x-p2.x) + abs(p1.y-p2.y) + abs(p1.z-p2.z));
    }
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data19.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    unordered_set<Point3D> points;
    while (getline(data_file, line) && line.size() != 0) {
      vector<string> splitted = split(line,',');
      points.insert(Point3D(stoi(splitted[0]), stoi(splitted[1]), stoi(splitted[2])));
    }
    scans.push_back(points);
  }
  data_file.close();

  part1();
  part2();
}