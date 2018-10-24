package org.usfirst.frc.team1662.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Controls {
	public static Joystick joy1 = new Joystick(0);
	public static Joystick joy2 = new Joystick(1);
	
//	public static JoystickButton shiftUp = new JoystickButton(joy1, 1); //A
//	public static JoystickButton shiftDown = new JoystickButton(joy1, 2); //B
	
	public static JoystickButton shiftUpDown = new JoystickButton(joy1, 5); //Left bumper
	
//	public static JoystickButton grabberUp = new JoystickButton(joy2, 4); //Y
//	public static JoystickButton grabberDown = new JoystickButton(joy2, 3); //X
		
//	public static JoystickButton grabClosed = new JoystickButton(joy1, 6); //Right bumper
//	public static JoystickButton grabOpen = new JoystickButton(joy1, 5); //Left bumper
	
	public static JoystickButton grabCloseOpen = new JoystickButton(joy1, 6); //Right bumper
	
	//public static JoystickButton elevatorUp = new JoystickButton(joy2, 1);//A  this should be uncommented if we go back to buttons instead of using the stick
	//public static JoystickButton elevatorDown = new JoystickButton(joy2, 2);//B this should be uncommented if we go back to buttons instead of using the stick
	
	public static JoystickButton climbUp = new JoystickButton(joy2, 6); //Right bumper
	
//	public static JoystickButton pistonClosed = new JoystickButton(joy2, 7); //Right bumper
//	public static JoystickButton pistonOpen = new JoystickButton(joy2, 8); //Left bumper
	//Climbing controlled by joy2 triggers
}
