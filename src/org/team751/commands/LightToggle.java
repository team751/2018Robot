package org.team751.commands;

import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LightToggle extends Command {
	private boolean isOn = false;
	
	@Override
	protected void initialize() {
		requires(Robot.cameraLight);
		super.initialize();
	}
	
	@Override
	protected void execute() {
		super.execute();
		System.out.println("Light is now " + !isOn);
		if(isOn){
			Robot.cameraLight.off();
			isOn = false;
		}
		else{
			Robot.cameraLight.on();
			isOn = true;
		}
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
