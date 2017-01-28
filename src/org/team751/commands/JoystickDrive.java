package org.team751.commands;

import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickDrive extends Command {
	private static final double SENSITIVITY_THRESHOLD = 0.1;
	
    public JoystickDrive() {
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (Robot.drivetrain.isDrivingAutonomously) return;
    	double x = Robot.oi.driverStick.getRawAxis(4);
    	double y = Robot.oi.driverStick.getRawAxis(5);
    	// TODO: Normalize if not already
    	
    	// prevent tiny movements on joystick from causing drive to freak out
    	if(x > JoystickDrive.SENSITIVITY_THRESHOLD || y > JoystickDrive.SENSITIVITY_THRESHOLD){
    	    Robot.drivetrain.setRPMOnJoystick(x, y);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
