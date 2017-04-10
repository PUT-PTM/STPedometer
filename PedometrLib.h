#include "stm32f4xx_spi.h"
#include "tm_stm32f4_lis302dl_lis3dsh.h"
#include "stm32f4xx_usart.h"
#include "stm32f4xx_exti.h"
#include "misc.h"
#include "stm32f4xx_tim.h"
#include "stm32f4xx_gpio.h"
#include "stm32f4xx_rcc.h"

extern TM_LIS302DL_LIS3DSH_t Axes_Data;
extern TM_LIS302DL_LIS3DSH_Device_t IMU_Type;
extern GPIO_InitTypeDef GPIO_InitStruct;

extern void USART_puts(USART_TypeDef*, volatile char*);
extern void TIM3_IRQHandler(void);
extern void USART3Init(uint32_t BaudRate);
extern void LedOutInit(GPIO_TypeDef*, uint16_t, unsigned char);
extern void Tim3Init(uint32_t, uint16_t);
extern void LisInit(void);
