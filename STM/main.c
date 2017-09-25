/**
 ******************************************************************************
 * @file    main.c
 * @author  Ac6
 * @version V1.0
 * @date    01-December-2013
 * @brief   Default main function.
 ******************************************************************************
 */

#include "Pedometr.h"

int i;

void TIM3_IRQHandler(void) {
	if (TIM_GetITStatus(TIM3, TIM_IT_Update) != RESET) {

		TM_LIS302DL_LIS3DSH_ReadAxes(&Axes_Data);

		GPIO_ToggleBits(GPIOD, GPIO_Pin_12);
		GPIO_ToggleBits(GPIOD, GPIO_Pin_13);

		if (sample < N)
			re[sample++] = Axes_Data.Y;
		else
			FFT_Flag = 1;

		TIM_ClearITPendingBit(TIM3, TIM_IT_Update);
	}
}

int main(void) {
	SystemInit();
	LedOutInit(GPIOD, GPIO_Pin_12, 1);
	LedOutInit(GPIOD, GPIO_Pin_13, 0);
	USART3Init(9600);
	Tim3Init(10000, 60); // 420, 4360 262->64Hz ---> 64 samples ~= 1s
	LisInit();

	while (1) {
		if (FFT_Flag) {
			TIM_ITConfig(TIM3, TIM_IT_Update, DISABLE);

			fft(re, im, N);
			CalcMagnitude(re, im, mag, N);
			maxMagFreq = GetMaxIndex(mag, N);
			if(maxMagFreq == 2 || maxMagFreq == 3)
			HC_SEND_int(maxMagFreq);

			ClearBuffers();
			sample = 0;
			FFT_Flag = 0;
			TIM_ITConfig(TIM3, TIM_IT_Update, ENABLE);
		}

	}
}
