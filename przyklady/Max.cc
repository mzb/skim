#include <iostream>
#include <string>
#include <vector>
using namespace std;

/* Zwraca maksymalną liczbę z podanej listy */
int Max(vector<int> lista);

/* Testuje Max */
void Main();

/* Zwraca maksymalną liczbę z podanej listy */
int Max(vector<int> lista) {
  int max;
  int i;
  int _next = 0;
  while (_next > -1) {
    switch (_next) {
    case 0: _next = 1; break;
    case 1: max = lista[0];
      i = 1; _next = 2; break;
    case 2: if (i < lista.size()) _next = 3; else _next = 6; break;
    case 3: if (lista[i] > max) _next = 4; else _next = 5; break;
    case 4: /* Znaleziono nowe maksimum */
      max = lista[i]; _next = 5; break;
    case 5: i = i + 1; _next = 2; break;
    case 6: /* Zwróć znalezione maksimum */
      return max; _next = -1; break;
    }
  }
}

/* Testuje Max */
void Main() {
  vector<int> lista(10);
  int l;
  int i;
  int n;
  int max;
  int _next = 7;
  while (_next > -1) {
    switch (_next) {
    case 7: _next = 8; break;
    case 8: i = 0; _next = 9; break;
    case 9: cout << "Podaj ilość liczb: "; cin >> n; _next = 10; break;
    case 10: if (n > 0) _next = 13; else _next = 17; break;
    case 13: if (i < n) _next = 12; else _next = 14; break;
    case 12: cout << "Podaj kolejną liczbę: "; cin >> l; _next = 11; break;
    case 11: lista[i] = l;
      i = i + 1; _next = 13; break;
    case 14: /* Znajdz maksimum  */
      max = Max(lista); _next = 15; break;
    case 15: cout << "Maksymalna liczba to: " << max << "" << "\n"; _next = 16; break;
    case 16: _next = -1; break;
    case 17: cout << "Podana wartość jest nieprawidłowa. Wciśnij \"Cancel\" aby przerwać" << "\n"; _next = 9; break;
    }
  }
}


int main() {
  Main();
  return 0;
}
