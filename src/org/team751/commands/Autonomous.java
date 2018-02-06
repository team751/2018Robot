package src.org.team751.commands;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import src.org.team751.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Autonomous extends Command {
	private static double timeToDrive = 15;
	private static double leftSpeed = 0.5;
	private static double rightSpeed = -leftSpeed;
	// 1.216 (0.225/0.185) in C7
	// 0.925 in Bellarmine
	private static double ratio = 0.95;
	private static double totalCurrent;
	private double initDistance, initOrientation;
	private static final int maxError = 5;
	private static final double maxDistanceError = 0.1;
	// The line is 188 inches away from the wall
	// Robot's length with bumper = 38.5 inches
	// Robot's width with bumper = 34.5 inches

	// The central goal is 110 inches away from the wall
	private static final int centralDist = 90;
	private static final int angle = 60;

	// CVR Blue Side (measured from the wall)
	// Left to intersection 85-86 inches
	// Intersection to left goal 88 inches
	// Right to intersection 92 inches
	// Intersection to right goal 83 inches

	private static final int leftFirstDist = 65;
	private static final int leftSecondDist = 68;
	private static final int rightFirstDist = 72;
	private static final int rightSecondDist = 63;
	private static final int numberOfMagnets = 6;
	private static final double wheelDiameter = 6.0;

	// Currentlimit when driving forward is 40 at Bellarmine
	private static double currentLimit = 20;

	Timer timer = new Timer();

	// Whether the Near Switch 751 plate is left(and if false it is
	// right).

	public static boolean isNearSwitchLeft;

	private static boolean driving = false;

	public Autonomous() {
	}

	private void setUpSwitchPosition() {
		System.out.println("Gamedata getting...");

		String gameData;

		gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.isEmpty()) {
			System.out.println("We are not running this in a competition");
			return;
		}

		isNearSwitchLeft = (gameData.charAt(0) == 'L');

		System.out.println("gameData=" + gameData);
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
		timer.reset();
		initDistance = Robot.ADL.getDistance();
		initOrientation = Robot.ADL.getOrientation();
		timeToDrive = 15;

		setUpSwitchPosition();

		// do{
		// driveForDistance(5);
		// }while(driving);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
	}

	private void turnDegreesRight(double degrees) {
		double currentPosition = Robot.ADL.getOrientation();
		double finalPosition;
		driving = true;
		// Gets final position (accounts for negative values)
		if (currentPosition - degrees < 0) {
			finalPosition = (currentPosition - degrees) + 360;
		} else {
			finalPosition = currentPosition - degrees;
		}

		System.out
				.println("driving:" + driving + "current degree" + currentPosition + ", final degree:" + finalPosition);

		while (!(maxError + finalPosition > currentPosition && finalPosition - maxError < currentPosition)) {
			currentPosition = Robot.ADL.getOrientation();
			Robot.drivetrain.setRightSpeed(rightSpeed / 3);
			Robot.drivetrain.setLeftSpeed(-leftSpeed / 3);
			System.out.println(finalPosition - currentPosition + "degree left");
		}

		Robot.drivetrain.setRightSpeed(0);
		Robot.drivetrain.setLeftSpeed(0);
		driving = false;
	}

	private void turnDegreesLeft(double degrees) {
		double currentPosition = Robot.ADL.getOrientation();
		double finalPosition;

		// Gets final position (accounts for negative values)
		if (currentPosition + degrees > 360) {
			finalPosition = (currentPosition + degrees) - 360;
		} else {
			finalPosition = currentPosition + degrees;
		}

		// Waits until the Robot's heading is within the error margin
		// of the final heading position.
		while (!(maxError + finalPosition > currentPosition && finalPosition - maxError < currentPosition)) {
			currentPosition = Robot.ADL.getOrientation();
			Robot.drivetrain.setRightSpeed(-rightSpeed / 3);
			Robot.drivetrain.setLeftSpeed(leftSpeed / 3);
		}

		Robot.drivetrain.setRightSpeed(0);
		Robot.drivetrain.setLeftSpeed(0);
	}

	private void driveForDistance(double feet) {

		final double SPEED = 0.25;
		// final boolean usingRight;
		// final boolean usingLeft;

		// final long leftPulseCount = Robot.ADL.getLeftPulses();
		// final long rightPulseCount = Robot.ADL.getRightPulses();
		if (!driving) {
			initDistance = Robot.ADL.getDistance();
			driving = true;
		}
		double currentDistance = Robot.ADL.getDistance();

		// test prints
		System.out.println("InitialDistance:" + initDistance);
		System.out.println("target:" + (initDistance + feet));
		System.out.println("Distance travel:" + (Robot.ADL.getDistance() - initDistance));

		double distanceTravel = Robot.ADL.getDistance() - initDistance;
		double distanceToTargetRatio = distanceTravel / feet;
		double controlRatio = 1 - distanceToTargetRatio * distanceToTargetRatio;

		// Start going...
		//if the dis travel is not within the range, then keep going with control ratio, else stop
		if (!(Math.abs(Robot.ADL.getDistance() - initDistance) < feet + maxDistanceError
				&& Math.abs(Robot.ADL.getDistance() - initDistance) > feet - maxDistanceError)) {
			Robot.drivetrain.setLeftSpeed(SPEED * controlRatio);
			Robot.drivetrain.setRightSpeed(-SPEED * controlRatio);
		} else {
			Robot.drivetrain.setLeftSpeed(0);
			Robot.drivetrain.setRightSpeed(0);
			driving = false;
			return;
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// 2018 crew: we might want to put the autos under execute rather than
		// start
		do {
			this.driveForDistance(5);
		} while (driving);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return timer.get() >= timeToDrive;
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

}
