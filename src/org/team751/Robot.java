==== BASE ====
package org.team751.arduino;

import java.util.Arrays;
==== BASE ====

import edu.wpi.first.wpilibj.SerialPort;

==== BASE ====
import org.team751.arduino.ArduinoDataListener;
import org.team751.commands.Autonomous;
import org.team751.jetson.JoystickInputUDP;
import org.team751.jetson.StateSenderUDP;
import org.team751.subsystems.Drivetrain;
import org.team751.subsystems.Winch;

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
	public static final Drivetrain drivetrain = new Drivetrain();
	
	private double distance, velocity, heading;

	private double orientation;
	private long requestNumber = 0;
	private long leftPulses, rightPulses;
	private SerialPort port;

	public ArduinoDataListener() {
		distanceFeet = 0.0;
		distanceInches = 0.0;
		
		velocity = 0.0;
		heading = 0.0;

		orientation = 0.0;
		leftPulses = 0;
		rightPulses = 0;

		port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(1024);
		port.setTimeout(0.001);
	}

	public double getOrientation(){
		return orientation;
	}

	public long getLeftPulses() throws InterruptedException {
		return leftPulses;
	}

	public long getRightPulses() throws InterruptedException {
		return rightPulses;
	}

	public double getVelocity() {
		return velocity;
	}
	
	public double getDistance(){
		return distance;
	}

	public double getHeading() throws InterruptedException {
		return heading;
	}
	
	private void refreshDistance(){
		//
	}

	@Override
	public void run() {
		try {
			fetchData();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Command autonomousCommand;
    }
	
	public void disabledPeriodic() {
		printarduinoinfo();
		
//		Scheduler.getInstance().run();
//        SmartDashboard.putNumber("leftEncoder", Robot.drivetrain.leftEncoder.getDistance());
//        SmartDashboard.putNumber("rightEncoder", Robot.drivetrain.rightEncoder.getDistance());
//        SmartDashboard.putNumber("Right Encoder Rate", Robot.drivetrain.rightEncoder.getRate());
//        SmartDashboard.putNumber("Left Encoder Rate", Robot.drivetrain.rightEncoder.getRate());
//        
		
//        System.out.println("leftEncoder" + Robot.drivetrain.leftEncoder.getDistance());
//        System.out.println("RightEncoder" + -Robot.drivetrain.rightEncoder.getDistance());
//        
//        try {
//			stateSenderUDP.sendState(RobotState.DISABLED, 0);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	private void printarduinoinfo() {
		SmartDashboard.putNumber("Heading", ADL.getHeading());
		
		SmartDashboard.putNumber("Orientation", ADL.getOrientation());
		SmartDashboard.putNumber("LeftPulses", ADL.getLeftPulses());
		SmartDashboard.putNumber("RightPulses", ADL.getRightPulses());
		SmartDashboard.putNumber("Distance(inches)", ADL.getDistanceInches());
		SmartDashboard.putNumber("Distance(feet)", ADL.getDistanceFeet());
		//SmartDashboard.putNumber("X", ADL.getX());
		//SmartDashboard.putNumber("Y", ADL.getY());
		//System.out.println("Heading: " + ADL.getHeading() + ", Velocity: " + ADL.getVelocity() + ", Distance: " + ADL.getDistance());
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
    	
        if (autonomousCommand != null) autonomousCommand.start();
        crushed = false;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        System.out.println("Total Current: " + Robot.drivetrain.pdp.getTotalCurrent());
        printarduinoinfo();
        //System.out.println("Heading: " + ADL.getHeading());
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        
    }


    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	System.out.println("the constructor");
        Scheduler.getInstance().run();
        printarduinoinfo();
//        SmartDashboard.putNumber("leftEncoder", Robot.drivetrain.leftEncoder.getDistance());
//        SmartDashboard.putNumber("rightEncoder", -Robot.drivetrain.rightEncoder.getDistance());
//        System.out.println("leftEncoder" + Robot.drivetrain.leftEncoder.getDistance());
//        System.out.println("RightEncoder" + Robot.drivetrain.rightEncoder.getDistance());
        
//        System.out.println("Left Speed: " + Robot.drivetrain.leftDriveController1.getSpeed());
//        System.out.println("Right Speed: " + Robot.drivetrain.rightDriveController1.getSpeed());
       
//Current check        
//        System.out.println("Total Current: " + Robot.drivetrain.pdp.getTotalCurrent());
//        System.out.print("Left Motors: " + "Motor1: " + Robot.drivetrain.pdp.getCurrent(3) + ",");
//        System.out.print("Motor3: " + Robot.drivetrain.pdp.getCurrent(2) + ",");
//        System.out.print("Motor5: " + Robot.drivetrain.pdp.getCurrent(1) + ",");
//        System.out.print("Right Motors: " + "Motor0: " + Robot.drivetrain.pdp.getCurrent(0) + ",");
//        System.out.print("Motor2: " + Robot.drivetrain.pdp.getCurrent(13) + ",");
//        System.out.print("Motor4: " + Robot.drivetrain.pdp.getCurrent(14) + ",");
//        System.out.println();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
