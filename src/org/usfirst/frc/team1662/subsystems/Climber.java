package org.usfirst.frc.team1662.subsystems;

import org.usfirst.frc.team1662.robot.Constants;
import org.usfirst.frc.team1662.robot.Controls;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Climber {
	WPI_VictorSPX MClimb, ClimbUp;
//	DoubleSolenoid climbLock;
	
	public Climber() {
		MClimb = new WPI_VictorSPX(Constants.Climber.MClimbNum);
		ClimbUp = new WPI_VictorSPX(Constants.Climber.CimberUpNum);
		
//		climbLock = new DoubleSolenoid(Constants.Climber.pistonLockNums[0], Constants.Climber.pistonLockNums[1]);
	}
	
	public void TeleopCode() {
//		if(Controls.pistonClosed.get())
//			climbLock.set(Constants.Climber.pistonClosed);
//		
//		if(Controls.pistonOpen.get())
//			climbLock.set(Constants.Climber.pistonOpen);
		
		double volts = 0;
		
		if(Controls.joy2.getRawAxis(3) > 0.25)//0.1
			volts = (Controls.joy2.getRawAxis(3));
		else
			volts = (-Controls.joy2.getRawAxis(2));
		
		volts = volts - 0.05;
	
		MClimb.set(volts);
		
		if(Controls.climbUp.get())
			ClimbUp.set(1);
		else 
			ClimbUp.set(0);
	}
}
