#include <math.h>
#include <string>
#include <vector>
#include <regex>

#define PI 3.14159265

class Point2D {
  public:
    double x,y;
    Point2D(int x, int y) {
      this->x = x;
      this->y = y;
    }

    Point2D(double x, double y) {
      this->x = x;
      this->y = y;
    }

    const double distTo(const Point2D& other) {
      double dx = x - other.x;
      double dy = y - other.y;
      return sqrt(dx*dx + dy*dy);
    }

    const double angleTo(const Point2D& other) {
      double angle = atan2(other.x - x, other.y - y) * 180 / PI;
      return angle + ceil(-angle/360)*360;
    }

    void rotate(double deg) {
      double theta = deg * PI / 180;
      double newX = x*cos(theta) - y*sin(theta);
      y = round(y*cos(theta) + x*sin(theta));
      x = round(newX);
    }

    void add(double dx, double dy) {
      x += dx;
      y += dy;
    }

    Point2D added(double dx, double dy) {
      return Point2D(x + dx, y + dy);
    }

    bool operator ==(const Point2D& other) const {
      if (x == other.x && y == other.y) {
        return 1;
      } else {
        return 0;
      }
    }
};

namespace std {
  template<>
  struct hash<Point2D> {
      size_t operator()(const Point2D& obj) const {
        std::hash<double> double_hasher;
        return double_hasher(obj.x) ^ double_hasher(obj.y);
      }
    };
}

std::vector<std::string> split(const std::string& str, char delim) {
  std::stringstream strStream(str);
  std::string value;
  std::vector<std::string> splitted;
  while (std::getline(strStream, value, delim)) {
    splitted.push_back(value);
  }
  return splitted;
}

void removeChar(std::string& str, char c) {
  str.erase(remove(str.begin(), str.end(), c), str.end());
}

std::smatch match(const std::regex& pattern, const std::string& str) {
  std::smatch sm;
  std::regex_match(str, sm, pattern);
  return sm;
}