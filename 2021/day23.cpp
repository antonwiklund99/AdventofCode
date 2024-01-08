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

/* char rooms[4][2] = {{'D','C'},{'C','A'}, {'D','A'}, {'B','B'}};
char hallway[11] = "..........."; */
char rooms[4][3] = {"DA", "BB", "CC", ".D"};
string hallway = "..........A";

int energyPerStep(char c) {
  switch (c) {
    case 'A': return 1;
    case 'B': return 10;
    case 'C': return 100;
    case 'D': return 1000;
    default:  return -1;
  }
}

int solve(int energy) {
  if (rooms[0][0] == 'A' && rooms[0][1] == 'A' && rooms[1][0] == 'B' && rooms[1][1] == 'B' &&
      rooms[2][0] == 'C' && rooms[2][1] == 'C' && rooms[3][0] == 'D' && rooms[3][1] == 'D') {
    std::cout << "FOUND RETURNING " << energy << endl;
    return energy;
  }
  std::cout << "ENERGY = " << energy << endl;
  std::cout << hallway << endl;
  std::cout << rooms[0] << " " << rooms[1] << " " << rooms[2] << " " << rooms[3] << endl;
  int res = INT32_MAX;
  for (int i = 0; i < 4; i++) {
    if (rooms[i][0] - 'A' == i && rooms[i][1] - 'A' == i) {
      continue;
    } else if (rooms[i][0] != '.' || (rooms[i][1] != '.' && rooms[i][1] - 'A' != i)) {
      int waitIndex = rooms[i][0] != '.' ? 0 : 1;
      char amph = rooms[i][waitIndex];
      int steps = waitIndex == 0 ? 1 : 2;
      int index = 2+i*2;
      for (int j = 1; j < 11-index; j++) {
        if (hallway[index+j] == '.') {
          hallway[index+j] = rooms[i][waitIndex];
          rooms[i][waitIndex] = '.';
          res = min(res, solve(energy + (steps+i)*energyPerStep(hallway[index+j])));
          rooms[i][waitIndex] = hallway[index+j];
          hallway[index+j] = '.';
        } else {
          break;
        }
      }
      for (int j = 1; j <= index; j++) {
        if (hallway[index-j] == '.') {
          hallway[index-j] = rooms[i][waitIndex];
          rooms[i][waitIndex] = '.';
          res = min(res, solve(energy + (steps+i)*energyPerStep(hallway[index-j])));
          rooms[i][waitIndex] = hallway[index-j];
          hallway[index-j] = '.';
        } else {
          break;
        }
      }
    }
  }
  for (int i = 0; i < 11; i++) {
    if (hallway[i] != '.') {
      char amph = hallway[i];
      int roomIndex = amph - 'A';
      // Check for path to room
      bool pathExists = true;
      if (roomIndex*2+2 > i) {
        for (int j = i+1; j <= roomIndex*2+2; j++) {
          if (hallway[j] != '.') {
            pathExists = false;
            break;
          }
        }
      } else {
        for (int j = i-1; j >= roomIndex*2+2; j--) {
          if (hallway[j] != '.') {
            pathExists = false;
            break;
          }
        }
      }
      if (pathExists) {
        if (rooms[roomIndex][0] == '.' && rooms[roomIndex][1] == '.') {
          rooms[roomIndex][1] = amph;
          hallway[i] = '.';
          res = min(res, solve(energy+(abs(roomIndex*2+2-i)+3)*energyPerStep(amph)));
          hallway[i] = amph;
          rooms[roomIndex][1] = '.';
        } else if (rooms[roomIndex][1] == amph && rooms[roomIndex][0] == '.') {
          rooms[roomIndex][0] = amph;
          hallway[i] = '.';
          res = min(res, solve(energy+(abs(roomIndex*2+2-i)+2)*energyPerStep(amph)));
          hallway[i] = amph;
          rooms[roomIndex][0] = '.';
        }
      }
    }
  }
  std::cout << "RETURNING " << res << endl;
  return res;
}

void part1() {
  std::cout << solve(0) << endl;
}

void part2() {

}

int main() {
  part1();
  part2();
}