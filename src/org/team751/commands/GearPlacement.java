package org.team751.commands;

import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class GearPlacement extends Command {
	
	@Override
	protected void initialize() {
		    			
	}
	
	@Override
	protected void execute() {
		super.execute();		
		double x = Robot.autonomousJoystickSimulator.getX(); // get network input
		double y = Robot.autonomousJoystickSimulator.getY(); // get network input
		Robot.drivetrain.setRPMOnJoystick(x, y);
	}
	
	@Override
	protected void end() {
		super.end();
	}

	@Override
	protected void interrupted() {
		super.interrupted();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
