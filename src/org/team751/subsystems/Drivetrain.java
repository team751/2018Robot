package org.team751.subsystems;

import org.team751.commands.JoystickDrive;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 */
public class Drivetrain extends Subsystem {
<<<<<<< HEAD
    public VictorSP leftDriveController1 = new VictorSP(3);
    public VictorSP leftDriveController2 = new VictorSP(4);
=======

    public VictorSP leftDriveController1 = new VictorSP(1);
    public VictorSP leftDriveController2 = new VictorSP(3);
>>>>>>> b211dd129c62d33b192db035c12da7f5b7e5f334
    public VictorSP leftDriveController3 = new VictorSP(5);
    
    public VictorSP rightDriveController1 = new VictorSP(0);
    public VictorSP rightDriveController2 = new VictorSP(1);
    public VictorSP rightDriveController3 = new VictorSP(2);
    
	  public DigitalInput switch4 = new DigitalInput (4);
	  public DigitalInput switch5 = new DigitalInput (5);
	  public DigitalInput switch6 = new DigitalInput (6);
	  public DigitalInput switch7= new DigitalInput (7);
	  public DigitalInput switch8 = new DigitalInput (8);
	  public DigitalInput[] switches = {switch4, switch5, switch6, switch7, switch8};
	  public boolean[] switchesStatus = {switch4.get(), switch5.get(), switch6.get(), switch7.get(), switch8.get()};
    
//    public static final double WHEEL_DIAMETER = 6.0;
//    public static final double PULSE_PER_REVOLUTION = 360;
//    public static final double ENCODER_GEAR_RATIO = 1;
//    public static final double GEAR_RATIO = 1.0; //unsure
//    public static final double FUDGE_FACTOR = 1.0;
    
    public PowerDistributionPanel pdp = new PowerDistributionPanel();
//    public boolean isDrivingAutonomously = false;
    
    public Drivetrain(){
//    	leftEncoder.setDistancePerPulse(Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION);
//    	rightEncoder.setDistancePerPulse(Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION);
    }
    
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new JoystickDrive());
    }
    
    public void setLeftSpeed(double speed) {
    	leftDriveController1.set(speed);
    	leftDriveController2.set(speed);
    	leftDriveController3.set(speed);
    }
    
    public void setRightSpeed(double speed) {
    	rightDriveController1.set(speed);
    	rightDriveController2.set(speed);
    	rightDriveController3.set(speed);
    }
}

