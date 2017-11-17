package org.team751.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import com.ctre.CANTalon.TalonControlMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class TestSixCANTalonDrive extends IterativeRobot {

	private final int FRONT_LEFT_PORT = 0;
	private final int FRONT_RIGHT_PORT = 1;
	private final int REAR_LEFT_PORT = 2;
	private final int REAR_RIGHT_PORT = 3;
	private final int LEFT_SLAVE_PORT = 4;
	private final int RIGHT_SLAVE_PORT = 5;
	
	/* talons for arcade drive */
	CANTalon frontLeftMotor = new CANTalon(FRONT_LEFT_PORT); /* device IDs here (1 of 2) */
	CANTalon rearLeftMotor = new CANTalon(REAR_LEFT_PORT);
	CANTalon frontRightMotor = new CANTalon(FRONT_RIGHT_PORT);
	CANTalon rearRightMotor = new CANTalon(REAR_RIGHT_PORT);

	/* extra talons for six motor drives */
	CANTalon leftSlave = new CANTalon(LEFT_SLAVE_PORT);
	CANTalon rightSlave = new CANTalon(RIGHT_SLAVE_PORT);

	RobotDrive drive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

	Joystick joy = new Joystick(0);

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	public void robotInit() {
		/*
		 * take our extra talons and just have them follow the Talons updated in
		 * arcadeDrive
		 */
		leftSlave.changeControlMode(TalonControlMode.Follower);
		rightSlave.changeControlMode(TalonControlMode.Follower);
		leftSlave.set(11); /* device IDs here (2 of 2) */
		rightSlave.set(14);

		/*
		 * the Talons on the left-side of my robot needs to drive reverse(red) to move
		 * robot forward. Since _leftSlave just follows frontLeftMotor, no need to
		 * invert it anywhere.
		 */
		drive.setInvertedMotor(MotorType.kFrontLeft, true);
		drive.setInvertedMotor(MotorType.kRearLeft, true);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		double forward = joy.getRawAxis(1); // logitech gampad left X, positive is forward
		double turn = joy.getRawAxis(2); // logitech gampad right X, positive means turn right
		drive.arcadeDrive(forward, turn);
	}
}
