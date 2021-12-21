#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>
#include "util.h"
using namespace std;

int start1 = 3;
int start2 = 5;
int rollsPerStep[10];

void initRollsPerStep() {
  for (int i = 0; i < 10; i++) rollsPerStep[i] = 0;
  for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
      for (int h = 1; h <= 3; h++) {
        rollsPerStep[(i+j+h)%10]++;
      }
    }
  }
}

pair<long,long> calcWins(int player1, int player2, int score1, int score2, bool turn1) {
  pair<long,long> res = pair<long,long>(0,0);
  for (int i = 3; i < 10; i++) {
    if (turn1) {
      int p1 = (player1 + i) % 10;
      int s1 = score1 + p1 + 1;
      if (s1 >= 21) {
        res.first += rollsPerStep[i];
      } else {
        auto r = calcWins(p1, player2, s1, score2, false);
        res.first += rollsPerStep[i]*r.first;
        res.second += rollsPerStep[i]*r.second;
      }
    } else {
      int p2 = (player2 + i) % 10;
      int s2 = score2 + p2 + 1;
      if (s2 >= 21) {
        res.second += rollsPerStep[i];
      } else {
        auto r = calcWins(player1, p2, score1, s2, true);
        res.first += rollsPerStep[i]*r.first;
        res.second += rollsPerStep[i]*r.second;
      }
    }
  }
  return res;
}

void part1() {
  int next = 0;
  int score1 = 0;
  int score2 = 0;
  int player1 = start1-1;
  int player2 = start2-1;
  bool turn1 = true;
  int rolls = 0;
  while (score1 < 1000 && score2 < 1000) {
    rolls += 3;
    if (turn1) {
      player1 = (player1 + 3*(next+1) + 3) % 10;
      score1 += player1 + 1;
    } else {
      player2 = (player2 + 3*(next+1) + 3) % 10;
      score2 += player2 + 1;
    }
    next = (next + 3) % 100;
    turn1 = !turn1;
  }
  cout << rolls*min(score1,score2) << endl;
}

void part2() {
  initRollsPerStep();
  auto res = calcWins(start1 - 1, start2 - 1, 0, 0, true);
  cout << max(res.first,res.second) << endl;
}

int main() {
  part1();
  part2();
}