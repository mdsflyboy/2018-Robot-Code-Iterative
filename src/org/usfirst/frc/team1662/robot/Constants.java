package org.usfirst.frc.team1662.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Constants {
	
	public static class Drive{
		public static int MLeftNum = 1,
			MRightNum = 3;
		public static int SLeftNum = 2,
			SRightNum = 4;
		
		public static int[] shiftNums = {0, 1};
		
		public static Value firstGear = Value.kForward,
				secondGear = Value.kReverse;
	}
	
	public static class Elevator{
		public static int MLeftNum = 7,
				SRightNum = 8;
	}
	
	public static class Gripper{
		public static int pivotNum = 10,
				rollersNum = 9;
		public static int[] clawNums = {3,2};
		
		public static Value clawClosed = Value.kReverse,
				clawOpened = Value.kForward;
	}
	
	public static class Climber{
		public static int MClimbNum = 6,
				CimberUpNum = 5;
		public static int[] pistonLockNums = {7,6};
		public static Value pistonClosed = Value.kForward,
				pistonOpen = Value.kReverse;
	}
}
