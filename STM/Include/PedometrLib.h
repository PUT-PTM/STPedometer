/*
 * Pedometr.h
 *
 *  Created on: 30.05.2017
 *      Author: maks
 */

#ifndef PEDOMETR_H_
#define PEDOMETR_H_

#include "stm32f4xx.h"
#include "stm32f4_discovery.h"
#include "tm_stm32f4_lis302dl_lis3dsh.h"
#include "arm_math.h"

#define SAMPLES	128
#define FFT_SIZE	SAMPLES / 2


extern TM_LIS302DL_LIS3DSH_t Axes_Data;
extern TM_LIS302DL_LIS3DSH_Device_t IMU_Type;
extern GPIO_InitTypeDef GPIO_InitStruct;
extern float32_t FFT_InputSamples[SAMPLES];
extern float32_t FFT_OutputSamples[FFT_SIZE];
extern arm_cfft_radix4_instance_f32 S;

extern float32_t maxValue;
extern uint32_t maxIndex;
extern uint16_t n;

extern void USART_puts(USART_TypeDef*, volatile char*);
extern void TIM3_IRQHandler(void);
extern void USART3Init(uint32_t BaudRate);
extern void LedOutInit(GPIO_TypeDef*, uint16_t, unsigned char);
extern void Tim3Init(uint32_t, uint16_t);
extern void LisInit(void);

#endif /* PEDOMETR_H_ */
