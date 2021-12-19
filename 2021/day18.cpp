#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>
#include "util.h"
using namespace std;

vector<string> lines;

struct Element {
  bool isNumber;
  int value;
  Element* x;
  Element* y;
};

struct ExplosionRes {
  bool found;
  bool leftAdded;
  bool rightAdded;
  Element* explodedElement;
};

Element* parseElement(string& s) {
  Element* ele = new Element;
  char head = s[0];
  s.erase(0,1);
  if (isdigit(head)) {
    ele->isNumber = true;
    ele->value = head - '0';

  } else {
    ele->isNumber = false;
    ele->x = parseElement(s);
    s.erase(0,1);
    ele->y = parseElement(s);
    s.erase(0,1);
  }
  return ele;
}

void printe(Element* e) {
  if (e->isNumber) {
    cout << e->value;
  } else {
    cout << "[";
    printe(e->x);
    cout << ",";
    printe(e->y);
    cout << "]";
  }
}

bool addToNumber(Element* searchElem, Element* addElement, bool leftSearch) {
  if (searchElem->isNumber) {
    if (addElement->isNumber) {
      searchElem->value += addElement->value;
    } else {
      Element* old = new Element;
      old->value = searchElem->value;
      old->isNumber = true;
      searchElem->isNumber = false;
      if (leftSearch) {
        searchElem->x = old;
        searchElem->y = addElement;
      } else {
        searchElem->x = addElement;
        searchElem->y = old;
      }
    }
    return true;
  } else if (leftSearch) {
    return addToNumber(searchElem->y, addElement, leftSearch) || addToNumber(searchElem->x, addElement, leftSearch);
  } else {
    return addToNumber(searchElem->x, addElement, leftSearch) || addToNumber(searchElem->y, addElement, leftSearch);
  }
}

void freeElement(Element* elem) {
  if (!elem->isNumber) {
    freeElement(elem->x);
    freeElement(elem->y);
  }
  free(elem);
}

ExplosionRes findAndExplode(Element* elem, int depth) {
  if (elem->isNumber) {
    return ExplosionRes{false};
  } else if (depth == 4 && elem->x->isNumber && elem->y->isNumber) {
    Element* exploded = new Element;
    exploded->isNumber = false;
    exploded->x = elem->x;
    exploded->y = elem->y;
    elem->isNumber = true;
    elem->value = 0;
    elem->x = nullptr;
    elem->y = nullptr;
    ExplosionRes res;
    res.explodedElement = exploded;
    res.found = true;
    res.leftAdded = false;
    res.rightAdded = false;
    return res;
  } else {
    ExplosionRes res = findAndExplode(elem->x, depth + 1);
    if (res.found) {
      if (!res.rightAdded) {
        res.rightAdded = addToNumber(elem->y, res.explodedElement->y, false);
      }
    } else {
      res = findAndExplode(elem->y, depth + 1);
      if (res.found && !res.leftAdded) {
        res.leftAdded = addToNumber(elem->x, res.explodedElement->x, true);
      }
    }
    return res;
  }
}

bool findAndSplit(Element* elem) {
  if (elem->isNumber) {
    if (elem->value > 9) {
      Element* left = new Element();
      Element* right = new Element();
      left->isNumber = true;
      left->value = elem->value/2;
      right->isNumber = true;
      right->value = ceil(((double) elem->value)/2);
      elem->isNumber = false;
      elem->x = left;
      elem->y = right;
      return true;
    } else {
      return false;
    }
  } else {
    return findAndSplit(elem->x) || findAndSplit(elem->y);
  }
}

void reduce(Element* elem) {
  while (true) {
    ExplosionRes expRes = findAndExplode(elem, 0);
    if (expRes.found) {
      if (!expRes.leftAdded) freeElement(expRes.explodedElement->x);
      if (!expRes.rightAdded) freeElement(expRes.explodedElement->y);
      continue;
    }
    if (findAndSplit(elem)) {
      continue;
    }
    break;
  }
}

int magnitude(Element* elem) {
  if (elem->isNumber) {
    return elem->value;
  } else {
    return 3*magnitude(elem->x) + 2*magnitude(elem->y);
  }
}

void part1() {
  vector<Element*> nums;
  for (string line : lines) {
    Element* elem = parseElement(line);
    nums.push_back(elem);
  }
  Element* res = nums[0];
  for (int i = 1; i < nums.size(); i++) {
    Element* added = new Element;
    added->isNumber = false;
    added->x = res;
    added->y = nums[i];
    reduce(added);
    res = added;
  }
  cout << magnitude(res) << endl;
}

void part2() {
  int res = 0;
  for (int i = 0; i < lines.size(); i++) {
    for (int j = 0; j < lines.size(); j++) {
      if (i != j) {
        Element* elem = new Element;
        elem->isNumber = false;
        string xs = lines[i];
        string ys = lines[j];
        elem->x = parseElement(xs);
        elem->y = parseElement(ys);
        reduce(elem);
        res = max(res, magnitude(elem));
        freeElement(elem);
      }
    }
  }
  cout << res << endl;
}

int main() {
  std::fstream data_file ("data/data18.txt", std::ios::in);
  string line;
  while (getline(data_file, line)) {
    lines.push_back(line);
  }
  data_file.close();

  part1();
  part2();
}