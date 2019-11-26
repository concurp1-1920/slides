/*
 *  The result of the computation is
 *
 *  1 * 2 * 3 * ... * 10 = 10! = 3628800
 *
 *  To show that volatile prevent messes by the compiler :
 *
 *  $> gcc -O0 main.c
 *  $> objdump -d a.out
 *
 *  Here you see that the main function has a loop.
 *
 *  $> gcc -O3 main.c
 *  $> objdump -d a.out
 *
 *  Here the main function only has 4 line :
 *
 *   0000000000001040 <main>:
 *   1040:       c7 05 ca 2f 00 00 00    movl   $0x375f00,0x2fca(%rip)        # 4014 <res>
 *   1047:       5f 37 00
 *   104a:       b8 01 00 00 00          mov    $0x1,%eax
 *   104f:       c3                      retq
 *
 *   The compiler execute the code and move the result (which is 0x37500) into the variable a.
 *
 *   $> echo "obase=10; ibase=16; 375F00" | bc
 *   3628800
 *
 *   By adding the volatile keyword in front of res, the compiler is prevented to compute something at compile time.
 *   But it will unroll the loop.
 */

int res;
int main(void) {
    res = 1;
    int max = 10;
    for (int i = 1; i <= max; i++) {
        res *= i;
    }
    int a = res;
    return 1;
}
