#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include "util.h"
using namespace std;

vector<vector<string>> signals;
vector<vector<string>> outputs;

void part1() {
  int res = 0;
  for (int i = 0; i < outputs.size(); i++) {
    for (auto output : outputs[i]) {
      if (2 == output.size()) res++;
      if (4 == output.size()) res++;
      if (3 == output.size()) res++;
      if (7 == output.size()) res++;
    }
  }
  cout << res << endl;
}

char diffOne(string s1, string s2) {
  for (char c : s1) {
    if (s2.find(c) == string::npos) {
      return c;
    }
  }
  return 'X';
}

string common(string s1, string s2, string ignoreChars) {
  string res;
  for (char c : s1) {
    if (s2.find(c) != string::npos && ignoreChars.find(c) == string::npos) {
      res += c;
    }
  }
  return res;
} 

void part2() {
  long res = 0;
  for (int i = 0; i < outputs.size(); i++) {
    string nums[10];
    for (auto signal : signals[i]) {
      if (signal.size() == 2) nums[1] = signal;
      if (signal.size() == 4) nums[4] = signal;
      if (signal.size() == 3) nums[7] = signal;
      if (signal.size() == 7) nums[8] = signal;
    }
    char mappings[7]{0};
    string cf = common(nums[1],nums[7],"");
    mappings[0] = diffOne(nums[7], nums[1]);
    // find 6
    for (auto signal : signals[i]) {
      if (signal.size() == 6) {
        string comm = common(signal, nums[8], cf);
        if (comm.size() == 5) {
          nums[6] = signal;
          mappings[2] = diffOne(nums[8], nums[6]);
          break;
        }
      }
    }
    removeChar(cf, mappings[2]);
    mappings[5] = cf[0];
    // find 0
    for (auto signal : signals[i]) {
      if (signal.size() == 6) {
        string comm = common(nums[4], signal, string{mappings[2],mappings[5]});
        if (comm.size() == 1) {
          nums[0] = signal;
          mappings[1] = comm[0];
          break;
        }
      }
    }
    //find b
    mappings[3] = diffOne(nums[4],nums[0]);
    // find g and 5
    string abdf = {mappings[0],mappings[1],mappings[3],mappings[5]};
    for (auto signal : signals[i]) {
      if (signal.size() == 5) {
        char diff = diffOne(signal, abdf);
        string comm = common(signal, abdf, "");
        if (diff != 'X' && comm.size() == 4) {
          mappings[6] = diff;
          nums[5] = signal;
          break;
        }
      }
    }
    // find e
    mappings[4] = diffOne(nums[8], abdf + string{mappings[2],mappings[6]});

    nums[2] = string{mappings[0],mappings[2],mappings[3],mappings[4],mappings[6]};
    nums[3] = string{mappings[0],mappings[2],mappings[3],mappings[5],mappings[6]};
    nums[9] = string{mappings[0],mappings[1],mappings[2],mappings[3],mappings[5],mappings[6]};
    
    long val = 0;
    for (auto digit : outputs[i]) {
      // find nbr
      for (int j = 0; j < 10; j++) {
        if (nums[j].size() == digit.size() && common(nums[j],digit,"").size() == digit.size()) {
          val *= 10;
          val += j;
          break;
        }
      }
    }
    res += val;
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data8.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    bool start = true;
    vector<string> ss;
    vector<string> so;
    for (auto s : split(line,' ')) {
      if (s == "|") start = false;
      else if (start) ss.push_back(s);
      else so.push_back(s);
    }
    signals.push_back(ss);
    outputs.push_back(so);
  }
  data_file.close();

  part1();
  part2();
}