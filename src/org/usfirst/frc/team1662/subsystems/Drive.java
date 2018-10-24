package org.usfirst.frc.team1662.subsystems;

import org.usfirst.frc.team1662.robot.AutoHub;
import org.usfirst.frc.team1662.robot.Constants;
import org.usfirst.frc.team1662.robot.Controls;
import org.usfirst.frc.team1662.util.Toggle;
import org.usfirst.frc.team1662.robot.AutoHub.Drive.CrossLine;
import org.usfirst.frc.team1662.robot.AutoHub.Drive.ScaleLeft;
import org.usfirst.frc.team1662.robot.AutoHub.Drive.SwitchAroundAuto;
import org.usfirst.frc.team1662.robot.AutoHub.Drive.SwitchAuto;
//import org.usfirst.frc.team1662.robot.AutoHub.Drive.SwitchRight;
import org.usfirst.frc.team1662.robot.AutoHub.Position;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;//do i need this since it is never used
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive {
	WPI_VictorSPX MLeft, MRight, SLeft, SRight;

	DoubleSolenoid shift;

	DifferentialDrive drive;

	Toggle shifterToggle;

	Timer stateTimer;

	public Drive() {
		MLeft = new WPI_VictorSPX(Constants.Drive.MLeftNum);
		MRight = new WPI_VictorSPX(Constants.Drive.MRightNum);
		SLeft = new WPI_VictorSPX(Constants.Drive.SLeftNum);
		SRight = new WPI_VictorSPX(Constants.Drive.SRightNum);
		SLeft.set(ControlMode.Follower, MLeft.getDeviceID());
		SRight.set(ControlMode.Follower, MRight.getDeviceID());

		shift = new DoubleSolenoid(Constants.Drive.shiftNums[0], Constants.Drive.shiftNums[1]);

		drive = new DifferentialDrive(MLeft, MRight);

		shifterToggle = new Toggle(false);

		stateTimer = new Timer();
	}

	public void TeleopCode() {
		drive.tankDrive(Controls.joy1.getRawAxis(1), Controls.joy1.getRawAxis(5));

		shifterToggle.toggleVar(Controls.shiftUpDown.get());
		if (shifterToggle.getToggle())
			shift.set(Constants.Drive.secondGear);
		else
			shift.set(Constants.Drive.firstGear);

		// if(Controls.shiftUp.get())
		// shift.set(Constants.Drive.secondGear);
		// if(Controls.shiftDown.get())
		// shift.set(Constants.Drive.firstGear);
	}

	public void AutoInit() {
		shift.set(Constants.Drive.secondGear);
		AutoHub.Drive.SA = SwitchAuto.DriveForward;//1      3
		AutoHub.Drive.SAA = SwitchAroundAuto.DriveForward;//2     2  switchAroundAuto
		AutoHub.Drive.CL = CrossLine.DriveForward;//3    1
		AutoHub.Drive.SL = ScaleLeft.DriveForward;//  4     4   I added this in right after we tested all autos ,excluding center switch, to be successful
		//AutoHub.Drive.SR = SwitchRight.DriveForward;//high risk line
		stateTimer.reset();
		stateTimer.start();
	}

	public void AutoCode() {
		switch(AutoHub.cAuto) {
		case CrossLine:
			switch(AutoHub.Drive.CL) {
			case DriveForward:
				drive.arcadeDrive(-1.0, -0);//x=-0.9    y=0 these were the original values
				if(stateTimer.get() > 1.5)//1
					AutoHub.Drive.CL = CrossLine.Done;
				break;
			case Done:
				drive.arcadeDrive(0,  0);
				break;
			}
			break;
		case SwitchAroundLeft://Does This Need To Be SwitchArounAuto
			switch(AutoHub.Drive.SAA) {
			case DriveForward:
				drive.arcadeDrive(-0.85, 0);
				if(stateTimer.get() > 3) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SAA = SwitchAroundAuto.Turn;
					stateTimer.start();
				}
				break;
			case Turn:
				drive.arcadeDrive(0, -0.85);//-0.91   -0.9
				if(stateTimer.get() > 0.5) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SAA = SwitchAroundAuto.DriveForward2;
					stateTimer.start();
				}
				break;
			case DriveForward2:
				drive.arcadeDrive(-0.9, 0);
				if(stateTimer.get() > 3.25) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SAA = SwitchAroundAuto.Turn2;
					stateTimer.start();
				}
				break;
			case Turn2:
				drive.arcadeDrive(0, 1.0);//0.9
				if(stateTimer.get() > 0.40) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SAA = SwitchAroundAuto.DriveForward3;
					stateTimer.start();
				}
				break;
			case DriveForward3:
				drive.arcadeDrive(-0.9, 0);
				if(stateTimer.get() > 1) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SAA = SwitchAroundAuto.Turn3;
					stateTimer.start();
				}
				break;
			case Turn3:
				drive.arcadeDrive(0, 0.9);
				if(stateTimer.get() > 0.37) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SAA = SwitchAroundAuto.WaitForElevatorUp;
					stateTimer.start();
				}
				break;
			case WaitForElevatorUp:
				if(AutoHub.Elevator.SA == AutoHub.Elevator.SwitchAuto.Done) { //if something with the elevator goes wrong in this code, this line is most likely why becasue im not sure if it referring to the right thing but i didnt know how to make it refer to anything else and matthew said it was set up but he might have not meant set up for this specifically or he might have misunderstood me.
					AutoHub.Drive.SAA = SwitchAroundAuto.DriveForward4;
					stateTimer.start();
				}
				break;
			case DriveForward4:
				drive.arcadeDrive(-0.1, 0);
				if(stateTimer.get() > 0.5) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SAA = SwitchAroundAuto.WaitForRelease;
					stateTimer.start();
				}
				break;
			case WaitForRelease:
				if(AutoHub.Gripper.SA == AutoHub.Gripper.SwitchAuto.Done)//same thing that went for the elevator part of this code segment goes for this as well.
				AutoHub.Drive.SAA = SwitchAroundAuto.Done;
				break;
			case Done:
				break;
			}
			break;
		/*case SwitchRight://this is the beginning of the super risky right auto in the left auto code.
			switch(AutoHub.Drive.SR) {
			case DriveForward:
				drive.arcadeDrive(-0.85, 0);
				if(stateTimer.get() > 2) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SR = SwitchAuto.Turn;
					stateTimer.start();
				}
				break;
			case Turn:
				drive.arcadeDrive(0, 0.7);
				if(stateTimer.get() > 0.5) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SR = SwitchAuto.WaitForElevatorUp;
					stateTimer.start();
				}
				break;
			case WaitForElevatorUp:
				if(AutoHub.Elevator.SA == AutoHub.Elevator.SwitchAuto.Done) {
					AutoHub.Drive.SR = SwitchAuto.DriveForward2;
					stateTimer.start();
				}
				break;
			case DriveForward2:
				drive.arcadeDrive(-0.85, 0);
				if(stateTimer.get() > 0.5) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SR = SwitchAuto.WaitForRelease;//why does t always auto correct or auto finish sa to SwitchAroundAuto!!!!
				}
				break;
			case WaitForRelease:
				if(AutoHub.Gripper.SA == AutoHub.Gripper.SwitchAuto.Done)
					AutoHub.Drive.SR = SwitchAuto.Done;
				break;
			case Done:
				break;
			}
			break;//this is the end of the risky code that mixes right and left auto potentially.*/
		case SwitchLeft:
			switch(AutoHub.Drive.SA) {
			case DriveForward:
				drive.arcadeDrive(-0.9, 0);//(power output forward, power output turning)  -0.85, 0
				if(stateTimer.get() > 1.75) {//duration of previous declaration
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SA = SwitchAuto.Turn;
					stateTimer.start();
				}
				break;
			case Turn:
				drive.arcadeDrive(0, -0.8);//-0.7
				if(stateTimer.get() > 0.5) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SA = SwitchAuto.WaitForElevatorUp;//why didnt we do a state timer start on this one but we did it on all of the other ones yet this is the one that works the best and most consistently besides the cross line auto.
				}
				break;
			case WaitForElevatorUp:
				if(AutoHub.Elevator.SA == AutoHub.Elevator.SwitchAuto.Done) {
					AutoHub.Drive.SA = SwitchAuto.DriveForward2;
					stateTimer.reset();
					stateTimer.start();//does this auto always work because it has a state timer set and reset in it.!!!!!!!!!
				}
				break;
			case DriveForward2:
				drive.arcadeDrive(-0.89, 0);//-0.75
				if(stateTimer.get() > 0.75) {//0.5
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SA = SwitchAuto.Turn2;//This was originally WaitForRelease Turn2
				}
				break;
			case Turn2://I added this																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
				if(AutoHub.ourSwitch == Position.right)
					drive.arcadeDrive(0, 0);
				else
					drive.arcadeDrive(0, 0);//1
				if((stateTimer.get() > 0 && AutoHub.ourSwitch == Position.left) || (stateTimer.get() > 0 && AutoHub.ourSwitch == Position.right))//position right was 0.5 seconds
					AutoHub.Drive.SA = SwitchAuto.DriveForward3;
				break;
			case DriveForward3:
				if(AutoHub.ourSwitch== Position.right)
					drive.arcadeDrive(0, 0);
				else
					drive.arcadeDrive(0, 0);
				if((stateTimer.get() > 0 && AutoHub.ourSwitch == Position.left) || (stateTimer.get() > 0 && AutoHub.ourSwitch == Position.right))
					AutoHub.Drive.SA = SwitchAuto.WaitForRelease;
				break;//below this line is where i stopped adding things
			case WaitForRelease:
				if(AutoHub.Gripper.SA == AutoHub.Gripper.SwitchAuto.Done)
					AutoHub.Drive.SA = SwitchAuto.Done;
				break;
			case Done:
				break;
			}
			break;
		case ScaleLeft:
			switch(AutoHub.Drive.SL) {//The part i added in starts here
			case DriveForward:
				drive.arcadeDrive(-0.85, 0);
				if(stateTimer.get() > 4.2) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SL = ScaleLeft.Turn;
					stateTimer.start();
				}
				break;
			case Turn:
				drive.arcadeDrive(0, -0.9);//-0.9
				if(stateTimer.get() > 0.5) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SL = ScaleLeft.WaitForElevatorUp;
					stateTimer.start();
				}
				break;
			case WaitForElevatorUp:
				if(AutoHub.Elevator.SA == AutoHub.Elevator.SwitchAuto.Done) {//why are these giving me errors
					AutoHub.Drive.SL = ScaleLeft.DriveForward2;
					stateTimer.start();
				}
				break;
			case DriveForward2:
				drive.arcadeDrive(-0.1, 0);
				if(stateTimer.get() > 0.5) {
					drive.arcadeDrive(0, 0);
					AutoHub.Drive.SL = ScaleLeft.WaitForRelease;
					stateTimer.start();
				}
				break;
			case WaitForRelease:
				if(AutoHub.Gripper.SA == AutoHub.Gripper.SwitchAuto.Done)
					AutoHub.Drive.SL = ScaleLeft.Done;
				break;
			case Done:
				break;
			}
			break;//This is the end of my added piece
		case CenterSwitch://I believe this is wrong
			switch(AutoHub.Drive.SA) {
			case DriveForward:
				drive.arcadeDrive(-0.7, 0);//i believe that the -1 was a -0.7 but we can deal with that later																																																					
				if(stateTimer.get() > 0.75) {
					//drive.arcadeDrive(0, 0); // even though im keeping it here this value did not affect anything, driveforward 2 was still running on an infinite loopi just added this in because i wanted to see if this would help stop the infinite loop for some reason
					AutoHub.Drive.SA = SwitchAuto.Turn;
					stateTimer.reset();
					stateTimer.start();
				}
				break;
			case Turn:																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
				if(AutoHub.ourSwitch == Position.right)
					drive.arcadeDrive(0, -0.5);
				else
					drive.arcadeDrive(0, 0.5);//1
				if((stateTimer.get() > 1.10 && AutoHub.ourSwitch == Position.left) || (stateTimer.get() > 1.1 && AutoHub.ourSwitch == Position.right))//position right was 0.5 seconds
					AutoHub.Drive.SA = SwitchAuto.WaitForElevatorUp;
				break;
			case WaitForElevatorUp:

				if(AutoHub.Elevator.SA == AutoHub.Elevator.SwitchAuto.Done) {
					AutoHub.Drive.SA = SwitchAuto.DriveForward2;
					stateTimer.reset();
					stateTimer.start();
				}
				break;
			case DriveForward2:
				//if(stateTimer.get() < 0.25) //drive.arcadeDrive(0,0); if(statetimer.get() < 0) drive.arcadeDrive(0, 0) originally was 0.25 this amount of time does not affect the loop
				//drive.arcadeDrive(-1.0, 0);//it keeps running this line over and over and over again.
					//else//it doesnt seem to make a difference whether or not this line and the two above it are included or commented out. No matter what it still runs the driveforward2 state correctly for the correct side and then skips to wait for release
				 if(AutoHub.ourSwitch == Position.right)//left
						drive.arcadeDrive(-0.75, 0.40);//-0.9 -0.5    
				 else
						drive.arcadeDrive(-0.75, -0.5);//-1.0,0 0.5
				if((stateTimer.get() > 2.75 && AutoHub.ourSwitch == Position.left) || (stateTimer.get() > 2.1 && AutoHub.ourSwitch == Position.right))//0.75 25
					AutoHub.Drive.SA = SwitchAuto.Turn2;//WaitForRelease
					//stateTimer.reset(); 
					//stateTimer.start(); //if you ever change drive forward 2, just copy the conditional turn state and you will be good.
				break;
			case Turn2://I added this																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
				if(AutoHub.ourSwitch == Position.right)//right
					drive.arcadeDrive(0, -0.8);
				else
					drive.arcadeDrive(0, -0.8);//1
				if((stateTimer.get() > 0.5 && AutoHub.ourSwitch == Position.left) || (stateTimer.get() > 0.75 && AutoHub.ourSwitch == Position.right))//position right was 0.5 seconds
					AutoHub.Drive.SA = SwitchAuto.DriveForward3;
				break;
			case DriveForward3:
				if(AutoHub.ourSwitch== Position.right)
					drive.arcadeDrive(0, 0);
				else
					drive.arcadeDrive(-0.25, 0);
				if((stateTimer.get() > 0.25 && AutoHub.ourSwitch == Position.left) || (stateTimer.get() > 0 && AutoHub.ourSwitch == Position.right))
					AutoHub.Drive.SA = SwitchAuto.WaitForRelease;
				break;//below this line was where i stopped adding
			case WaitForRelease:
				if(AutoHub.Gripper.SA == AutoHub.Gripper.SwitchAuto.Done)
					AutoHub.Drive.SA = SwitchAuto.Done;
				break;
			case Done:
				break;
			}
			break;
		}
	}
}
