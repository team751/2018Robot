
package src.org.team751;

import src.org.team751.arduino.ArduinoDataListener;
import src.org.team751.commands.Autonomous;
import src.org.team751.jetson.JoystickInputUDP;
import src.org.team751.jetson.StateSenderUDP;
import src.org.team751.subsystems.Drivetrain;
import src.org.team751.subsystems.Intake;
import src.org.team751.subsystems.Winch;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static final int delayTimeSeconds = 4;

	public static final Drivetrain drivetrain = new Drivetrain();

	public static SpeedController leftSpeedController;
	public static SpeedController rightSpeedController;

	public static DifferentialDrive robotDrive;

	public static final Winch winch = new Winch();
	public static final Intake intake = new Intake();
	public static OI oi;

	public static Command autonomousCommand;
	public static JoystickInputUDP autonomousJoystickSimulator;
	public static StateSenderUDP stateSenderUDP;
	public static boolean crushed;
	public static ArduinoDataListener ADL;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		oi = new OI();
		crushed = false;
		// instantiate the command used for the autonomous period
		leftSpeedController = new MultiSpeedController(drivetrain.leftDriveController1, drivetrain.leftDriveController2,
				drivetrain.leftDriveController3);
		rightSpeedController = new MultiSpeedController(drivetrain.rightDriveController1,
				drivetrain.rightDriveController2, drivetrain.rightDriveController3);
		ADL = new ArduinoDataListener();
		autonomousCommand = new Autonomous();
		Thread motorControlThread = new Thread(autonomousJoystickSimulator);
		motorControlThread.start();

		System.out.println("creating ADL thread");
		Thread listenerThread = new Thread(ADL);
		listenerThread.start();
		robotDrive = new DifferentialDrive(leftSpeedController, rightSpeedController);
	}

	public void disabledPeriodic() {
		this.printArduinoInfo();
	}

	private void printArduinoInfo() {
		// System.out.println("Orientation: " + ADL.getOrientation() +
		// "Distance: " + ADL.getDistance());
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
		crushed = false;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		this.printArduinoInfo();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();

	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	public void disabledInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		printArduinoInfo();

//		System.out.println("Left Speed: " + Robot.drivetrain.leftDriveController1.getSpeed());
//		System.out.println("Right Speed: " + Robot.drivetrain.rightDriveController1.getSpeed());
//
//		// Current check
//		System.out.println("Total Current: " + Robot.drivetrain.pdp.getTotalCurrent());
//		System.out.print("Left Motors: " + "Motor1: " + Robot.drivetrain.pdp.getCurrent(drivetrain.leftMotor1) + ", ");
//		System.out.print("Motor2: " + Robot.drivetrain.pdp.getCurrent(drivetrain.leftMotor2) + ", ");
//		System.out.print("Motor3: " + Robot.drivetrain.pdp.getCurrent(drivetrain.leftMotor3) + ", ");
//		System.out.print("Right Motors: " + "Motor1: " + Robot.drivetrain.pdp.getCurrent(drivetrain.rightMotor1) + ", ");
//		System.out.print("Motor2: " + Robot.drivetrain.pdp.getCurrent(drivetrain.rightMotor2) + ", ");
//		System.out.println("Motor3: " + Robot.drivetrain.pdp.getCurrent(drivetrain.rightMotor3));
	}

	/**
	 * This function is called periodically during test mode
	 */
	@SuppressWarnings("deprecation")
	public void testPeriodic() {
		LiveWindow.run();
	}

}
