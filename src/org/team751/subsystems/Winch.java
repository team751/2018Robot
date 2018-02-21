package src.org.team751.subsystems;

import src.org.team751.OI;
import src.org.team751.Robot;
import src.org.team751.commands.WinchController;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Winch extends Subsystem {
	public PWMVictorSPX winchMotorController1 = new PWMVictorSPX(6);
	public PWMVictorSPX winchMotorController2 = new PWMVictorSPX(7);

	public double toggleSpeed = 0.2;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new WinchController());
	}

	public void setSpeed(double speed) {
		winchMotorController1.set(speed);
		winchMotorController2.set(-speed);// reverse direction of second motor
											// so they spin together
	}

	public void goUpAuto() {
		setSpeed(0.5);
		while (!Robot.oi.topWinchLimitSwitch.get()) {
		}
		stop();
	}

	// toTop() and toBottom() send the lift to the desired location and are
	// stopped on any trigger input
	public void toTop() {
		setSpeed(toggleSpeed);
		while (!Robot.oi.topWinchLimitSwitch.get()) {
			final double triggerRight = Robot.oi.driverStick.getRawAxis(OI.Controller.RT.getButtonMapping());
			final double triggerLeft = Robot.oi.driverStick.getRawAxis(OI.Controller.LT.getButtonMapping());
			final double speed = triggerRight - triggerLeft;
			if (speed != 0) {
				break;
			}
		}
		stop();
	}

	public void toBottom() {
		setSpeed(-toggleSpeed);
		while (!Robot.oi.bottomWinchLimitSwitch.get()) {
			final double triggerRight = Robot.oi.driverStick.getRawAxis(OI.Controller.RT.getButtonMapping());
			final double triggerLeft = Robot.oi.driverStick.getRawAxis(OI.Controller.LT.getButtonMapping());
			final double speed = triggerRight - triggerLeft;
			if (speed != 0) {
				break;
			}
		}
		stop();
	}

	public void stop() {
		winchMotorController1.setSpeed(0.0);
		winchMotorController2.setSpeed(0.0);
	}
}
