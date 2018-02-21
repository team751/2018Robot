package src.org.team751.commands;

import src.org.team751.OI;
import src.org.team751.Robot;
import edu.wpi.first.wpilibj.command.Command;

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
		final double triggerRight = Robot.oi.driverStick.getRawAxis(OI.Controller.RT.getButtonMapping()); // RT
		final double triggerLeft = Robot.oi.driverStick.getRawAxis(OI.Controller.LT.getButtonMapping()); // LT

		final boolean bottomToggle = Robot.oi.driverStick.getRawButton(OI.Controller.X.getButtonMapping()); // X
		final boolean topToggle = Robot.oi.driverStick.getRawButton(OI.Controller.B.getButtonMapping()); // B

		final boolean bottomLimit = Robot.oi.bottomWinchLimitSwitch.get();
		final boolean topLimit = Robot.oi.topWinchLimitSwitch.get();

		final double speed = triggerRight - triggerLeft;

		// If going up and we haven't reached the top...
		if (speed > 0.0 && !topLimit) {
			Robot.winch.setSpeed(speed);
		}
		// If going down and we haven't reached the bottom...
		else if (speed < 0.0 && !bottomLimit) {
			Robot.winch.setSpeed(speed);
		/*} else if (!(bottomToggle && topToggle)) {
			if (bottomToggle && !bottomLimit) {
				Robot.winch.toBottom();
			}
			else if (topToggle && !topLimit) {
				Robot.winch.toTop();
			}*/
		} else {
			Robot.winch.stop();
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
