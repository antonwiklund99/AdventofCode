#include <vector>
#include <string>
#include <map>
#include <set>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <unordered_set>
#include <stack>
#include <regex>
#include "util.h"
using namespace std;

enum OP {
  INP,
  ADD,
  MUL,
  DIV,
  MOD,
  EQL
};

struct Instruction {
  OP op;
  int a;
  int b;
};

vector<Instruction> instructions;

void part1() {

}

void part2() {

}

int main() {
  std::fstream data_file ("data/data24.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    regex pattern = regex ("(inp|add|mul|div|mod|eql) (w|x|y|z) ?(-?\\d+|w|x|y|z)?");
    std::smatch sm = match(pattern, line);
    cout << sm.length() << endl;
    Instruction instr;
    if (sm[1] == "inp") {
      instr.op = INP;
      instr.a = stoi(sm[2]);
    } else if (sm[1] == "add") {
      instr.op = ADD;
      instr.a = stoi(sm[2]);
      instr.b = stoi(sm[3]);
    } else if (sm[1] == "mul") {
      instr.op = MUL;
      instr.a = stoi(sm[2]);
      instr.b = stoi(sm[3]);
    } else if (sm[1] == "div") {
      instr.op = DIV;
      instr.a = stoi(sm[2]);
      instr.b = stoi(sm[3]);
    } else if (sm[1] == "mod") {
      instr.op = MOD;
      instr.a = stoi(sm[2]);
      instr.b = stoi(sm[3]);
    } else if (sm[1] == "eql") {
      instr.op = EQL;
      instr.a = stoi(sm[2]);
      instr.b = stoi(sm[3]);
    } else {
      cout << sm[1] << endl;
    }
  }
  data_file.close();

  part1();
  part2();
}