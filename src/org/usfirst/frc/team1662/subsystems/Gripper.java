package org.usfirst.frc.team1662.subsystems;

import org.usfirst.frc.team1662.robot.AutoHub;
import org.usfirst.frc.team1662.robot.AutoHub.Gripper.SwitchAuto;//SwitchAuto
import org.usfirst.frc.team1662.robot.Constants;
import org.usfirst.frc.team1662.robot.Controls;
import org.usfirst.frc.team1662.util.Toggle;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class Gripper {
	WPI_VictorSPX pivot, rollers;
	DoubleSolenoid claw;
	Toggle closeOpenTog;
	
	Timer stateTimer;
	
	public Gripper() {
		 pivot = new WPI_VictorSPX(Constants.Gripper.pivotNum);
		 rollers = new WPI_VictorSPX(Constants.Gripper.rollersNum);
		 claw = new DoubleSolenoid(Constants.Gripper.clawNums[0], Constants.Gripper.clawNums[1]);
		 
		 closeOpenTog = new Toggle(false);
		 
		 stateTimer = new Timer();
	}
	
	public void TeleopCode() {
//		if(Controls.grabberUp.get())
//			pivot.set(0.7);
//		else if(Controls.grabberDown.get()) 
//			pivot.set(-0.7);
//		else
//			pivot.set(0);
//		
		pivot.set(Controls.joy2.getRawAxis(1));//5
			
		if(Controls.joy1.getRawAxis(3) > 0.05)
			rollers.set(Controls.joy1.getRawAxis(3));
		else
			rollers.set(-Controls.joy1.getRawAxis(2)+1/12);//if it doesnt work take out 1/12
		
		closeOpenTog.toggleVar(Controls.grabCloseOpen.get());
		
		if(closeOpenTog.getToggle())
			claw.set(Constants.Gripper.clawClosed);//clawClosed
		else 
			claw.set(Constants.Gripper.clawOpened);//clawOpened
//		if(Controls.grabOpen.get())
//			claw.set(Constants.Gripper.clawOpened);
	}
	
	public void AutoInit() {
		AutoHub.Gripper.SA = SwitchAuto.WaitForDrive;//AutoHub.Gripper.SA = SwitchAuto.WaitForDrive
	}
	
	public void AutoCode() {
		switch(AutoHub.Gripper.SA) {//SA
		case WaitForDrive:
			claw.set(Constants.Gripper.clawClosed);
			if(AutoHub.Drive.SA == AutoHub.Drive.SwitchAuto.WaitForRelease || AutoHub.Drive.SAA == AutoHub.Drive.SwitchAroundAuto.WaitForRelease || AutoHub.Drive.SL == AutoHub.Drive.ScaleLeft.WaitForRelease) /*|| AutoHub.Drive.SR == AutoHub.Drive.SwitchRight.WaitForRelease)*/ {// the last or statement with sl in it was added by me so feel free to take it out because i have no idea if it is helping us or hurting us. AutoHub.Drive.Sa == AutoHub.Drive.SwitchAuto.WaitForRelease
				AutoHub.Gripper.SA = SwitchAuto.Lower;//SAA SwitchAuto
				stateTimer.reset();
				stateTimer.start();
			}
			break;
		case Lower:
			pivot.set(0.5);//originally was -0.5 but i am switching to 0.5 so that gunnar is comfortable with the controls and our auto can work correctly
			if(stateTimer.get() > 0.75) {
				AutoHub.Gripper.SA = SwitchAuto.Release;//SA SwitchAuto
				stateTimer.reset();
				stateTimer.start();
			}
			break;
		case Release:
			pivot.set(0);
//			rollers.set(-1);
//			if(stateTimer.get() > 0.5) {
				claw.set(Constants.Gripper.clawOpened);
//				rollers.set(0);
				AutoHub.Gripper.SA = SwitchAuto.Done;//SA SwitchAuto
//			}
			break;
		case Done:
			break;
		}
	}
}
