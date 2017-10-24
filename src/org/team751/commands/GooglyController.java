package org.team751.commands;

import org.team751.Robot;
import org.team751.subsystems.GooglyEyes;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GooglyController extends Command {
	
    public GooglyController() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	final boolean left = Robot.oi.driverStick.getRawButton(3);
    	final boolean right = Robot.oi.driverStick.getRawButton(4);
    	
    	//unfinished
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
