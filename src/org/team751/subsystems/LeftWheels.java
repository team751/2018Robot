package org.team751.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class LeftWheels extends PIDSubsystem {
	public Encoder leftEncoder = new Encoder(0, 1);
	VictorSP leftDriveController1 = new VictorSP(0);
	VictorSP leftDriveController2 = new VictorSP(1);
    VictorSP leftDriveController3 = new VictorSP(2);
    
	public LeftWheels() {
		super(Drivetrain.kP, Drivetrain.kI, Drivetrain.kD);
		leftEncoder.setMinRate(10); // 10 rpm min
		leftEncoder.setDistancePerPulse(8); // 8 inches circumference for now
		leftEncoder.setSamplesToAverage(7); // hopefully 7 sample averages work fine
	}

	@Override
	protected double returnPIDInput() {
		return leftEncoder.getRate();
	}

	@Override
	protected void usePIDOutput(double output) {
		System.out.println("Left output: " + output);
		output/=Drivetrain.MAX_RPM; // scaling step
	    leftDriveController1.set(output);
	    leftDriveController2.set(output);
	    leftDriveController3.set(output);
	}

	@Override
	protected void initDefaultCommand() {
	}
}
