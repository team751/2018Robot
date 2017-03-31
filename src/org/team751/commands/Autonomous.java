package org.team751.commands;

import org.team751.CheesyDrive;
import org.team751.Robot;
import org.team751.CheesyDrive.MotorOutputs;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Autonomous extends Command {
	private static final double timeToDrive = 15;
	private static final double leftSpeed = 0.45;
	// 1.216 (0.225/0.185) in C7
	// 0.925 in Bellarmine
	private static final double ratio = 0.95;
	private static final double rightSpeed = -leftSpeed;
	private static double totalCurrent;
	
	// Currentlimit when driving forward is 40 at Bellarmine
	private static double currentLimit = 40;

	Timer timer = new Timer();
	Timer backupTimer = null;

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
		totalCurrent = Robot.drivetrain.pdp.getTotalCurrent();
		double time = timer.get();
		if (Robot.totallyCrushed){
			end();
		} else if (Robot.drivetrain.switch4.get()) {
			driveForward(time);
		} else if (Robot.drivetrain.switch5.get()) {
			centerForward(time);
		} else if (Robot.drivetrain.switch6.get()) {
			leftGoLeft(time);
		} else if (Robot.drivetrain.switch7.get()) {
			rightGoRight(time);
		} else {
			end(); //no auto when all switches are turned off
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
		//timer.get() >= timeToDrive;
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

	// methods for autonomous depending on the starting position
	protected void driveForward(double time) {
		if (time <= 3.75) { //to be adjusted in the field
			if (time > 1 && totalCurrent > currentLimit) {
				Robot.crushed = true;
			}
			Robot.drivetrain.setLeftSpeed(leftSpeed*0.75);
			Robot.drivetrain.setRightSpeed(rightSpeed*0.75);
		}
	}

	// this is the one almost always used
	protected void centerForward(double time) {
		if (time > 1 && totalCurrent > currentLimit) {
			Robot.crushed = true;
			
		}
		Robot.drivetrain.setLeftSpeed(leftSpeed * 0.75);
		Robot.drivetrain.setRightSpeed(rightSpeed * 0.75);
		
		if (Robot.crushed) {
			if(backupTimer == null){
				backupTimer = new Timer();
				backupTimer.start();
			}
			else if(backupTimer.get() > 4){
				Robot.drivetrain.setLeftSpeed(-leftSpeed * 0.75);
				Robot.drivetrain.setRightSpeed(-rightSpeed * 0.75);
			}
			else if(backupTimer.get() > 6){
				Robot.drivetrain.setLeftSpeed(leftSpeed * 0.75);
				Robot.drivetrain.setRightSpeed(rightSpeed * 0.75);
			}
			// should theoretically reach by 7, but to be safe...
			else if(backupTimer.get() > 6.5){
				// begin checking
				if(totalCurrent > currentLimit){
					// stop
					Robot.totallyCrushed = true;
				}
			}
		}
	}

	protected void leftGoLeft(double time) {
		// forward
		if (time <= 1.05) {
			Robot.drivetrain.setLeftSpeed(leftSpeed);
			Robot.drivetrain.setRightSpeed(rightSpeed);
		} else if (time <= 1.33) {
			turnCW();
		} else if (time < 10) { // turn left
			if (time > 2.4 && totalCurrent > currentLimit) {
				Robot.crushed = true;
			}
			Robot.drivetrain.setLeftSpeed(leftSpeed * 0.75);
			Robot.drivetrain.setRightSpeed(rightSpeed * 0.75);
		}
	}

	//Have not calibrated
	protected void rightGoRight(double time) {
		// forward
				if (time <= 1.05) {
					Robot.drivetrain.setLeftSpeed(leftSpeed);
					Robot.drivetrain.setRightSpeed(rightSpeed);
				} else if (time <= 1.33) {
					turnCCW();
				} else if (time < 10) { // turn left
					if (time > 2.4 && totalCurrent > currentLimit) {
						Robot.crushed = true;
					}
					Robot.drivetrain.setLeftSpeed(leftSpeed * 0.75);
					Robot.drivetrain.setRightSpeed(rightSpeed * 0.75);
				}
	}

	// turn clockwise
	protected void turnCW() {
		Robot.drivetrain.setLeftSpeed(0.45);
		Robot.drivetrain.setRightSpeed(0.45);
	}

	// turn counterclockwise
	protected void turnCCW() {
		//if turnCW speed and turn CCW speed are equal they don't really turn the same
		Robot.drivetrain.setLeftSpeed(-0.45);
		Robot.drivetrain.setRightSpeed(-0.45);
	}

}
