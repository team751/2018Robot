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
	private static double timeToDrive = 15; // Duration of the Autonomous
											// period.
	private static double leftSpeed = 0.3; // Speed for the left side.
	private static double rightSpeed = -leftSpeed; // Speed for the right side
													// (same as left).

	private double initDistance, initOrientation;

	// The distance (in feet) at which the robot starts slowing down when
	// driving straight.
	// TO TUNE: If the robot is OVERSHOOTING: Increase this value.
	// If the robot is UNDERSHOOTING: Decrease this value.
	private final double DRIVING_STRAIGHT_STOPPING_DISTANCE = 3.0;

	// The acceptable error (in feet) for driving straight.
	private final double DRIVING_STRAIGHT_ERROR = 0.1; // 1.2 inches.

	// The angular distance (in degrees) at which the robot starts to slow down.
	// TO TUNE: If the robot is OVERTURNING: Increase this value.
	// If the robot is UNDERTURNING: Decrease this value.
	private final double TURNING_STOPPING_DISTANCE = 22.0;

	// How much the left side can compensate for skewing.
	// TO TUNE: If the robot is skewing to the LEFT: Increase this value
	// Or, decrease RIGHT_SKEW_CEILING.
	private final double LEFT_SKEW_CEILING = 0.03;
	// How much the right side can compensate for skewing.
	// TO TUNE: If the robot is skewing to the RIGHT: Increase this value.
	// Or, decrease LEFT_SKEW_CEILING.
	private final double RIGHT_SKEW_CEILING = 0.03;

	private int count = 0; // Test var so execute only does something once.

	Timer timer = new Timer();

	public static boolean isNearSwitchLeft; // Flag denoting which side of the
											// switch is our's.
	public static boolean isScaleLeft; // Flag denoting which side of the scale
										// is our's.

	// Flag denoting whether the robot is driving or not.
	private static boolean driving = false;
	private double targetTimeMillis;

	public Autonomous() {

	}

	private void setUpSwitchPosition() {
		System.out.println("Gamedata getting...");

		/*
		 * String gameData;
		 * 
		 * gameData = DriverStation.getInstance().getGameSpecificMessage();
		 * 
		 * if (gameData.isEmpty()) {
		 * System.out.println("We are not running this in a competition");
		 * return; }
		 * 
		 * isNearSwitchLeft = (gameData.charAt(0) == 'L');
		 * 
		 * isScaleLeft = (gameData.charAt(1) == 'L');
		 * 
		 * System.out.println("gameData=" + gameData);
		 */

		isNearSwitchLeft = false;

		isScaleLeft = false;
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
			returnValue = new double[] { 140, -3 };
		} else if (isScaleLeft && isNearSwitchLeft) {
			// If scale preferred, travel to scale.
			// If not, travel to switch.
			if (posSpecificSwitch1) {
				// Path = Left to Left scale
				// Go 249.65 in., turn right, go 24 in., turn left, Go 10 in.
				returnValue = new double[] { 249.65, -4, -2, 24, -1, 10 };
			} else {
				// Path = Left to Left switch
				// Go 128 in., turn right, go 19.75 in.
				returnValue = new double[] { 128, -4, -2, 19.75 };
			}
		} else if (isNearSwitchLeft && !isScaleLeft) {
			// Path = Left to Left switch
			// Go 128 in., turn right, go 19.75 in.
			returnValue = new double[] { 128, -4, -2, 19.75 };
		} else if (isScaleLeft && !posSpecificSwitch2) {
			// Path = Left to Left scale
			// Go 249.65 in., turn right, go 24 in., turn left, Go 10 in.
			returnValue = new double[] { 249.65, -2, 24, -4, -1, 10 };
		} else {
			// Path = lol wut something went wrong
			returnValue = new double[] {-3};
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
			// TODO: Switch 7 is Left/Right auto path, not just auto path.

			// Path = Middle to auto line
			// Go 48 in., turn right, go 71.25 in., turn left, go 92 in.
			returnValue = new double[] { 48, -2, 71.25, -1, 92, -3 };
			System.out.println("Path: Middle to auto line");
		} else if (isNearSwitchLeft) {
			// Path = Middle to Left Switch
			// Go 48 in., turn left, go 112.25 in., turn right, go 100 in,
			// turn right, go 10 inches.
			returnValue = new double[] { 48, -1, 112.25, -2, 100, -4, -2, 10 };
			System.out.println("Path: Middle to left switch");
		} else if (!isNearSwitchLeft) {
			// Path = Middle to Right Switch
			// Go 48 in., turn right, go 101.25 in, turn left, go 100 in,
			// turn right, go 10 inches.
			returnValue = new double[] { 48, -2, 101.25, -1, 100, -4, -2, 10 };
			System.out.println("Path: Middle to right switch");
		} else {
			// Path = lol wut something went wrong
			returnValue = new double[] {-3};
		}

		return returnValue;
	}

	private double[] chooseRightPath(final boolean posSpecificSwitch1, final boolean posSpecificSwitch2,
			final boolean posSpecificSwitch3) {
		double[] returnValue;

		// If switch 7 is on, both the switch and the scale are on the right,
		// or the switch is on the right and the scale is not allowed,
		// just cross the auto line.
		if ((posSpecificSwitch3) || (isNearSwitchLeft && isScaleLeft) || (isNearSwitchLeft && posSpecificSwitch2)) {
			// Path = Right to auto line
			// Go 140 in.
			returnValue = new double[] { 140, -3 };
			System.out.println("Taking path: Right to auto line");
		} else if (!isScaleLeft && !isNearSwitchLeft) {
			// If scale preferred, travel to scale.
			// If not, travel to switch.
			if (posSpecificSwitch1) {
				// Path = Right to Right scale
				// Go 249.65 in., turn left, go 24 in., turn right, Go 10 in.
				returnValue = new double[] { 249.65, -1, -4, 24, -2, 10 };
				System.out.println("Taking path: Right to right scale");
			} else {
				// Path = Right to Right switch
				// Go 128 in., turn left, go 19.75 in.
				returnValue = new double[] { 128, -1, -4, 19.75 };
				System.out.println("Taking path: Right to right switch");
			}
		} else if (!isNearSwitchLeft && isScaleLeft) {
			// Path = Right to Right switch
			// Go 128 in., turn left, go 19.75 in.
			returnValue = new double[] { 128, -1, -4, 19.75 };
			System.out.println("Taking path: Right to right switch");
		} else if (isScaleLeft && !posSpecificSwitch2) {
			// Path = Right to Right scale
			// Go 249.65 in., turn left, go 24 in., turn right, Go 10 in.
			returnValue = new double[] { 249.65, -1, 24, -4, -2, 10 };
			System.out.println("Taking path: Right to right scale");
		} else {
			// Path = Error
			returnValue = new double[] {-3};
			System.out.println("Taking path: Error");
		}

		return returnValue;
	}

	private double[] decidePath() throws InterruptedException {
		final boolean leftPos = /* Robot.oi.autoSwitches[0].get() */false;
		final boolean middlePos = Robot.oi.autoSwitches[1].get();
		final boolean rightPos = /* Robot.oi.autoSwitches[2].get() */false;

		final boolean delay = Robot.oi.autoSwitches[3].get();

		final boolean posSpecificSwitch1 = Robot.oi.autoSwitches[4].get();
		final boolean posSpecificSwitch2 = Robot.oi.autoSwitches[5].get();
		final boolean posSpecificSwitch3 = Robot.oi.autoSwitches[6].get();

		int switchPosNum = (leftPos) ? 1 : 0;
		switchPosNum += (middlePos) ? 1 : 0;
		switchPosNum += (rightPos) ? 1 : 0;

		setUpSwitchPosition();

		System.out.println("2: " + rightPos + " 3: " + delay + " 4: " + posSpecificSwitch1 + " 5: " + posSpecificSwitch2
				+ " 6: " + posSpecificSwitch3);

		// If zero or more than one position switch is enabled,
		// then stand still.
		if (switchPosNum == 0 || switchPosNum > 1) {
			System.out.println("SwitchPosNum=" + switchPosNum);
			return new double[0];
		}

		if (delay) {
			TimeUnit.SECONDS.sleep(Robot.delayTimeSeconds);
		}

		if (leftPos) {
			System.out.println("Position: Left");
			return chooseLeftPath(posSpecificSwitch1, posSpecificSwitch2, posSpecificSwitch3);
		} else if (middlePos) {
			System.out.println("Position: Middle");
			return chooseMiddlePath(posSpecificSwitch1, posSpecificSwitch2, posSpecificSwitch3);
		} else {
			System.out.println("Position: Right");
			return chooseRightPath(posSpecificSwitch1, posSpecificSwitch2, posSpecificSwitch3);
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

		setUpSwitchPosition();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
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
		double speed = 0.3;
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
		while (driving && Robot.autoEnabled && !(System.currentTimeMillis() >= targetTimeMillis)) {
			currentPosition = Robot.ADL.getOrientation() % 360;
			degreesTraveled = this.angularDistance(initDegrees, currentPosition);

			// System.out.println("Final Position: " + finalPosition);
			// System.out.println("Initial Degrees: " + initDegrees);
			// System.out.println("Degrees Traveled: " + degreesTraveled);
			// System.out.println("Current Position: " + currentPosition);
			// System.out.println("Slowing Position: " + slowingPosition +
			// "\n");

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
				System.out.println("Done turning");
				controlRatio = 0.0;
				driving = false;
			}

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

		System.out.println("Initial Orientation: " + initDegrees);
		System.out.println("Degrees Traveled: " + degreesTraveled);
		System.out.println("Final Orientation: " + currentPosition);
	}

	// }

	private double angularDistance(double angle1, double angle2) {
		return 180.0 - Math.abs(Math.abs(angle1 - angle2) - 180.0);
	}

	private double newHeading(double angle1, double angle2) {
		return (this.angularDistance(angle1, angle2) + angle1) % 360.0;
	}

	private double skew(double initial, double current) {
		double result = (360.0 - (initial + 360.0 - current) % 360.0) % 180.0;
		if (this.angularDistance(initial, current) == (180.0 - result)) {
			return result - 180.0;
		}
		return result;
	}

	/**
	 * Re-maps a number from one range to another. That is, a value of fromLow
	 * would get mapped to toLow, a value of fromHigh to toHigh, values
	 * in-between to values in-between, etc.
	 * 
	 * Does not constrain values to within the range, because out-of-range
	 * values are sometimes intended and useful. The constrain() function may be
	 * used either before or after this function, if limits to the ranges are
	 * desired.
	 * 
	 * Note that the "lower bounds" of either range may be larger or smaller
	 * than the "upper bounds" so the map() function may be used to reverse a
	 * range of numbers.
	 * 
	 * @param value - the value to be mapped
	 * @param inputLowerBound - the lower bound of the value
	 * @param inputUpperBound - the upper bound of the value
	 * @param outputLowerBound - the desired lower bound of the output
	 * @param outputUpperBound - the desired upper bound of the output
	 * @return the mapped value
	 */
	private double map(double value, double inputLowerBound, double inputUpperBound, double outputLowerBound,
			double outputUpperBound) {
		return (value - inputLowerBound) * (outputUpperBound - outputLowerBound) / (inputUpperBound - inputLowerBound)
				+ outputLowerBound;
	}

	private void driveForDistance(double feet) {

		initDistance = Robot.ADL.getDistance();

		double leftCalculatedSpeed = 0.0;
		double rightCalculatedSpeed = 0.0;

		double test = 0;
		double test2 = 0;

		boolean leftWasBoosted = false;
		boolean rightWasBoosted = false;

		// setting up the supposed orientation, so that we know if it changes
		initOrientation = Robot.ADL.getOrientation();
		driving = true;

		while (driving && Robot.autoEnabled && 
				!(System.currentTimeMillis() >= targetTimeMillis)) {
			final double currentDistance = Robot.ADL.getDistance();
			final double currentOrientation = Robot.ADL.getOrientation();

			final double distanceTraveled = currentDistance - initDistance;
			double controlRatio;
			test = distanceTraveled;
			test2 = currentOrientation;

			// SKEWING CORRECTING CODE HERE
			double skew = this.skew(initOrientation, currentOrientation);
			System.out.println("Skew: " + skew);
			if (skew < 0.0) { // Boost left
				if (rightWasBoosted) { // We're skewing left because we boosted
										// the right side.
					rightCalculatedSpeed = 0; // Decrease the right speed.
					rightWasBoosted = false;
				} else {
					leftWasBoosted = true;
					System.out.println("Amount added to LCS: " + this.map(-skew, 0.0, 5.0, 0.0, this.LEFT_SKEW_CEILING));
					leftCalculatedSpeed += this.map(-skew, 0.0, 1.5, 0.0, this.LEFT_SKEW_CEILING);
				}
			} else if (skew > 0.0) { // Boost right
				if (leftWasBoosted) { // We're skewing right because we boosted
										// the left side.
					leftCalculatedSpeed = 0; // Decrease the left speed.
					leftWasBoosted = false;
				} else {
					System.out.println("Amount added to RCS: " + this.map(skew, 0.0, 5.0, 0.0, this.RIGHT_SKEW_CEILING));
					rightCalculatedSpeed += this.map(skew, 0.0, 1.5, 0.0, this.RIGHT_SKEW_CEILING);
					rightWasBoosted = true;
				}
			}

			if (leftCalculatedSpeed > this.LEFT_SKEW_CEILING) {
				leftCalculatedSpeed = this.LEFT_SKEW_CEILING;
			}
			if (rightCalculatedSpeed > this.RIGHT_SKEW_CEILING) {
				rightCalculatedSpeed = this.RIGHT_SKEW_CEILING;
			}

			if (distanceTraveled < feet - DRIVING_STRAIGHT_STOPPING_DISTANCE) {
				controlRatio = 1.0; // Full speed
			}
			// Reached the stopping distance threshold, but not at goal.
			else if (distanceTraveled >= feet - DRIVING_STRAIGHT_ERROR) {
				controlRatio = 0.0;
				driving = false;
			}
			// Reached the goal.
			else {
				final double stoppingZoneProgress = (distanceTraveled - (feet - DRIVING_STRAIGHT_STOPPING_DISTANCE))
						/ DRIVING_STRAIGHT_STOPPING_DISTANCE;

				controlRatio = 1 - stoppingZoneProgress;
				if (controlRatio < 0.5) {
					controlRatio = 0.5;
				}
			}
			// System.out.println("Init Orientation: " + initOrientation);
			// System.out.println("Current Orientation: " + currentOrientation);
			// System.out.println("Left Calculated Speed: " +
			// leftCalculatedSpeed);
			// System.out.println("Right Calculated Speed: " +
			// rightCalculatedSpeed + "\n");

			Robot.drivetrain.setLeftSpeed((leftSpeed * controlRatio) + leftCalculatedSpeed);
			Robot.drivetrain.setRightSpeed((rightSpeed * controlRatio) - rightCalculatedSpeed);
		}
		System.out.println("Done driving. Distance Traveled: " + test);
		System.out.println("Init Orientation: " + initOrientation);
		System.out.println("Current Orientation: " + test2);
	}


	public void executePath(double[] path) {
		boolean dropCube = true;
		
		for (double currentPath : path) {
			if(targetTimeMillis != System.currentTimeMillis()){
				if (currentPath < 0) {
					if (currentPath == -1) {
						turnDegrees(90);
					} else if(currentPath == -3){
						dropCube = false;
					}else if(currentPath == -4){
						Robot.winch.goUpAuto();
					} else {
						turnDegrees(270);
					}
				} else {
					driveForDistance(currentPath / 12.0);
				}
			}else{
				break;
			}
		}
		
		if(dropCube){
			Robot.intake.ejectAuto();
		}
	}
	
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (count == 0) {
			targetTimeMillis = System.currentTimeMillis()+15000;
			
			// executePath(new double[] { 249.65, -1, 24, -2, 10 });
			//Robot.winch.goUpAuto();
			//Robot.intake.ejectAuto();
			// this.turnDegrees(90);
			//this.driveForDistance(5.0);
			this.turnDegrees(270);
			/*
			 * try { this.executePath(this.decidePath()); } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}
		count++;
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
