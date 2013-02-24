#include <iostream>
#include <string>
#include <vector>
using namespace std;

/* Zlicza */
void Zliczanie();

/* Zlicza */
void Zliczanie() {
  int n;
  int _next = 0;
  while (_next > -1) {
    switch (_next) {
    case 0: _next = 6; break;
    case 6:  _next = 1; break;
    case 1: cout << "Podaj ilość iteracji: "; cin >> n; _next = 2; break;
    case 2: if (n > 0) _next = 3; else _next = 4; break;
    case 4: _next = -1; break;
    case 3: cout << "Iteracja nr " << n << "" << "\n"; _next = 5; break;
    case 5: n = n - 1; _next = 2; break;
    }
  }
}


int main() {
  Zliczanie();
  return 0;
}
