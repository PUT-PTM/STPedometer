package com.example.maks.fft;
/******************************************************************************
*  Copyright 2006-2007 Columbia University.
*
*  This file is part of MEAPsoft.
*
*  MEAPsoft is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License version 2 as
*  published by the Free Software Foundation.
*
*  MEAPsoft is distributed in the hope that it will be useful, but
*  WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with MEAPsoft; if not, write to the Free Software
*  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
*  02110-1301 USA
*
*  See the file "COPYING" for the text of the license.
*/

import static android.R.attr.max;

/******************************************************************************
 *  Compilation:  javac Gaussian.java
 *  Execution:    java Gaussian x mu sigma
 *
 *  Function to compute the Gaussian pdf (probability density function)
 *  and the Gaussian cdf (cumulative density function)
 *
 *  % java Gaussian 820 1019 209
 *  0.17050966869132111
 *
 *  % java Gaussian 1500 1019 209
 *  0.9893164837383883
 *
 *  % java Gaussian 1500 1025 231
 *  0.9801220907365489
 *
 *  The approximation is accurate to absolute error less than 8 * 10^(-16).
 *  Reference: Evaluating the Normal Distribution by George Marsaglia.
 *  http://www.jstatsoft.org/v11/a04/paper
 *
 ******************************************************************************/
public class FFT {

    int n, m;

    // Lookup tables. Only need to recompute when size of FFT changes.
    double[] cos;
    double[] sin;

    public FFT(int n) {
        this.n = n;
        this.m = (int) (Math.log(n) / Math.log(2));

        // Make sure n is a power of 2
        if (n != (1 << m))
            throw new RuntimeException("FFT length must be power of 2");

        // precompute tables
        cos = new double[n / 2];
        sin = new double[n / 2];

        for (int i = 0; i < n / 2; i++) {
            cos[i] = Math.cos(-2 * Math.PI * i / n);
            sin[i] = Math.sin(-2 * Math.PI * i / n);
        }

    }

    private void fft(double[] x, double[] y) {
        int i, j, k, n1, n2, a;
        double c, s, t1, t2;

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
                c = cos[a];
                s = sin[a];
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
    }

    /* MM */
    private void CalcMagnitude(double[] Re, double[] Im, double[] Mag)
    {
        for (int i = 0; i < n; i++)
            Mag[i] = Math.round(Math.sqrt(Math.pow(Re[i], 2) + Math.pow(Im[i], 2)));
    }
    public void PrintReIm(double[] Re, double[] Im)
    {
        System.out.print("Re: [");
        for(int i = 0; i < Re.length; i++)
            System.out.print(((int)(Re[i]*1000)/1000.0) + " ");

        System.out.print("]\nIm: [");
        for(int i = 0; i < Im.length; i++)
            System.out.print(((int)(Im[i]*1000)/1000.0) + " ");

        System.out.println("]");
    }
    private static double pdf(double x) {
        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
    }
    private static double pdf(double x, double Mu, double Sigma) {
        return pdf((x - Mu) / Sigma) / Sigma;
    }
    private static int GetMaxIndex(double[] Array, int N)
    {
        double Max = Array[0];
        int IndexMax = 0;
        for (int i = 1; i < N; i++)
            if (Array[i] > max)
            {
                Max = Array[i];
                IndexMax = i;
            }
        return IndexMax + 1;
    }

    public boolean CheckFrequency(double[] Samples, int ExpectedFrequency) {
        int ActualFrequency = 0;
        int MaxMagnitudeFrequency;
        double Re[] = new double[n];
        double Im[] = new double[n];
        double Mag[] = new double[n];
        double Gauss[] = new double[n];
        double[] Result = new double[n];

        for (int i = 0; i < n; i++)
            Re[i] = Samples[i];
        for (int i = 0; i < n; i++)
            Im[i] = 0;

        fft(Re, Im);
        CalcMagnitude(Re, Im, Mag);
        MaxMagnitudeFrequency = GetMaxIndex(Mag,n);

        /* Opcjonalne */
        /*
        int k = -(n / 2) + 1;
        for (int i = 0; i < n; i++)
        {
            Gauss[i] = pdf(k, MaxMagnitudeFrequency, 1);
            k += 2;
        }
        for(int i=0; i<n; i++)
            Result[i] = ((int)(Mag[i] * Gauss[i]) * 100) / 100;

        ActualFrequency = GetMaxIndex(Result,n);

        if (ActualFrequency == ExpectedFrequency)
            return true;
        */

        if (MaxMagnitudeFrequency == ExpectedFrequency)
            return true;
        return false;
    }
    /* MM */
}