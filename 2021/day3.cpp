#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include "util.h"
using namespace std;

vector<int> nums;
int bits;

void part1() {
  int res = 0;
  for (int i = 1 << (bits-1); i != 0; i >>= 1) {
    int ones = count_if(nums.begin(), nums.end(), [i](int n) { return n & i; });
    if (ones > nums.size()/2) res += i;
  }
  cout << res*(res^((1 << bits) - 1)) << endl;
}

void part2() {
  vector<int> ox(nums.size());
  vector<int> co(nums.size());
  copy(nums.begin(), nums.end(), ox.begin());
  copy(nums.begin(), nums.end(), co.begin());
  int bit = 1 << (bits-1);
  while (ox.size() != 1) {
    int ones = count_if(ox.begin(), ox.end(), [bit](int n) { return n & bit; });
    bool keepOnes = ones >= (ox.size() - ones);
    ox.erase(remove_if(ox.begin(), ox.end(), [bit,keepOnes](int n) {
      return keepOnes ^ (bool) (n & bit);
    }), ox.end());
    bit >>= 1;
  }
  bit = 1 << (bits-1);
  while (co.size() != 1) {
    int ones = count_if(co.begin(), co.end(), [bit](int n) { return n & bit; });
    bool keepOnes= ones < (co.size() - ones);
    co.erase(remove_if(co.begin(), co.end(), [bit,keepOnes](int n) {
      return keepOnes ^ (bool) (n & bit);
    }), co.end());
    bit >>= 1;
  }
  cout << ox[0]*co[0] << endl;
}

int main() {
  fstream data_file ("data/data3.txt", ios::in);
  string line;
  while (getline(data_file, line)) {
    bits = line.size();
    nums.push_back(stoi(line,0,2));
  }
  data_file.close();

  part1();
  part2();
}