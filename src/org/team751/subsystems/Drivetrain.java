package src.org.team751.subsystems;

import src.org.team751.MultiSpeedController;
import src.org.team751.commands.JoystickDrive;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Drivetrain extends Subsystem {
	public PWMVictorSPX leftDriveController1;
	public PWMVictorSPX leftDriveController2;
	public PWMVictorSPX leftDriveController3;

	public PWMVictorSPX rightDriveController1;
	public PWMVictorSPX rightDriveController2;
	public PWMVictorSPX rightDriveController3;

	public MultiSpeedController leftSpeedController;
	public MultiSpeedController rightSpeedController;

	public final int leftMotor1 = 1;
	public final int leftMotor2 = 3;
	public final int leftMotor3 = 5;

	public final int rightMotor1 = 0;
	public final int rightMotor2 = 2;
	public final int rightMotor3 = 4;

	// The minimum allowed voltage for the PDP.
	// Any voltage below this point will be considered a brownout.
	public final double brownoutVoltageThreshold = 8.0;

	public PowerDistributionPanel pdp = new PowerDistributionPanel();

	public boolean isDrivingAutonomously;

	public Drivetrain() {
		leftDriveController1 = new PWMVictorSPX(this.leftMotor1);
		leftDriveController2 = new PWMVictorSPX(this.leftMotor2);
		leftDriveController3 = new PWMVictorSPX(this.leftMotor3);

		rightDriveController1 = new PWMVictorSPX(this.rightMotor1);
		rightDriveController2 = new PWMVictorSPX(this.rightMotor2);
		rightDriveController3 = new PWMVictorSPX(this.rightMotor3);

		leftSpeedController = new MultiSpeedController(leftDriveController1, leftDriveController2,
				leftDriveController3);
		rightSpeedController = new MultiSpeedController(rightDriveController1, rightDriveController2,
				rightDriveController3);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new JoystickDrive());
	}

	public void setLeftSpeed(double speed) {
		leftSpeedController.set(speed);
	}

	public void setRightSpeed(double speed) {
		this.rightSpeedController.set(speed);
	}
}
