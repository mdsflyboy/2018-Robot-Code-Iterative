package org.usfirst.frc.team1662.robot;

public class AutoHub {
	public static enum Position{
		left,
		right;
	}
	public static Position ourSwitch = Position.left;//for both of these lines do i need to make it that our switch and scale can be left or right instead of just switch left and scale right always
	public static Position ourScale = Position.right;//i have no recollection of whether or not this was originally set to left or not RIGHT do i need to switch it to left
	
	public static enum Autos{
		CrossLine,//1
		CenterSwitch,//2
		SwitchLeft,//3
		SwitchAroundLeft,//4  Should This be SwitchAroundAuto   switchAroundLeft ScaleAroundLeft and add a comma because it will not be the end of the list if you got this wrong
		ScaleLeft;//5 this should become a semicolon if the line below it is removed (SwitchRight)
		//SwitchRight;//this is part of that high risk right auo code that i put in with the left autos in robot.java
	}
	
	public static Autos cAuto = Autos.CrossLine;
	
	public static class Drive{
		public static enum CrossLine{
			DriveForward,
			Done;
		}
		
		public static CrossLine CL = CrossLine.DriveForward;
		
		public static enum SwitchAuto{//switchAuto
			DriveForward,
			Turn,
			WaitForElevatorUp,
			DriveForward2,
			Turn2,//added
			DriveForward3,//added
			WaitForRelease,
			Done;
		}
		
		public static SwitchAuto SA = SwitchAuto.DriveForward;
		
		public static enum SwitchAroundAuto{
			DriveForward,
			Turn,
			DriveForward2,
			Turn2,
			DriveForward3,
			Turn3,
			WaitForElevatorUp,
			DriveForward4,
			WaitForRelease,
			Done;
		}
		
		public static SwitchAroundAuto SAA = SwitchAroundAuto.DriveForward;//SwitchAroundAuto
	//there was a bracket that looked like this "}" here originally but it did not have the quotations around it
	
		public static enum ScaleLeft{  //I added this in from this line
			DriveForward,
			Turn,
			WaitForElevatorUp,
			DriveForward2,
			WaitForRelease,
			Done;
		}
		
	public static ScaleLeft SL = ScaleLeft.DriveForward;// this is the end  Check line under this for reference I added this in to replace the bracket that looked like this "}" (without the quotes) on line 54
		
	//public static enum SwitchRight{//high risk starts here
		//DriveForward,
		//Turn,
		//WaitForElevatorUp,
		//DriveForward2,
		//WaitForRelease,
		//Done;
		
		//public static SwitchRight SR = SwitchRight.Done;//high risk ends here
	//}
}	
	public static class Gripper{
		public static enum SwitchAuto{//SwitchAuto
			WaitForDrive,
			Lower,
			Release,
			Done;
		}
		public static SwitchAuto SA = SwitchAuto.Done;//SwitchAuto SA = SwitchAuto.Done;
	}
	
	public static class Elevator{
		public static enum SwitchAuto{
			WaitForDriveForward,
			RaiseElevator,
			Done;
		}
		public static SwitchAuto SA = SwitchAuto.WaitForDriveForward;//SwitchAuto SA = SwitchAuto.WaitForDriveForward
	}
}