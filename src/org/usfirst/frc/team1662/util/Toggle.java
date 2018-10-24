package org.usfirst.frc.team1662.util;

public class Toggle {
	boolean toggle = false, pInput = false;
	
	public Toggle(boolean init) {
		toggle = init;
	}
	
	public boolean toggleVar(boolean input) {
		if(input && !pInput) {
			toggle = !toggle;
		}
		pInput = input;
		return toggle;
	}
	
	public boolean getToggle() {
		return toggle;
	}
}
