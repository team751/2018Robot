package src.org.team751.subsystems;

import src.org.team751.MultiSpeedController;
import src.org.team751.commands.JoystickDrive;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 */
public class Drivetrain extends Subsystem {
    public PWMVictorSPX leftDriveController1;
    public PWMVictorSPX leftDriveController2;
    public PWMVictorSPX leftDriveController3;
    
    public PWMVictorSPX rightDriveController1;
    public PWMVictorSPX rightDriveController2;
    public PWMVictorSPX rightDriveController3;
    
   // public MultiSpeedController leftSpeedController;
   // public MultiSpeedController rightSpeedController;
    
    
    /*public DigitalInput switch4 = new DigitalInput (4);
    public DigitalInput switch5 = new DigitalInput (5);
    public DigitalInput switch6 = new DigitalInput (6);
    public DigitalInput switch7= new DigitalInput (7);
    public DigitalInput switch8 = new DigitalInput (8);
    public DigitalInput[] switches = {switch4, switch5, switch6, switch7, switch8};
    public boolean[] switchesStatus = {switch4.get(), switch5.get(), switch6.get(), switch7.get(), switch8.get()};*/
    
//    public static final double WHEEL_DIAMETER = 6.0;
//    public static final double PULSE_PER_REVOLUTION = 360;
//    public static final double ENCODER_GEAR_RATIO = 1;
//    public static final double GEAR_RATIO = 1.0; //unsure
//    public static final double FUDGE_FACTOR = 1.0;
    
    public PowerDistributionPanel pdp = new PowerDistributionPanel();
    
//    public Encoder leftEncoder = new Encoder(2, 3, true, EncodingType.k4X);
//    public Encoder rightEncoder = new Encoder(0, 1, true, EncodingType.k4X);
    public boolean isDrivingAutonomously;
    
    public Drivetrain(){
//    	leftEncoder.setDistancePerPulse(Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION);
//    	rightEncoder.setDistancePerPulse(Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION);
//    	leftSpeedController = new MultiSpeedController(leftDriveController1,leftDriveController2,leftDriveController3);
//        rightSpeedController = new MultiSpeedController(rightDriveController1,rightDriveController2,rightDriveController3);
    	//2016bot 345,012 | 2017bot, 135, 024
    	leftDriveController1 = new PWMVictorSPX(1);
    	leftDriveController2 = new PWMVictorSPX(3);
    	leftDriveController3 = new PWMVictorSPX(5);
        
    	rightDriveController1 = new PWMVictorSPX(0);
    	rightDriveController2 = new PWMVictorSPX(2);
    	rightDriveController3 = new PWMVictorSPX(4);
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

