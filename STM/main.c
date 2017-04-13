#include "PedometrLib.h"

int main(void)
{
	SystemInit();
    LedOutInit(GPIOD,GPIO_Pin_12,1);
    LedOutInit(GPIOD,GPIO_Pin_13,0);
    USART3Init(9600);
    Tim3Init(10000,840);
    LisInit();


    cplx t = cexp(-I * PI * 2 / 4);

    while(1)
    {

    }
}
