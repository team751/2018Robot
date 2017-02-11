
package org.team751;

import java.net.UnknownHostException;

import org.team751.commands.Autonomous;
import org.team751.commands.GearPlacement;
import org.team751.commands.LightToggle;
import org.team751.jetson.JoystickInputUDP;
import org.team751.jetson.StateSenderUDP;
import org.team751.subsystems.Drivetrain;
import org.team751.subsystems.Light;
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
	public static final Light cameraLight = new Light();
	public static OI oi;
	
	public static JoystickInputUDP autonomousJoystickSimulator;
	public static StateSenderUDP stateSenderUDP;

    Command autonomousCommand;

		/*Encoder Distance Constants*/
        public static final double wheelDiameter = 6.25; //wheel 6 plus something around it
        public static final double pulsePerRevolution = 360; //not certain if this is right
//      public static final double encoderGearRatio = 3; //not sure what this is
//      public static final double gearRatio = 64.0/20.0;
//      public static final double Fudgefactor = 1.0;
	final double distancePerPulse = Math.PI * wheelDiameter / pulsePerRevolution;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
		oi.autoButton.whenPressed(new GearPlacement());
		oi.lightButton.whenPressed(new LightToggle());
	    
	// set distancePerpulse for the encoders
		drivetrain.leftEncoder.setDistancePerPulse(distancePerPulse);
		drivetrain.rightEncoder.setDistancePerPulse(distancePerPulse);
	    
        // instantiate the command used for the autonomous period
        autonomousCommand = new Autonomous();    
        autonomousJoystickSimulator = new JoystickInputUDP(6001);
        Thread motorControlThread = new Thread(autonomousJoystickSimulator);
        motorControlThread.start();
                
        try {
			stateSenderUDP = new StateSenderUDP("10.7.51.76", 6000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
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
