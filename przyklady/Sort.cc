#include <iostream>
#include <string>
#include <vector>
using namespace std;

/* Sortowanie bąbelkowe listy liczb (n - ilość liczb) */
vector<int> Sort(vector<int> list, int n);

/* Compare And Swap - Zamienia miejscami list[i] i list[j] jeśli list[i] > list[j] */
vector<int> CAS(vector<int> list, int i, int j);

void Main();

/* Wypisuje listę (n - ilość elementów) */
void Print(vector<int> list, int n);

/* Sortowanie bąbelkowe listy liczb (n - ilość liczb) */
vector<int> Sort(vector<int> list, int n) {
  int i;
  int j;
  int _next = 0;
  while (_next > -1) {
    switch (_next) {
    case 0: _next = 1; break;
    case 1: i = n - 1; _next = 25; break;
    case 25: if (i >= 0) _next = 26; else _next = 31; break;
    case 26: j = 0; _next = 27; break;
    case 27: if (j < i) _next = 29; else _next = 30; break;
    case 29: list = CAS(list,j,j+1);
      j = j + 1; _next = 27; break;
    case 30: i = i - 1; _next = 25; break;
    case 31: return list; _next = -1; break;
    }
  }
}

/* Compare And Swap - Zamienia miejscami list[i] i list[j] jeśli list[i] > list[j] */
vector<int> CAS(vector<int> list, int i, int j) {
  int t;
  int _next = 2;
  while (_next > -1) {
    switch (_next) {
    case 2: _next = 28; break;
    case 28: if (list[i] > list[j]) _next = 3; else _next = 4; break;
    case 3: t = list[i];
      list[i] = list[j];
      list[j] = t; _next = 4; break;
    case 4: return list; _next = -1; break;
    }
  }
}

void Main() {
  vector<int> list(10);
  int n;
  int i;
  int t;
  int _next = 5;
  while (_next > -1) {
    switch (_next) {
    case 5: _next = 6; break;
    case 6: i = 0; _next = 7; break;
    case 7: cout << "Podaj ilość liczb: "; cin >> n; _next = 8; break;
    case 8: if (n > 0 && n <= 10) _next = 12; else _next = 24; break;
    case 12:  _next = 11; break;
    case 11: if (i < n) _next = 9; else _next = 13; break;
    case 9: cout << "list[" << i << "] = "; cin >> t; _next = 10; break;
    case 10: list[i] = t;
      i = i + 1; _next = 11; break;
    case 13: list = Sort(list, n);
      i = 0; _next = 14; break;
    case 14: cout << "Posortowana lista" << "\n"; _next = 20; break;
    case 20: /* Wypisz posortowaną listę */
      Print(list, n); _next = 21; break;
    case 21: _next = -1; break;
    case 24: cout << "Podaj liczbę z zakresu 1..10" << "\n"; _next = 7; break;
    }
  }
}

/* Wypisuje listę (n - ilość elementów) */
void Print(vector<int> list, int n) {
  int i;
  int t;
  int _next = 22;
  while (_next > -1) {
    switch (_next) {
    case 22: _next = 23; break;
    case 23: i = 0; _next = 15; break;
    case 15: if (i < n) _next = 16; else _next = 19; break;
    case 16: t = list[i]; _next = 17; break;
    case 17: cout << "list[" << i << "] = " << t << "" << "\n"; _next = 18; break;
    case 18: i = i + 1; _next = 15; break;
    case 19: _next = -1; break;
    }
  }
}


int main() {
  Main();
  return 0;
}
