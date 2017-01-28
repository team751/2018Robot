package org.team751.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class RightWheels extends PIDSubsystem {
    public Encoder rightEncoder = new Encoder(2, 3, true, EncodingType.k2X); // port stuff, true means reported forward is always true forward
	public VictorSP rightDriveController1 = new VictorSP(3);
    public VictorSP rightDriveController2 = new VictorSP(4);
    public VictorSP rightDriveController3 = new VictorSP(5);
    
	public RightWheels() {
		super(Drivetrain.kP, Drivetrain.kI, Drivetrain.kD);
		rightEncoder.setMinRate(10); // 10 rpm min
		rightEncoder.setDistancePerPulse(8); // 8 inches circumference for now
		rightEncoder.setSamplesToAverage(7); // hopefully 7 sample averages work fine
	}

	@Override
	protected double returnPIDInput() {
		return rightEncoder.getRate();
	}

	@Override
	protected void usePIDOutput(double output) {
		System.out.println("right output: " + output);
		output/=Drivetrain.MAX_RPM; // scaling step
		rightDriveController1.set(output);
		rightDriveController2.set(output);
		rightDriveController3.set(output);
	}

	@Override
	protected void initDefaultCommand() {
		
	}

}
