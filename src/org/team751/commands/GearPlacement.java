package org.team751.commands;

import org.team751.CheesyDrive;
import org.team751.Robot;
import org.team751.CheesyDrive.MotorOutputs;

import edu.wpi.first.wpilibj.command.Command;

public class GearPlacement extends Command {
	CheesyDrive cheesyDrive = new CheesyDrive();
	
	@Override
	protected void execute() {
		super.execute();		
		
	}
	
	@Override
	protected void initialize() {
		double x = Robot.autonomousJoystickSimulator.getX(); // get network input
    	double y = Robot.autonomousJoystickSimulator.getY(); // get network input
    	boolean quickTurn = false; // idk what quickturn is
    	
    	MotorOutputs output = cheesyDrive.cheesyDrive(-y, x, quickTurn);
    	
    	Robot.drivetrain.setLeftSpeed(-output.left);
    	Robot.drivetrain.setRightSpeed(output.right);
		
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
