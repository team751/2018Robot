package org.team751.commands;

import org.team751.CheesyDrive;
import org.team751.CheesyDrive.MotorOutputs;
import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickDrive extends Command {
	
	private static final double SENSITIVITY = 0.15;
	CheesyDrive cheesyDrive = new CheesyDrive();
	
    public JoystickDrive() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double x = -Robot.oi.driverStick.getRawAxis(4);
    	double y = Robot.oi.driverStick.getRawAxis(5);
    	boolean quickTurn = true;
       
    	if (Math.abs(x) < SENSITIVITY){x = 0;}
    	if (Math.abs(y) < SENSITIVITY){y = 0;}
    	//System.out.println("x: " + x + ", y: " + y);

    	MotorOutputs output = cheesyDrive.cheesyDrive(y, x, quickTurn);
<<<<<<< HEAD
    	System.out.println("Before left: " + -output.left + ", right: " + output.right);
=======
    	//System.out.println("Before left: " + -output.left + ", right: " + output.right);
>>>>>>> b211dd129c62d33b192db035c12da7f5b7e5f334
    	double left = -output.left;
    	double right = output.right;
    	// apply linear bump
    	final double MAX_NATURAL = 0.69;
    	left /= MAX_NATURAL;
    	right /= MAX_NATURAL;
    	
    	boolean slowButton = Robot.oi.driverStick.getRawButton(7); // y button
    	if (slowButton){
    		System.out.println("bumper pressed");
    		left /= 2.0;
    		right /= 2.0;
    	}
    	System.out.println("After left: " + left + "; Right: " + right);
    	Robot.drivetrain.setLeftSpeed(left);
    	Robot.drivetrain.setRightSpeed(right);
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
