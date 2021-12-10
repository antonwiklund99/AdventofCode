#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <stack>
#include "util.h"
using namespace std;

vector<string> lines;

void part1() {
  int res = 0;
  for (int i = 0; i < lines.size(); i++) {
    stack<char> s;
    for (char c : lines[i]) {
      if (c == '(') s.push(c);
      else if (c == '[') s.push(c);
      else if (c == '{') s.push(c);
      else if (c == '<') s.push(c);
      else if (c == ')') {
        if (s.top() != '(') {
          res += 3;
          break;
        }
        s.pop();
      } else if (c == ']') {
        if (s.top() != '[') {
          res += 57;
          break;
        }
        s.pop();
      } else if (c == '}') {
        if (s.top() != '{') {
          res += 1197;
          break;
        }
        s.pop();
      } else if (c == '>') {
        if (s.top() != '<') {
          res += 25137;
          break;
        }
        s.pop();
      }
    }
  }
  cout << res << endl;
}

void part2() {
  vector<long> scores;
  for (int i = 0; i < lines.size(); i++) {
    stack<char> s;
    bool err = false;
    for (char c : lines[i]) {
      if (c == '(') s.push(c);
      else if (c == '[') s.push(c);
      else if (c == '{') s.push(c);
      else if (c == '<') s.push(c);
      else if (c == ')') {
        if (s.top() != '(') {
          err = true;
          break;
        }
        s.pop();
      } else if (c == '}') {
        if (s.top() != '{') {
          err = true;
          break;
        }
        s.pop();
      } else if (c == ']') {
        if (s.top() != '[') {
          err = true;
          break;
        }
        s.pop();
      } else if (c == '>') {
        if (s.top() != '<') {
          err = true;
          break;
        }
        s.pop();
      }
    }
    if (!err) {
      long score = 0;
      while (s.size() != 0) {
        score *= 5;
        if (s.top() == '(') score += 1;
        else if (s.top() == '[') score += 2;
        else if (s.top() == '{') score += 3;
        else if (s.top() == '<') score += 4;
        s.pop();
      }
      scores.push_back(score);
    }
  }
  sort(scores.begin(),scores.end());
  cout << scores[scores.size()/2] << endl;
}

int main() {
  std::fstream data_file ("data/data10.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    lines.push_back(line);
  }
  data_file.close();

  part1();
  part2();
}