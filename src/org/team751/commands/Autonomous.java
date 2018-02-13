package src.org.team751.commands;

import src.org.team751.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	private static final double stoppingDistance = 3.0;
	private static final double turnDuringStraightError = 0.5;
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

	private int count = 0;

	// Currentlimit when driving forward is 40 at Bellarmine
	private static double currentLimit = 20;

	Timer timer = new Timer();

	// Whether the Near Switch 751 plate is left(and if false it is
	// right).

	public static boolean isNearSwitchLeft;

	private static boolean driving = false;

	// private static PIDController leftPID;
	// private static PIDController rightPID;
	private static final double kP = 1.0;
	private static final double kI = 0.0;
	private static final double kD = 0.0;

	public Autonomous() {
		// leftPID = new PIDController(kP, kI, kD, Robot.ADL,
		// Robot.leftSpeedController);
		// rightPID = new PIDController(kP, kI, kD, Robot.ADL,
		// Robot.rightSpeedController);
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

		// leftPID.enable();
		// rightPID.enable();

		setUpSwitchPosition();

		// do{
		// driveForDistance(5);
		// }while(driving);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
	}

	public void executePath(double[] path) {
		for (double currentPath : path) {
			if (currentPath < 0) {
				if (currentPath != -1) {
					turnDegrees(90);
				} else {
					turnDegreesRight(90);
				}
			} else {
				driveForDistance(currentPath);
			}
		}
	}

	private void turnDegreesRight(double degrees) {
		double currentPosition = Robot.ADL.getOrientation();
		double finalPosition;
		double speedRatio;
		driving = true;
		// Gets final position (accounts for negative values)
		while (driving) {
			if (currentPosition - degrees < 0) {
				finalPosition = (currentPosition - degrees) + 360;
			} else {
				finalPosition = currentPosition - degrees;
			}

			// leftPID.setSetpoint(degrees);
			// rightPID.setSetpoint(degrees);

			System.out.println(
					"driving:" + driving + "current degree" + currentPosition + ", final degree:" + finalPosition);

			while (!(maxError + finalPosition > currentPosition && finalPosition - maxError < currentPosition)) {
				currentPosition = Robot.ADL.getOrientation();
				speedRatio = Math.abs(finalPosition - currentPosition) / degrees;
				Robot.drivetrain.setRightSpeed(rightSpeed / 4 + 0.25 * rightSpeed * speedRatio);
				Robot.drivetrain.setLeftSpeed(-leftSpeed / 4 + 0.25 * rightSpeed * speedRatio);
				System.out.println(finalPosition - currentPosition + "degree left");
			}

			Robot.drivetrain.setRightSpeed(0);
			Robot.drivetrain.setLeftSpeed(0);
			driving = false;
		}
	}

	private void turnDegrees(double degrees) {
		double currentPosition = Robot.ADL.getOrientation();
		double finalPosition;
		double controlRatio;
		double degreesTravelled;
		final double slowingPosition;

		driving = true;
		while (driving) {
			// Gets final position (accounts for negative values)
			if (currentPosition + degrees > 360) {
				finalPosition = (currentPosition + degrees) - 360;
			} else {
				finalPosition = currentPosition + degrees;
			}

			// Determines whether to turn left or right.
			// if it turns right, the degree goes up
			// turn left -> go down
			boolean turnRight = degrees < 180 ? true : false;
			
			//Gets stopping position
			if(turnRight){
			if (currentPosition + (degrees-30) > 360) {
				slowingPosition = (currentPosition + (degrees-30)) - 360;
			} else {
				slowingPosition = currentPosition + degrees - 30;
			}
			}else{
				if (currentPosition + (degrees + 30) > 360){
					slowingPosition = (currentPosition + (degrees-30)) - 360;
				}else{
					slowingPosition = currentPosition + degrees + 30;
				}
			}

			// Waits until the Robot's heading is within the error margin
			// of the final heading position.
			// 90 - 5 > 50 && 95 > 50 || 85 < x < 95
			// TODO
			while (finalPosition-maxError > currentPosition ||
				   finalPosition+maxError < currentPosition) {
				
				if (turnRight){
					if (currentPosition <  + 360 % 360) {
						controlRatio = 1.0; // Full speed
					}
				}else{
					//when turning left, it goes from 
					if (currentPosition > slowingPosition){
						
					}
				}
				// Reached the stopping distance threshold, but not at goal.
				else if (degreesTravelled < degrees) {
					final double stoppingZoneProgress = (degreesTravelled - (feet - stoppingDistance)) / stoppingDistance;

					controlRatio = 1 - stoppingZoneProgress;
					if (controlRatio < 0.2) {
						controlRatio = 0.2;
					}
				}
				// Reached the goal.
				else {
					controlRatio = 0.0;
					driving = false;
				}

				Robot.drivetrain.setLeftSpeed(leftSpeed * controlRatio);
				Robot.drivetrain.setRightSpeed(-rightSpeed * controlRatio);
			}

			Robot.drivetrain.setRightSpeed(0);
			Robot.drivetrain.setLeftSpeed(0);
			driving = false;
		}
	}

	private void driveForDistance(double feet) {

		double leftSpeed = 0.3;
		double rightSpeed = 0.3;
		// final boolean usingRight;
		// final boolean usingLeft;

		// final long leftPulseCount = Robot.ADL.getLeftPulses();
		// final long rightPulseCount = Robot.ADL.getRightPulses();
		initDistance = Robot.ADL.getDistance();

		// setting up the supposed orientation, so that we know if it changes
		initOrientation = Robot.ADL.getOrientation();
		driving = true;

		// leftPID.setSetpoint(feet);
		// rightPID.setSetpoint(feet);

		while (driving) {
			final double currentDistance = Robot.ADL.getDistance();
			final double currentOrientation = Robot.ADL.getOrientation();

			// test prints
			// System.out.println("InitialDistance:" + initDistance);
			// System.out.println("ADL Distance:" + currentDistance);
			// System.out.println("target:" + (initDistance + feet));
			// System.out.println("Distance travel:" + (Robot.ADL.getDistance()
			// - initDistance));

			final double distanceTraveled = currentDistance - initDistance;
			double controlRatio;

			// calculating the four possibilities because of 360->0
			// 350->10, 10->350, 40-50, 50-40
			// case 1: tRD = -340, tLD = 340, -340+360 = 20, 340+360 = 700, so
			// 20 < 700 --> choose turnRightDegree and speed up right
			// case 2: tRD = 340, tLD = -340, same calculations, 20<700 -->
			// choose turnLeftDegree and speed up left
			// case 3: tRD = 10, tLD = -10, 370, 350, both greater than 180,
			// choose the non-negative one --> tRD --> speed up right
			// case 4: similar to case 3
			double turnRightDegree = currentOrientation - initOrientation;
			double turnLeftDegree = initOrientation - currentOrientation;
			boolean turningRight = false;
			boolean turningLeft = false;
			// set boundary to 90
			if (Math.abs(turnRightDegree) < 180 && Math.abs(turnLeftDegree) < 180) {
				if (turnRightDegree > turnDuringStraightError) {
					// if (rightSpeed < 0.35)
					// rightSpeed += 0.001;
					// else
					leftSpeed -= 0.001;
				} else if (turnLeftDegree > turnDuringStraightError) {
					// if (leftSpeed < 0.35)
					// leftSpeed += 0.001;
					// else
					rightSpeed -= 0.001;
				}
			} else {
				if (turnRightDegree < 0) {
					if ((turnRightDegree + 360) > turnDuringStraightError) {
						// if (rightSpeed < 0.35)
						// rightSpeed += 0.001;
						// else
						leftSpeed -= 0.001;
					}
				} else {
					if ((turnLeftDegree + 360) > turnDuringStraightError) {
						// if (leftSpeed < 0.35)
						// leftSpeed += 0.001;
						// else
						rightSpeed -= 0.001;
					}
				}
			}
			System.out.println("leftSpeed:" + leftSpeed);
			System.out.println("rightSpeed:" + rightSpeed);
			// If we haven't reached the stopping distance threshold.

			if (distanceTraveled < feet - stoppingDistance) {
				controlRatio = 1.0; // Full speed
			}
			// Reached the stopping distance threshold, but not at goal.
			else if (distanceTraveled < feet) {
				final double stoppingZoneProgress = (distanceTraveled - (feet - stoppingDistance)) / stoppingDistance;

				controlRatio = 1 - stoppingZoneProgress;
				if (controlRatio < 0.2) {
					controlRatio = 0.2;
				}
			}
			// Reached the goal.
			else {
				controlRatio = 0.0;
				driving = false;
			}

			Robot.drivetrain.setLeftSpeed(leftSpeed * controlRatio);
			Robot.drivetrain.setRightSpeed(-rightSpeed * controlRatio);
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// 2018 crew: we might want to put the autos under execute rather than
		// start

		// this.turnDegreesRight(90);
		// this.turnDegreesRight(90);
		if (count == 0) {
			// this.driveForDistance(3);
			this.turnDegrees(90);
			count++;
		}

		// double path[] = {128, -2};

		// this.executePath(path);
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
