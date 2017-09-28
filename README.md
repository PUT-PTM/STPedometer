# STPedometer
<B>Overview:</B> Pedometer made on STM32F4Discovery. Amount of steps made are displayed on android based phones. <br />
<B>Description:</B> Data collected from accelerometer are computed by FFT.
	Results are crucial for further calculations to define if step was made.
	Every time step is made board sends a signal by bluetooth to the phone with android where app counts every step that has been made. 
	To communicate with phone we used bluetooth module HC-06.  <br />
<B>Tools:</B> CooCox Version: 1.7.0, Android Studio Version 2.3.3 <br />
<B>How to run:</B> To make it work it is enough to connect bluetooth module to STM32 and then the board to the power supply.
	To count steps it is necessary to have an android mobile phone with installed app.
####
| Pin | Destination |
|:---|:---|
| ```GPIOD_12``` | Test Led |
| ```GPIOD_13``` | Test Led |
| ```GPIOC_10``` | USART3 TX |
| ```GPIOC_11``` | USART3 RX |
####
<B>How to compile:</B> Make a project for STM32F407VGDiscovery, copy and paste code, add to the project all the necessary libraries and everything should work fine.
<B>Credits:</B> Maksymilian Meller, Jakub Kaszczyński <br /><br />
The project was conducted during the Microprocessor Lab course held by the Institute of Control and Information Engineering, Poznan University of Technology.
Supervisor: Marek Kraft/Michał Fularz/Tomasz Mańkowski/Adam Bondyra

