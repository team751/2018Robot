
package org.team751;

import java.net.UnknownHostException;

import org.team751.arduino.ArduinoDataListener;
import org.team751.commands.Autonomous;
import org.team751.commands.GearPlacement;
import org.team751.jetson.JoystickInputUDP;
import org.team751.jetson.StateSenderUDP;
import org.team751.subsystems.Drivetrain;
import org.team751.subsystems.Winch;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static final Drivetrain drivetrain = new Drivetrain();
	public static final Winch winch = new Winch();
	public static OI oi;
	
	public static JoystickInputUDP autonomousJoystickSimulator;
	public static StateSenderUDP stateSenderUDP;
	public static boolean crushed = false;
	public static ArduinoDataListener ADL = new ArduinoDataListener(7776);

    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
		//oi.autoButton.whenPressed(new GearPlacement()); no gear placement autonomous
        // instantiate the command used for the autonomous period
        autonomousCommand = new Autonomous();    
        Thread motorControlThread = new Thread(autonomousJoystickSimulator);
        motorControlThread.start();
        
        Thread imuThread = new Thread();
        
        try {
			stateSenderUDP = new StateSenderUDP("10.7.51.76", 6000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
	
	public void disabledPeriodic() {
//		Scheduler.getInstance().run();
//        try {
//			stateSenderUDP.sendState(RobotState.DISABLED, 0);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
    	crushed = false;
        if (autonomousCommand != null) autonomousCommand.start();
        crushed = false;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        System.out.println("Total Current: " + Robot.drivetrain.pdp.getTotalCurrent());
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
        Scheduler.getInstance().run();
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
