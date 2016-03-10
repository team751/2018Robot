
package org.team751;

import java.io.IOException;
import java.net.UnknownHostException;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.team751.commands.Autonomous;
import org.team751.ros.MotorControlUDP;
import org.team751.ros.RobotState;
import org.team751.ros.StateSenderUDP;
import org.team751.ros.MotorControlUDP.MotorPacketResponder;
import org.team751.subsystems.Arms;
import org.team751.subsystems.Drivetrain;
import org.team751.subsystems.Intake;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static final Drivetrain drivetrain = new Drivetrain();
	public static final Intake intake = new Intake();
	public static final Arms arms = new Arms();
	public static OI oi;
	
	public static MotorControlUDP motorControlServer;
	public static StateSenderUDP stateSenderUDP;

    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
        // instantiate the command used for the autonomous period
        autonomousCommand = new Autonomous();
                
//        motorControlServer = new MotorControlUDP(new MotorPacketResponder() {
//			@Override
//			public void setMotorSpeed(int idx, double speed) {
//				Robot.drivetrain.isDrivingAutonomously = Math.abs(speed) -.01 >= 0;
////				if (!Robot.drivetrain.isDrivingAutonomously) return;
//				switch (idx) {
//				case 0:
//					Robot.drivetrain.setLeftSpeed(-speed);
//					break;
//				case 1:
//					Robot.drivetrain.setRightSpeed(speed);
//					break;
//				case 2:
//					Robot.intake.intakeController.set(speed);
//				default:
//					break;
//				}
//			}
//		}, 9000);
        
//        Thread motorControlThread = new Thread(motorControlServer);
//        motorControlThread.start();
//        
//        try {
//			stateSenderUDP = new StateSenderUDP("10.7.51.76", 6000);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
        SmartDashboard.putNumber("leftEncoder", Robot.drivetrain.leftEncoder.getDistance());
        SmartDashboard.putNumber("rightEncoder", -Robot.drivetrain.rightEncoder.getDistance());
        
//        try {
//			stateSenderUDP.sendState(RobotState.DISABLED, 0);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
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
        
        SmartDashboard.putNumber("leftEncoder", Robot.drivetrain.leftEncoder.getDistance());
        SmartDashboard.putNumber("rightEncoder", -Robot.drivetrain.rightEncoder.getDistance() * 2);
        
//        try {
//			stateSenderUDP.sendState(RobotState.TELEOP, 0);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
