package org.team751.commands;

import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeController extends Command {

	boolean intakeOn = false;
	boolean outtakeOn = false;
	
    public IntakeController() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (Robot.drivetrain.isDrivingAutonomously) return;
    	
    	if (Robot.oi.driverStick.getRawAxis(3) >= .5) {
    		if (intakeOn) {
    			Robot.intake.disable();
    			intakeOn = false;
    		} else {
    			Robot.intake.intake();
    			intakeOn = true;
    		}
    		while (Robot.oi.driverStick.getRawAxis(3) >= .5) {}
    	}
    	
    	if (Robot.oi.driverStick.getRawAxis(2) >= .5) {
    		if (outtakeOn) {
    			Robot.intake.disable();
    			outtakeOn = false;
    		} else {
    			Robot.intake.outtake();
    			outtakeOn = true;
    		}
    		while (Robot.oi.driverStick.getRawAxis(2) >= .5) {}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
