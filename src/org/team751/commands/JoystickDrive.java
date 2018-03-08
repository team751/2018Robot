package src.org.team751.commands;

import src.org.team751.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class JoystickDrive extends Command {

	private long lastBrownout;

	// The power reduction factor applied when brownout is detected.
	// Ex. a reduction factor of 2.0 will cut the x and y in half.
	private final double brownoutReductionFactor = 2.0;

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

//		if (Robot.drivetrain.pdp.getVoltage() < Robot.drivetrain.brownoutVoltageThreshold) {
			// If the time difference between this brownout and the last
			// brownout is too long, reduce the power.
//			long currentTime = System.currentTimeMillis();
//			if (currentTime - this.lastBrownout > Robot.drivetrain.brownoutPeriodThreshold) {
//				Robot.robotDrive.arcadeDrive(y / this.brownoutReductionFactor, x / this.brownoutReductionFactor, true);
//			}
//			this.lastBrownout = currentTime;
//		} else {
			Robot.robotDrive.arcadeDrive(y, x, true);
//		}
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
