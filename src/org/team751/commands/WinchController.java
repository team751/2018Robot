package org.team751.commands;

import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WinchController extends Command {

    public WinchController() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.winch);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	final boolean up = Robot.oi.driverStick.getRawButton(1);
//    	final boolean down = Robot.oi.driverStick.getRawButton(2);
    	
    	if(up){
    		Robot.winch.forward();
		}
//    	else if(down){
//    		Robot.winch.reverse();
//    	}
    	else if(!up){
    		Robot.winch.disable();
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
