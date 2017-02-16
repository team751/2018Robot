package org.team751.commands;

import org.team751.CheesyDrive;
import org.team751.CheesyDrive.MotorOutputs;
import org.team751.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickDrive extends Command {
	private static final double SENSITIVITY_THRESHOLD = 0.1;
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
    	if (Robot.drivetrain.isDrivingAutonomously) return;
    	double x = -Robot.oi.driverStick.getRawAxis(4) * .5;
    	double y = Robot.oi.driverStick.getRawAxis(5);
    	boolean quickTurn = true;
    	
    	System.out.println("x: " + x + ", y: " + y);
    	
    	// prevent tiny movements on joystick from causing drive to freak out
    	//if(x > JoystickDrive.SENSITIVITY_THRESHOLD || y > JoystickDrive.SENSITIVITY_THRESHOLD || quickTurn){
    	
    		MotorOutputs output = cheesyDrive.cheesyDrive(-y, x, quickTurn);
    	System.out.println("left: " + -output.left + ", right: " + output.right);
    		Robot.drivetrain.setLeftSpeed(-output.left);
    		Robot.drivetrain.setRightSpeed(output.right);
    	//}
    	//else{
    		//Robot.drivetrain.setLeftSpeed(0);
    		//Robot.drivetrain.setRightSpeed(0);
    	//}
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
