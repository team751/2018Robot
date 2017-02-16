package org.team751.commands;

import org.team751.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Autonomous extends Command {
	private static double timeToDrive = 3;
	private static double speed = 0.5;
	
	Timer timer = new Timer();
	
    public Autonomous() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	if(timer.get() < 3){
//    		Robot.drivetrain.setLeftSpeed(-speed);
//    		Robot.drivetrain.setRightSpeed(speed);
//    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() >= timeToDrive;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.setLeftSpeed(0);
    	Robot.drivetrain.setRightSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
