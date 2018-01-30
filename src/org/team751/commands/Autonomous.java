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

	// The line is 188 inches away from the wall
	// Robot's length with bumper = 38.5 inches
	// Robot's width with bumper = 34.5 inches
	private static final int passLineDist = 168;

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

	// Currentlimit when driving forward is 40 at Bellarmine
	private static double currentLimit = 20;

	Timer timer = new Timer();

	// Whether the Near Switch 751 plate is left(and if false it is
	// right).

	public static boolean isNearSwitchLeft;

	public Autonomous() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
	}

	private void setUpSwitchPosition() {
		System.out.println("Gamedata getting...");

		String gameData;

		gameData = DriverStation.getInstance().getGameSpecificMessage();

		isNearSwitchLeft = (gameData.charAt(0) == 'L');

		System.out.println("gameData=" + gameData);
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
		timer.reset();
		// initDistance = Robot.ADL.getY();
		/*try {
			initOrientation = Robot.ADL.getHeading();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		timeToDrive = 15;

		setUpSwitchPosition();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
	}

	private void driveForTenFeet() {
		Robot.drivetrain.setLeftSpeed(0.75);
		Robot.drivetrain.setRightSpeed(0.75);
		
		// Wait until Distance reaches ten feet
//		while(Robot.ADL.getDistanceFeet() < 10.0) {}
//		
//		Robot.drivetrain.setLeftSpeed(0);
//		Robot.drivetrain.setRightSpeed(0);
	}
	
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// totalCurrent = Robot.drivetrain.pdp.getTotalCurrent();
		// //double time = timer.get();
		// //drive(time);
		//
		// if (Robot.crushed) {
		// end();
		// } else if (Robot.drivetrain.switch4.get()) {
		// driveForDistance(passLineDist);
		// } else if (Robot.drivetrain.switch5.get()) {
		// driveForDistance(centralDist);
		// } else if (Robot.drivetrain.switch6.get()) {
		// leftGoLeftDistance(leftFirstDist, angle, leftSecondDist);
		// } else if (Robot.drivetrain.switch7.get()) {
		// rightGoRightDistance(rightFirstDist, angle, rightSecondDist);
		// } else {
		// end(); //no auto when all switches are turned off
		// }
		//
		// leftGoLeftDistance(60,60,30);

		driveForTenFeet();
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
	//
	// protected void driveForDistance(int goal) {
	// System.out.println("Init Distance"+ initDistance);
	// System.out.println("Distance" + Robot.ADL.getY());
	// double controlRatio = 1 - (Robot.ADL.getY() - initDistance) / goal;
	//
	// if(Math.abs(Robot.ADL.getY() - initDistance) < goal){
	// Robot.drivetrain.setLeftSpeed(0.25 * controlRatio);
	// Robot.drivetrain.setRightSpeed(-0.25 * controlRatio);
	// }
	// }
	//
	// protected void leftGoLeftDistance(int firstDist, int angle, int
	// secondDist){
	// if(Math.abs(Robot.ADL.getY() - initDistance) < firstDist){
	// driveForDistance(firstDist);
	// }
	// else{
	// double finalPosition = initOrientation - angle + 360;
	// double controlRatio = 1 - Math.abs(Robot.ADL.getHeading() -
	// initOrientation)/Math.abs(Robot.ADL.getHeading() - finalPosition%360);
	//
	// System.out.println("Heading" + Robot.ADL.getHeading());
	// System.out.println("Init"+ initOrientation);
	// System.out.println("Difference" + Math.abs((Robot.ADL.getHeading() -
	// finalPosition%360)));
	//
	// if(Math.abs((Robot.ADL.getHeading() - finalPosition%360)) > maxError){
	// Robot.drivetrain.setLeftSpeed(leftSpeed * controlRatio);
	// Robot.drivetrain.setRightSpeed(-rightSpeed * controlRatio);
	// }
	// else{
	// initDistance = Robot.ADL.getY();
	// System.out.println("last init" + initDistance);
	// driveForDistance(secondDist);
	// end();
	// timeToDrive = 0;
	// }
	// }
	// }
	//
	// protected void rightGoRightDistance(int firstDist, int angle, int
	// secondDist){
	// if(Math.abs(Robot.ADL.getY() - initDistance) < firstDist){
	// driveForDistance(firstDist);
	// }
	// else{
	// double finalPosition = initOrientation - angle + 360;
	// double controlRatio = 1 - Math.abs(Robot.ADL.getHeading() -
	// initOrientation)/Math.abs(Robot.ADL.getHeading() - finalPosition%360);
	//
	// System.out.println("Heading" + Robot.ADL.getHeading());
	// System.out.println("Init"+ initOrientation);
	// System.out.println("Difference" + Math.abs((Robot.ADL.getHeading() -
	// finalPosition%360)));
	//
	// if(Math.abs((Robot.ADL.getHeading() - finalPosition%360)) > maxError){
	// Robot.drivetrain.setLeftSpeed(-leftSpeed * controlRatio);
	// Robot.drivetrain.setRightSpeed(rightSpeed * controlRatio);
	// }
	// else{
	// initDistance = Robot.ADL.getY();
	// System.out.println("last init" + initDistance);
	// driveForDistance(secondDist);
	// end();
	// timeToDrive = 0;
	// }
	// }
	// }

	protected void drive(double time) {
		System.out.println("drive" + time);
		if (time <= 2) {
			Robot.drivetrain.setLeftSpeed(leftSpeed);
			Robot.drivetrain.setRightSpeed(rightSpeed);
		} else
			end();
	}

	// methods for autonomous depending on the starting position
	protected void driveForward(double time) {
		if (time <= 5) { // to be adjusted in the field
			if (time > 1 && totalCurrent > currentLimit) {
				Robot.crushed = true;
			}
			Robot.drivetrain.setLeftSpeed(leftSpeed * 0.75);
			Robot.drivetrain.setRightSpeed(rightSpeed * 0.75);
		}
	}

	protected void centerForward(double time) {
		if (time <= 5) {
			if (time > 1 && totalCurrent > currentLimit) {
				Robot.crushed = true;
			}
			Robot.drivetrain.setLeftSpeed(leftSpeed * 0.5);
			Robot.drivetrain.setRightSpeed(rightSpeed * 0.5);
		}
	}

	protected void leftGoLeft(double time) {
		// forward
		if (time <= 1.05) {
			Robot.drivetrain.setLeftSpeed(leftSpeed);
			Robot.drivetrain.setRightSpeed(rightSpeed);
		} else if (time <= 1.33) {
			turnCW();
		} else if (time < 10) { // turn left
			if (time > 2.4 && totalCurrent > currentLimit) {
				Robot.crushed = true;
			}
			Robot.drivetrain.setLeftSpeed(leftSpeed * 0.75);
			Robot.drivetrain.setRightSpeed(rightSpeed * 0.75);
		}
	}

	// Have not calibrated
	protected void rightGoRight(double time) {
		// forward
		if (time <= 1.05) {
			Robot.drivetrain.setLeftSpeed(leftSpeed);
			Robot.drivetrain.setRightSpeed(rightSpeed);
		} else if (time <= 1.33) {
			turnCCW();
		} else if (time < 10) { // turn left
			if (time > 2.4 && totalCurrent > currentLimit) {
				Robot.crushed = true;
			}
			Robot.drivetrain.setLeftSpeed(leftSpeed * 0.75);
			Robot.drivetrain.setRightSpeed(rightSpeed * 0.75);
		}
	}

	// turn clockwise
	protected void turnCW() {
		Robot.drivetrain.setLeftSpeed(0.65);
		Robot.drivetrain.setRightSpeed(0.65);
	}

	// turn counterclockwise
	protected void turnCCW() {
		// if turnCW speed and turn CCW speed are equal they don't really turn
		// the same
		Robot.drivetrain.setLeftSpeed(-0.90);
		Robot.drivetrain.setRightSpeed(-0.95);
	}

}
