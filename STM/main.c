/**
  ******************************************************************************
  * @file    main.c
  * @author  Ac6
  * @version V1.0
  * @date    01-December-2013
  * @brief   Default main function.
  ******************************************************************************
*/


#include "stm32f4xx.h"
#include "stm32f4_discovery.h"
#include "arm_math.h"
#include "arm_common_tables.h"
#include "arm_const_structs.h"
#include "Pedometr.h"

int main(void)
{
	SystemInit();

    LedOutInit(GPIOD,GPIO_Pin_12,1);
    LedOutInit(GPIOD,GPIO_Pin_13,0);
    USART3Init(9600);
    Tim3Init(10000,840);
    LisInit();

    while(1)
	{
    	if (n >= SAMPLES)
		{
			arm_cfft_radix4_init_f32(&S, FFT_SIZE, 0, 1);
			arm_cfft_radix4_f32(&S, FFT_InputSamples); // tutaj sie zawiesza
			USART_puts(USART3, "A");

			/*
			arm_cmplx_mag_f32(FFT_InputSamples, FFT_OutputSamples, FFT_SIZE);
			arm_max_f32(FFT_OutputSamples, FFT_SIZE, &maxValue, &maxIndex);
			*/

			n = 0;
		}
	}
}
