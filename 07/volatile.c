volatile int res;
void main(void) {
  res = 1;
  int max = 10;
  for (int i = 1; i <= max; i++) {
    res *= i;
  }
  int a = res;
}
