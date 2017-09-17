/*
 * Pedometr.h
 *
 *  Created on: 30.05.2017
 *      Author: maks
 */

#ifndef PEDOMETR_H_
#define PEDOMETR_H_
#include "stm32f4xx_conf.h"
#include "stm32f4xx_gpio.h"
#include "stm32f4xx_rcc.h"
#include "stm32f4xx_exti.h"
#include "stm32f4xx_syscfg.h"
#include "stm32f4xx_usart.h"
#include "misc.h"
#include "stm32f4xx_spi.h"
#include "stm32f4xx_tim.h"
#include "tm_stm32f4_lis302dl_lis3dsh.h"

extern TM_LIS302DL_LIS3DSH_t Axes_Data;
extern TM_LIS302DL_LIS3DSH_Device_t IMU_Type;
extern GPIO_InitTypeDef GPIO_InitStruct;
extern uint8_t sample;
extern unsigned char FFT_Flag;
extern volatile double im[64];
extern volatile double re[64];
extern volatile double mag[64];
extern volatile double gauss[64];
extern volatile double result[64];
extern const int N;
extern volatile int maxMagFreq;

extern void USART_puts(USART_TypeDef*, volatile char*);
extern void HC_SEND_double(volatile double);
extern void HC_SEND_int(volatile uint8_t);
extern void ClearBuffers();
extern void TIM3_IRQHandler(void);
extern void USART3Init(uint32_t BaudRate);
extern void LedOutInit(GPIO_TypeDef*, uint16_t, unsigned char);
extern void Tim3Init(uint32_t, uint16_t);
extern void LisInit(void);

#endif /* PEDOMETR_H_ */




/*
			int k = -(64 / 2) + 1;

			for(i=0; i < 64; i++)
				gauss[i] = pdf(k++,32,1);


			USART_puts(USART3,"gauss: [ ");
			for (i = 0; i < 64; i++) {
				char buff[50];
				sprintf(buff, "%5.10f", gauss[i]);
				USART_puts(USART3,buff);
				USART_puts(USART3,"\n\r");
			}
			USART_puts(USART3," ]\n\r");

//			for(i=0; i < N; i++)
//				result[i] =  ((int)(mag[i] * gauss[i]) * 100) / 100;

			actualFreq = GetMaxIndex(result,N);

			USART_puts(USART3,"result: [ ");
						for (i = 0; i < N; i++) {
							char buff[50];
							sprintf(buff, "%2.2f", result[i]);
							USART_puts(USART3,buff);
							USART_puts(USART3,"\n\r");
						}
						USART_puts(USART3," ]\n\r");

*/


