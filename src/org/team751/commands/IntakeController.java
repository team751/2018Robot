package src.org.team751.commands;

import edu.wpi.first.wpilibj.command.Command;
import src.org.team751.OI;
import src.org.team751.Robot;

public class IntakeController extends Command {

	private boolean wasIntaking;
	private int count;

	public IntakeController() {
		requires(Robot.intake);
	}

	protected void initalize() {
		this.wasIntaking = false;
		this.count = 0;
	}

	protected void execute() {
		boolean intakeLimit = Robot.oi.intakeLimitSwitch.get(); // True on high

		final boolean out = Robot.oi.driverStick.getRawButton(OI.Controller.RB.getButtonMapping()); // RB
		final boolean in = Robot.oi.driverStick.getRawButton(OI.Controller.LB.getButtonMapping()); // LB
		final boolean reset = Robot.oi.driverStick.getRawButton(OI.Controller.A.getButtonMapping()); // A

		if (!wasIntaking && in) { // If the in button was just pressed.
			this.count = 0; // Reset the count to allow intake correcting.
		}

		// If more than one button is pressed, don't do anything
		if ((out && in) || ((out || in) && reset)) {
			this.wasIntaking = false;
			Robot.intake.stop();
		} else if (out) { // If the out button is pressed, go out
			this.wasIntaking = false;
			Robot.intake.eject();
		} else if (reset) {
			this.wasIntaking = false;
			this.correctIntake();
		}
		// If the in button is pressed, go in while limit switch is not pressed
		else if (in) {
			if (intakeLimit) { // Limit switch pressed
				if (this.count++ == 0) { // Correct the intake once
					this.correctIntake();
				}
			} else {
				this.wasIntaking = true;
				Robot.intake.takeIn();
			}
		}
		else { // If none of the above conditions are met, do not do anything
			Robot.intake.stop();
			this.wasIntaking = false;
		}
	}

	public void correctIntake() {
		Robot.intake.eject();
		try {
			Thread.sleep(75);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Robot.intake.takeIn();
		try {
			Thread.sleep(75);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Robot.intake.stop();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}
}
