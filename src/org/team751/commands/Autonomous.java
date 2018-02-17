package src.org.team751.commands;

import src.org.team751.Robot;

import edu.wpi.first.wpilibj.Timer;

import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous extends Command {
	private static double timeToDrive = 15;
	private static double leftSpeed = 0.2;
	private static double rightSpeed = -leftSpeed;
	// 1.216 (0.225/0.185) in C7
	// 0.925 in Bellarmine
	private static double ratio = 0.95;
	private static double totalCurrent;
	private double initDistance, initOrientation;
	private static final double DRIVING_STRAIGHT_STOPPING_DISTANCE = 3.0;
	private static final double TURNING_STOPPING_DISTANCE = 19.0;
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
	public static boolean isScaleLeft;

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

		isScaleLeft = (gameData.charAt(1) == 'L');

		System.out.println("gameData=" + gameData);
	}

	private double[] chooseLeftPath(final boolean posSpecificSwitch1, final boolean posSpecificSwitch2,
			final boolean posSpecificSwitch3) {
		double[] returnValue;

		// If switch 7 is on, both the switch and the scale are on the right,
		// or the switch is on the right and the scale is not allowed,
		// just cross the auto line.
		if ((posSpecificSwitch3) || (!isNearSwitchLeft && !isScaleLeft) || (!isNearSwitchLeft && posSpecificSwitch2)) {
			// Path = Left to auto line
			// Go 140 in.
			returnValue = new double[] { 140 };
		} else if (isScaleLeft && isNearSwitchLeft) {
			// If scale preferred, travel to scale.
			// If not, travel to switch.
			if (posSpecificSwitch1) {
				// Path = Left to Left scale
				// Go 249.65 in., turn right, go 24 in., turn left, Go 10 in.
				returnValue = new double[] { 249.65, -2, 24, -1, 10 };
			} else {
				// Path = Left to Left switch
				// Go 128 in., turn right, go 19.75 in.
				returnValue = new double[] { 128, -2, 19.75 };
			}
		} else if (isNearSwitchLeft && !isScaleLeft) {
			// Path = Left to Left switch
			// Go 128 in., turn right, go 19.75 in.
			returnValue = new double[] { 128, -2, 19.75 };
		} else if (isScaleLeft && !posSpecificSwitch2) {
			// Path = Left to Left scale
			// Go 249.65 in., turn right, go 24 in., turn left, Go 10 in.
			returnValue = new double[] { 249.65, -2, 24, -1, 10 };
		} else {
			// Path = lol wut something went wrong
			returnValue = new double[] {};
		}

		return returnValue;
	}

	private double[] chooseMiddlePath(final boolean posSpecificSwitch1, final boolean posSpecificSwitch2,
			final boolean posSpecificSwitch3) {
		double[] returnValue;

		// If our switch side is not allowed, or switch 7
		// is on, then just cross auto line.
		if ((isNearSwitchLeft && posSpecificSwitch1) || (!isNearSwitchLeft && posSpecificSwitch2)
				|| posSpecificSwitch3) {
			// Path = Middle to auto line
			// Go 48 in., turn right, go 71.25 in., turn left, go 92 in.
			returnValue = new double[] { 48, -2, 71.25, -1, 92 };
		} else if (isNearSwitchLeft) {
			// Path = Middle to Left Switch
			// Go 68 in., turn left, go 102.25 in., turn right, go 22 in.
			returnValue = new double[] { 68, -1, 102.25, -2, 22 };
		} else if (!isNearSwitchLeft) {
			// Path = Middle to Right Switch
			// Go 68 in., turn right, go 91.25 in, turn left, go 22 in.
			returnValue = new double[] { 68, -2, 91.25, -1, 22 };
		} else {
			// Path = lol wut something went wrong
			returnValue = new double[] {};
		}

		return returnValue;
	}

	private double[] chooseRightPath(final boolean posSpecificSwitch1, 
									final boolean posSpecificSwitch2,
									final boolean posSpecificSwitch3) {
		double[] returnValue;

		// If switch 7 is on, both the switch and the scale are on the right,
		// or the switch is on the right and the scale is not allowed,
		// just cross the auto line.
		if ((posSpecificSwitch3) || (isNearSwitchLeft && isScaleLeft) || 
			(isNearSwitchLeft && posSpecificSwitch2)) {
			// Path = Right to auto line
			// Go 140 in.
			returnValue = new double[] { 140 };
		} else if (!isScaleLeft && !isNearSwitchLeft) {
			// If scale preferred, travel to scale.
			// If not, travel to switch.
			if (posSpecificSwitch1) {
				// Path = Right to Right scale
				// Go 249.65 in., turn left, go 24 in., turn right, Go 10 in.
				returnValue = new double[] { 249.65, -1, 24, -2, 10 };
			} else {
				// Path = Right to Right switch
				// Go 128 in., turn left, go 19.75 in.
				returnValue = new double[] { 128, -1, 19.75 };
			}
		} else if (!isNearSwitchLeft && isScaleLeft) {
			// Path = Right to Right switch
			// Go 128 in., turn left, go 19.75 in.
			returnValue = new double[] { 128, -1, 19.75 };
		} else if (isScaleLeft && !posSpecificSwitch2) {
			// Path = Right to Right scale
			// Go 249.65 in., turn left, go 24 in., turn right, Go 10 in.
			returnValue = new double[] { 249.65, -1, 24, -2, 10 };
		} else {
			// Path = lol wut something went wrong
			returnValue = new double[] {};
		}

		return returnValue;
	}

	private double[] decidePath() throws InterruptedException {
		final boolean leftPos = Robot.oi.autoSwitches[0].get();
		final boolean middlePos = Robot.oi.autoSwitches[1].get();
		final boolean rightPos = Robot.oi.autoSwitches[2].get();

		final boolean delay = Robot.oi.autoSwitches[3].get();

		final boolean posSpecificSwitch1 = Robot.oi.autoSwitches[4].get();
		final boolean posSpecificSwitch2 = Robot.oi.autoSwitches[5].get();
		final boolean posSpecificSwitch3 = Robot.oi.autoSwitches[7].get();

		int switchPosNum = (leftPos) ? 1 : 0;
		switchPosNum += (middlePos) ? 1 : 0;
		switchPosNum += (rightPos) ? 1 : 0;

		setUpSwitchPosition();

		// If zero or more than one position switch is enabled,
		// then stand still.
		if (switchPosNum == 0 || switchPosNum > 1) {
			return new double[0];
		}

		if (delay) {
			TimeUnit.SECONDS.sleep(Robot.delayTimeSeconds);
		}

		if (leftPos) {
			return chooseLeftPath(posSpecificSwitch1, posSpecificSwitch2, 
					             posSpecificSwitch3);
		} else if (middlePos) {
			return chooseMiddlePath(posSpecificSwitch1, posSpecificSwitch2, 
					                posSpecificSwitch3);
		} else {
			return chooseRightPath(posSpecificSwitch1, posSpecificSwitch2, 
					              posSpecificSwitch3);
		}
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
					turnDegrees(270);
				}
			} else {
				driveForDistance(currentPath);
			}
		}
	}

	public double addMod360(double degrees, double degrees2) {
		double result = degrees + degrees2;

		if (result >= 360.0) {
			result -= 360;
		}

		return result;
	}

	private void turnDegrees(double degrees) {

		double currentPosition = Robot.ADL.getOrientation() % 360;
		final double initDegrees = currentPosition;
		final double finalPosition = this.newHeading(currentPosition, currentPosition + degrees);
		double controlRatio = 0.0;
		double degreesTraveled = 0.0;
		double slowingPosition = 0.0;
		double speed = 0.2;
		boolean turnRight = degrees < 180 ? true : false;
		double stoppingDistance = turnRight ? TURNING_STOPPING_DISTANCE : TURNING_STOPPING_DISTANCE - 1.0;

		// Gets stopping position
		if (turnRight) {
			if (currentPosition + (degrees - stoppingDistance) > 360) {
				slowingPosition = (currentPosition + (degrees - stoppingDistance)) - 360;
			} else {
				slowingPosition = currentPosition + degrees - stoppingDistance;
			}
		} else {
			if (currentPosition + (degrees + stoppingDistance) > 360) {
				slowingPosition = (currentPosition + (degrees + stoppingDistance)) - 360;
			} else {
				slowingPosition = currentPosition + degrees + stoppingDistance;
			}
		}

		driving = true;
		while (driving) {
			currentPosition = Robot.ADL.getOrientation() % 360;
			degreesTraveled = this.angularDistance(initDegrees, currentPosition);
			// Determines whether to turn left or right.
			// if it turns right, the degree goes up
			// turn left -> go down
			/*
			 * System.out.println("Final Position: " + finalPosition);
			 * System.out.println("Initial Degrees: " + initDegrees);
			 * System.out.println("Degrees Traveled: " + degreesTraveled);
			 * System.out.println("Current Position: " + currentPosition);
			 * System.out.println("Slowing Position: " + slowingPosition +
			 * "\n");
			 */

			// Waits until the Robot's heading is within the error margin
			// of the final heading position.
			// 90 - 5 > 50 && 95 > 50 || 85 < x < 95
			// TODO
			if (degreesTraveled < angularDistance(initDegrees, slowingPosition)) {
				controlRatio = 1.0; // Full speed
			}
			// Reached the stopping distance threshold, but not at goal.
			else if (degreesTraveled < angularDistance(initDegrees, finalPosition)) {
				final double stoppingZoneProgress = angularDistance(slowingPosition, currentPosition)
						/ angularDistance(slowingPosition, finalPosition);
				controlRatio = 1 - stoppingZoneProgress;
			} // Reached the goal.
			else {
				System.out.println("Reached the goal");
				controlRatio = 0.0;
				driving = false;
			}

			// System.out.println("Control Ratio: " + controlRatio);
			// System.out.println("Degrees Traveled: " + degreesTraveled);
			if (turnRight) {
				Robot.drivetrain.setLeftSpeed(speed * controlRatio);
				Robot.drivetrain.setRightSpeed(speed * controlRatio);
			} else {
				Robot.drivetrain.setLeftSpeed(-speed * controlRatio);
				Robot.drivetrain.setRightSpeed(-speed * controlRatio);
			}
		}

		Robot.drivetrain.setRightSpeed(0);
		Robot.drivetrain.setLeftSpeed(0);
		driving = false;
		System.out.println("Degrees Traveled: " + degreesTraveled);
	}

	// }

	private double angularDistance(double angle1, double angle2) {
		return 180.0 - Math.abs(Math.abs(angle1 - angle2) - 180.0);
	}

	private double newHeading(double angle1, double angle2) {
		return (this.angularDistance(angle1, angle2) + angle1) % 360.0;
	}

	private double skew(double initial, double current) {
		double result = (current + 360.0 - initial) % 360.0;
		if (result > 180.0) { // Skewing to the left, need to boost left side.
			return -(result - 180.0);
		} else if (result < 180.0) { // Skewing to the right, need to boost
										// right side.
			return result;
		} else {
			// If result is 180, we're going in the exact opposite direction -->
			// severe error.
			System.out.println("ERROR: Going in exact opposite direction!");
			return Double.MIN_VALUE;
		}
	}

	private void driveForDistance(double feet) {

		initDistance = Robot.ADL.getDistance();

		double leftCalculatedSpeed = 0.0;
		double rightCalculatedSpeed = 0.0;

		// setting up the supposed orientation, so that we know if it changes
		initOrientation = Robot.ADL.getOrientation();
		driving = true;

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

			// SKEWING CORRECTING CODE HERE
			double skew = this.skew(initOrientation, currentOrientation);
			if (skew < 0.0) {
				// Boost left
				leftCalculatedSpeed += -skew; // NEED TO DEVISE A WAY TO
												// INCORPORATE THE RESULT INTO
												// SPEED.
			} else if (skew > 0.0) {
				// Boost right
				rightCalculatedSpeed += skew; // ^^^
			}

			if (distanceTraveled < feet - DRIVING_STRAIGHT_STOPPING_DISTANCE) {
				controlRatio = 1.0; // Full speed
			}
			// Reached the stopping distance threshold, but not at goal.
			else if (distanceTraveled < feet) {
				final double stoppingZoneProgress = (distanceTraveled - (feet - DRIVING_STRAIGHT_STOPPING_DISTANCE))
						/ DRIVING_STRAIGHT_STOPPING_DISTANCE;

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

			Robot.drivetrain.setLeftSpeed(leftSpeed * controlRatio * leftCalculatedSpeed);
			Robot.drivetrain.setRightSpeed(rightSpeed * controlRatio * rightCalculatedSpeed);
		}

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// 2018 crew: we might want to put the autos under execute rather than
		// start

		// this.turnDegreesRight(90);
		// this.turnDegreesRight(90);
		if (count == 0) {
			this.driveForDistance(10.0);
			this.turnDegrees(90.0);
			// this.turnDegrees(45.0);
			// this.turnDegrees(270.0);
			/*try {
				executePath(decidePath());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
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
