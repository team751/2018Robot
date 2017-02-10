package org.team751.commands;

import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LightToggle extends Command {
	private boolean isOn = false;
	
	@Override
	protected void initialize() {
		super.initialize();
		requires(Robot.cameraLight);
	}
	
	@Override
	protected void execute() {
		super.execute();
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
