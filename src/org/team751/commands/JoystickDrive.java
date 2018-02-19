package src.org.team751.commands;

import src.org.team751.Robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 *
 */
public class JoystickDrive extends Command {

	private long lastBrownout;

	public JoystickDrive() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		this.lastBrownout = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.drivetrain.isDrivingAutonomously) {
			return;
		}

		double x = Robot.oi.driverStick.getRawAxis(4);
		double y = Robot.oi.driverStick.getRawAxis(5);

		if (Robot.drivetrain.pdp.getVoltage() < Robot.drivetrain.brownoutVoltageThreshold) {
			// If the time difference between this brownout and the last
			// brownout is too long, reduce the power.
			long currentTime = System.currentTimeMillis();
			if (currentTime - this.lastBrownout > Robot.drivetrain.brownoutPeriodThreshold) {
				Robot.robotDrive.arcadeDrive(-y / 2, x / 2, true);
			}
			this.lastBrownout = currentTime;
		} else {
			Robot.robotDrive.arcadeDrive(-y, x, true);
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
