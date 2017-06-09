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

int main(void)
{
	SystemInit();
	LedOutInit(GPIOD,GPIO_Pin_12,1);
    LedOutInit(GPIOD,GPIO_Pin_13,0);
    USART3Init(9600);
    Tim3Init(10000,420);
    LisInit();

    while(1)
	{

	}
}

