/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1662.robot;

import org.usfirst.frc.team1662.robot.AutoHub.Autos;
import org.usfirst.frc.team1662.robot.AutoHub.Position;
import org.usfirst.frc.team1662.subsystems.Climber;
import org.usfirst.frc.team1662.subsystems.Drive;
import org.usfirst.frc.team1662.subsystems.Elevator;
import org.usfirst.frc.team1662.subsystems.Gripper;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * Matthew Stringer
 * Phone 916 206 0556
 * Email mdsflyboy@gmail.com
 */
public class Robot extends IterativeRobot {
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	Drive mDrive;
	Gripper mGripper;
	Elevator mElevator;
	Climber mClimber;

	@Override
	public void robotInit() {
		m_chooser.addDefault("Cross Line", "CrossLine");//CrossLine, CrossLine
		m_chooser.addObject("Center Switch", "CenterSwitch");//Center, Center
		m_chooser.addObject("Left Switch", "LeftSwitch");
		m_chooser.addObject("Switch Around Auto", "SwitchAroundAuto");//Switch Around Auto, SwitchAroundAuto
		m_chooser.addObject("Scale Left", "ScaleLeft");//Left Scale, LeftScale
		//m_chooser.addObject("Right Switch",  "RightSwitch");//this is also part of the high risk right code that i put in with the left autos.
		SmartDashboard.putData("Auto choices", m_chooser);
		
		mDrive = new Drive();
		mGripper = new Gripper();
		mElevator = new Elevator();
		mClimber = new Climber();
		
		int qualX = 320; int qualY = 240; int FPS = 20; UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();//160 120 30
		camera.setFPS(FPS); camera.setResolution(qualX, qualY);//this is the code for the live feed camera
	}
	
	@Override
	public void robotPeriodic() {
		mElevator.Periodic();
	}
	
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		 //autoSelected = SmartDashboard.getString("Auto Selector",
		//defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		
		String data;
		data = DriverStation.getInstance().getGameSpecificMessage();
		if(data.length() > 0) {
			if(data.charAt(0) == 'L')
				AutoHub.ourSwitch = Position.left;
			else
				AutoHub.ourSwitch = Position.right;
			if(data.charAt(1) == 'L')
				AutoHub.ourScale = Position.left;//left
			else
				AutoHub.ourScale = Position.right;//right
		}
		mDrive.AutoInit();
		mElevator.AutoInit();
		mGripper.AutoInit();
	}

	@Override
	public void autonomousPeriodic() {
		mDrive.AutoCode();
		mElevator.AutoCode();
		mGripper.AutoCode();
		switch (m_autoSelected) {
			case "CenterSwitch"://CrossLine CenterSwitch
				AutoHub.cAuto = Autos.CenterSwitch;//CrossLine CenterSwitch
				break;
			case "LeftSwitch":
				if(AutoHub.ourSwitch == Position.left)
					AutoHub.cAuto = Autos.SwitchLeft;//SwitchLeft
				else if(AutoHub.ourScale == Position.left)//left
					AutoHub.cAuto = Autos.ScaleLeft;//ScaleLeft
				else if(AutoHub.ourScale == Position.right)//I added this line in along with the segment on this page that has the lengthy comment attached to it				
					AutoHub.cAuto = Autos.SwitchAroundLeft;//read above line	
				//else if(AutoHub.ourSwitch == Position.right)//this is part of the really risky code that i added in
					//AutoHub.cAuto = Autos.SwitchRight;//This is part of the really risky code that i added in
				else 
					AutoHub.cAuto = Autos.CrossLine;
				break;
			case "LeftScale"://LeftScale
				if(AutoHub.ourScale == Position.left)//left right
					AutoHub.cAuto = Autos.ScaleLeft;//ScaleLeft switcharoundauto
				else if(AutoHub.ourSwitch == Position.left)
					AutoHub.cAuto = Autos.SwitchLeft;//SwitchLeft
				else if(AutoHub.ourScale == Position.right)//I added this line in along with the others on this page left
					AutoHub.cAuto = Autos.SwitchAroundLeft;//read comment above and apply it to this line. changes made to lines 94, 95, 104, 105, 109-118  scaleleft
				//else if(AutoHub.ourSwitch == Position.right)//this is part of the really risky code that i added in
					//AutoHub.cAuto = Autos.SwitchRight;//this is part of the really risky code that I added in
				else 
					AutoHub.cAuto = Autos.CrossLine;
				break;
			case "SwitchAroundAuto"://starting here
				if(AutoHub.ourScale == Position.right)
					AutoHub.cAuto = Autos.SwitchAroundLeft;
				else if(AutoHub.ourSwitch == Position.left)
					AutoHub.cAuto = Autos.SwitchLeft;
				else if(AutoHub.ourScale == Position.left)
					AutoHub.cAuto = Autos.ScaleLeft;
				//else if(AutoHub.ourSwitch == Position.right)
					//AutoHub.cAuto = Autos.SwitchRight;//this was added in
				else
					AutoHub.cAuto = Autos.CrossLine;
				break;//and ending here is something that i added in. if you have problems with the auto after this moment 3/20/16 at 4:16 pm after the switch around auto worked for the first time then delete this
			//case "SwitchRight"://this is the actual start of what i said on line 128
				//if(AutoHub.ourSwitch == Position.right)
					//AutoHub.cAuto = Autos.SwitchRight;
				//else if(AutoHub.ourSwitch == Position.left)//really iffy add on. i am including an auto that starts from the right within the if else statements of ones that start from the left so this very well could be wrong. Ask matthew and if it is incorrect just take it out.
					//AutoHub.cAuto = Autos.SwitchLeft;
				//else if(AutoHub.ourScale == Position.left)
					//AutoHub.cAuto = Autos.ScaleLeft;
				//else if(AutoHub.ourScale == Position.right)
					//AutoHub.cAuto = Autos.SwitchAroundLeft;
				//else
					//AutoHub.cAuto = Autos.CrossLine;//this is the end of my extremely risky add-ons that place a right auto with all of the left autos.
					
				}//you might want to switch these and make scale left in the middle and switch around auto on bottom
	}
	
	@Override
	public void teleopPeriodic() {
		mDrive.TeleopCode();
		mGripper.TeleopCode();
		mElevator.TeleopCode();
		mClimber.TeleopCode();
	}
	
	@Override
	public void testPeriodic() {
	}
}
