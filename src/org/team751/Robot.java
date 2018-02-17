
package src.org.team751;

import java.net.UnknownHostException;

import src.org.team751.arduino.ArduinoDataListener;
import src.org.team751.commands.Autonomous;
import src.org.team751.jetson.JoystickInputUDP;
import src.org.team751.jetson.StateSenderUDP;
import src.org.team751.subsystems.Drivetrain;
import src.org.team751.subsystems.Intake;
import src.org.team751.subsystems.Winch;

import java.net.UnknownHostException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

	Command autonomousCommand;
	public static JoystickInputUDP autonomousJoystickSimulator;
	public static StateSenderUDP stateSenderUDP;
	public static boolean crushed;
	public static ArduinoDataListener ADL;

	private void setUpSwitchPosition() {
		System.out.println("Gamedata getting...");

		String gameData;

		gameData = DriverStation.getInstance().getGameSpecificMessage();

		Autonomous.isNearSwitchLeft = (gameData.charAt(0) == 'L');

		System.out.println("gameData=" + gameData);
	}

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
		printarduinoinfo();

		// Scheduler.getInstance().run();
		// SmartDashboard.putNumber("leftEncoder",
		// Robot.drivetrain.leftEncoder.getDistance());
		// SmartDashboard.putNumber("rightEncoder",
		// Robot.drivetrain.rightEncoder.getDistance());
		// SmartDashboard.putNumber("Right Encoder Rate",
		// Robot.drivetrain.rightEncoder.getRate());
		// SmartDashboard.putNumber("Left Encoder Rate",
		// Robot.drivetrain.rightEncoder.getRate());
		//

		// System.out.println("leftEncoder" +
		// Robot.drivetrain.leftEncoder.getDistance());
		// System.out.println("RightEncoder" +
		// -Robot.drivetrain.rightEncoder.getDistance());
		//
		// try {
		// stateSenderUDP.sendState(RobotState.DISABLED, 0);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private void printarduinoinfo() {
		// SmartDashboard.putNumber("X", ADL.getX());
		// SmartDashboard.putNumber("Y", ADL.getY());
		// System.out.println("Heading: " + ADL.getHeading() + ", Velocity: " +
		// ADL.getVelocity() + ", Distance: " + ADL.getDistance());
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)

		if (autonomousCommand != null)
			autonomousCommand.start();
		crushed = false;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		System.out.println("Auton enabled");
		printarduinoinfo();
		// System.out.println("Heading: " + ADL.getHeading());
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
		printarduinoinfo();
		// SmartDashboard.putNumber("leftEncoder",
		// Robot.drivetrain.leftEncoder.getDistance());
		// SmartDashboard.putNumber("rightEncoder",
		// -Robot.drivetrain.rightEncoder.getDistance());
		// System.out.println("leftEncoder" +
		// Robot.drivetrain.leftEncoder.getDistance());
		// System.out.println("RightEncoder" +
		// Robot.drivetrain.rightEncoder.getDistance());

		// System.out.println("Left Speed: " +
		// Robot.drivetrain.leftDriveController1.getSpeed());
		// System.out.println("Right Speed: " +
		// Robot.drivetrain.rightDriveController1.getSpeed());

		// Current check
		// System.out.println("Total Current: " +
		// Robot.drivetrain.pdp.getTotalCurrent());
		// System.out.print("Left Motors: " + "Motor1: " +
		// Robot.drivetrain.pdp.getCurrent(3) + ",");
		// System.out.print("Motor3: " + Robot.drivetrain.pdp.getCurrent(2) +
		// ",");
		// System.out.print("Motor5: " + Robot.drivetrain.pdp.getCurrent(1) +
		// ",");
		// System.out.print("Right Motors: " + "Motor0: " +
		// Robot.drivetrain.pdp.getCurrent(0) + ",");
		// System.out.print("Motor2: " + Robot.drivetrain.pdp.getCurrent(13) +
		// ",");
		// System.out.print("Motor4: " + Robot.drivetrain.pdp.getCurrent(14) +
		// ",");
		// System.out.println();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}

}
