#include <iostream>
#include <string>
#include <vector>
using namespace std;

/* Sumuje dwie liczby */
void Suma();

/* Sumuje dwie liczby */
void Suma() {
  int suma;
  int a;
  int b;
  int _next = 0;
  while (_next > -1) {
    switch (_next) {
    case 0: _next = 6; break;
    case 6:  _next = 2; break;
    case 2: cout << "Podaj a: "; cin >> a; _next = 3; break;
    case 3: cout << "Podaj b: "; cin >> b; _next = 1; break;
    case 1: suma = a + b; _next = 4; break;
    case 4: cout << "" << a << " + " << b << " = " << suma << "" << "\n"; _next = 5; break;
    case 5: _next = -1; break;
    }
  }
}


int main() {
  Suma();
  return 0;
}
