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

extern void USART_puts(USART_TypeDef*, volatile char*);
extern void TIM3_IRQHandler(void);
extern void USART3Init(uint32_t BaudRate);
extern void LedOutInit(GPIO_TypeDef*, uint16_t, unsigned char);
extern void Tim3Init(uint32_t, uint16_t);
extern void LisInit(void);

#endif /* PEDOMETR_H_ */
