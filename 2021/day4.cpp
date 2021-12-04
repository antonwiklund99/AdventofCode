#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <unordered_set>
#include <unordered_map>
#include "util.h"
using namespace std;

vector<int> draws;
vector<int>::iterator drawItr;
vector<unordered_map<int,pair<int,int>>*> players;
vector<array<array<bool,5>,5>> marked;
unordered_set<int> alreadyWon;

void part1() {
  while (drawItr != draws.end()) {
    for (int i = 0; i < players.size(); i++) {
      auto player = players[i];
      if (player->find(*drawItr) != player->end()) {
        pair<int,int> cord = player->at(*drawItr);
        marked[i][cord.first][cord.second] = true;
        bool win = true;
        for (int j = 0; j < 5; j++) {
          if (!marked[i][cord.first][j]) {
            win = false;
            break;
          }
        }
        if (!win) {
          win = true;
          for (int j = 0; j < 5; j++) {
            if (!marked[i][j][cord.second]) {
              win = false;
              break;
            }
          }
        }
        if (win) {
          int sum = 0;
          alreadyWon.insert(i);
          for (auto itr = player->begin(); itr != player->end(); itr++) {
            pair<int,int> p = itr->second;
            if (!marked[i][p.first][p.second]) sum += itr->first;
          }
          cout << sum*(*drawItr) << endl;
          return;
        }
      }
    }
    drawItr++;
  }
}

void part2() {
  while (drawItr != draws.end()) {
    for (int i = 0; i < players.size(); i++) {
      if (alreadyWon.find(i) == alreadyWon.end()) {
        auto player = players[i];
        if (player->find(*drawItr) != player->end()) {
          pair<int,int> cord = player->at(*drawItr);
          marked[i][cord.first][cord.second] = true;
          bool win = true;
          for (int j = 0; j < 5; j++) {
            if (!marked[i][cord.first][j]) {
              win = false;
              break;
            }
          }
          if (!win) {
            win = true;
            for (int j = 0; j < 5; j++) {
              if (!marked[i][j][cord.second]) {
                win = false;
                break;
              }
            }
          }
          if (win) {
            alreadyWon.insert(i);
            if (alreadyWon.size() == players.size()) {
              int sum = 0;
              for (auto itr = player->begin(); itr != player->end(); itr++) {
                pair<int,int> p = itr->second;
                if (!marked[i][p.first][p.second]) sum += itr->first;
              }
              cout << sum*(*drawItr) << endl;
              return;
            }
          }
        }
      }
    }
    drawItr++;
  }
}

int main() {
  std::fstream data_file ("data/data4.txt", std::ios::in);
  string line;
  getline(data_file, line);
  vector<string> drawStrings = split(line, ',');
  for (string s : drawStrings) {
    draws.push_back(stoi(s));
  }
  drawItr = draws.begin();
  while (getline(data_file, line)) {
    if (!line.size() == 0) {
      vector<string> splitted = split(line, ' ');
      auto grid = new unordered_map<int, pair<int,int>>();
      for (int r = 0; r < 5; r++) {
        if (r != 0) {
          getline(data_file, line);
          splitted = split(line, ' ');
        }
        int c = 0;
        auto itr = splitted.begin();
        while (c < 5) {
          removeWhitespace(*itr);
          if (itr->size() != 0) {
            (*grid)[stoi(*itr)] = pair<int,int>(r,c);
            c++;
          }
          itr++;
        }
      }
      players.push_back(grid);
      marked.push_back(array<array<bool,5>,5>{false});
    }
  }
  data_file.close();

  part1();
  part2();
}