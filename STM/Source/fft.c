#include "fft.h"
/***************************************************************
* fft.c
* Douglas L. Jones
* University of Illinois at Urbana-Champaign
* January 19, 1992
* http://cnx.rice.edu/content/m12016/latest/
*
*   fft: in-place radix-2 DIT DFT of a complex input
*
*   input:
* n: length of FFT: must be a power of two
* m: n = 2**m
*   input/output
* x: double array of length n with real part of data
* y: double array of length n with imag part of data
*
*   Permission to copy and use this program is granted
*   as long as this header is included.
****************************************************************/

void fft(double x[], double y[], int n) {
        int m = (int) (log(n) / log(2));
        int i, j, k, n1, n2, a;
        double c, s, t1, t2;
        int z;

        double *Cos = (double*)malloc(sizeof(double) * n / 2);
        double *Sin = (double*)malloc(sizeof(double) * n / 2);

        for (z = 0; z < n / 2; z++) {
            Cos[z] = cos(-2 * M_PI * z / n);
            Sin[z] = sin(-2 * M_PI * z / n);
        }
        // Bit-reverse

        j = 0;
        n2 = n / 2;
        for (i = 1; i < n - 1; i++) {
            n1 = n2;
            while (j >= n1) {
                j = j - n1;
                n1 = n1 / 2;
            }
            j = j + n1;

            if (i < j) {
                t1 = x[i];
                x[i] = x[j];
                x[j] = t1;
                t1 = y[i];
                y[i] = y[j];
                y[j] = t1;
            }
        }

        // FFT
        n1 = 0;
        n2 = 1;

        for (i = 0; i < m; i++) {
            n1 = n2;
            n2 = n2 + n2;
            a = 0;

            for (j = 0; j < n1; j++) {
                c = Cos[a];
                s = Sin[a];
                a += 1 << (m - i - 1);

                for (k = j; k < n; k = k + n2) {
                    t1 = c * x[k + n1] - s * y[k + n1];
                    t2 = s * x[k + n1] + c * y[k + n1];
                    x[k + n1] = x[k] - t1;
                    y[k + n1] = y[k] - t2;
                    x[k] = x[k] + t1;
                    y[k] = y[k] + t2;
                }
            }
        }
        free(Sin);
        free(Cos);

    }
/////// M.MELLER////////////////////////////////////
void CalcMagnitude(double Re[], double Im[], double Mag[], int n)
{
	for (int i = 0; i < n; i++)
		Mag[i] = round(sqrt(pow(Re[i], 2) + pow(Im[i], 2)));
}
double _pdf(double x) {
	return exp(-x*x / 2) / sqrt(2 * M_PI);
}
double pdf(double x, double mu, double sigma) {
	return _pdf((x - mu) / sigma) / sigma;
}

int GetMaxIndex(double array[], int n)
{
	double max = array[0];
	int indexMax = 0;
	int i;
	for (i = 1; i < n; i++)
		if (array[i] > max)
		{
			max = array[i];
			indexMax = i;
		}
	return indexMax + 1;
}
