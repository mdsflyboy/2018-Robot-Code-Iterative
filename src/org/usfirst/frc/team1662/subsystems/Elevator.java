package org.usfirst.frc.team1662.subsystems;

import javax.swing.Scrollable;

import org.usfirst.frc.team1662.robot.AutoHub;
import org.usfirst.frc.team1662.robot.Constants;
import org.usfirst.frc.team1662.robot.Controls;
import org.usfirst.frc.team1662.robot.AutoHub.Autos;
import org.usfirst.frc.team1662.robot.AutoHub.Elevator.SwitchAuto;//org.usfirst.frc.team1662.robot.AutoHub.Elevator.SwitchAuto

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;
//since the line above is not used is it really necessary
public class Elevator {
	WPI_VictorSPX MLeft, SRight;
	
	Timer stateTimer;
	
	public Elevator() {
		MLeft = new WPI_VictorSPX(Constants.Elevator.MLeftNum);
		SRight = new WPI_VictorSPX(Constants.Elevator.SRightNum);
		//DirectInput limit = new DirectInput (port); feel free to take this out if it doesnt work
		stateTimer = new Timer();
	}
	
	public void Periodic() {
		SRight.set(MLeft.get());
	}
	
	public void TeleopCode() {//the six lines following this line are correct and should be uncommented and the line below the sixth (ie the seventh) should be taken out if you want to restore the default
		//if(Controls.elevatorUp.get())
			//MLeft.set(1);
		//else if(Controls.elevatorDown.get())
			//MLeft.set(-0.6);
		//else
		//MLeft.set(0.06);
		MLeft.set(-Controls.joy2.getRawAxis(5)+ 0.06); //(Controls.joy2.getRawAxis5 + 0.06);
	}
	
	public void AutoInit() {
		AutoHub.Elevator.SA = SwitchAuto.WaitForDriveForward; //AutoHub.Elevator.Sa = SwitchAuto.WaitDriveForward
}

	public void AutoCode() {
		switch(AutoHub.Elevator.SA) {//SA
		case WaitForDriveForward:
			if(AutoHub.Drive.SA == AutoHub.Drive.SwitchAuto.WaitForElevatorUp || AutoHub.Drive.SAA == AutoHub.Drive.SwitchAroundAuto.WaitForElevatorUp || AutoHub.Drive.SL == AutoHub.Drive.ScaleLeft.WaitForElevatorUp) /* || AutoHub.Drive.SR == AutoHub.Drive.SwitchRight.WaitForElevatorUp)*/ {// I added in the last segmenmt with the sl in it and i have no idea whether or not it is helping us or hurting us. Matthew originally had this as if(AutoHub.Drive.SA == AutoHub.Drive.SwitchAuto.WaitForElevatorUp) {   so if there are any problems with this part of the code that might be why
				stateTimer.reset();
				stateTimer.start();
				AutoHub.Elevator.SA = SwitchAuto.RaiseElevator;//AutoHub.Elevator.Sa = SwitchAuto.RaiseElevator
			}
			break;
		case RaiseElevator:
			MLeft.set(0.7);
			if(stateTimer.get() > 1.75 && (AutoHub.cAuto == Autos.ScaleLeft || AutoHub.cAuto == Autos.SwitchAroundLeft))//   1 TO 1.75   If this whole code thing goes wrong, try reverting it back to the original but keep all of the ScaleLeft statements as SwitchAroundLeft
				AutoHub.Elevator.SA = SwitchAuto.Done;//AutoHub.Elevator.Sa = SwitchAuto.Done; are both the line above and below supposed to be ScaleLeft
			else if(stateTimer.get() > 0.7 && (AutoHub.cAuto != Autos.ScaleLeft || AutoHub.cAuto == Autos.SwitchAroundLeft))//ScaleLeft 0.5
				AutoHub.Elevator.SA = SwitchAuto.Done;//AutoHub.Elevator.SA = SwitchAuto.Done;
			break;
		case Done:
			MLeft.set(0.1);
			break;
		}
	}
}
