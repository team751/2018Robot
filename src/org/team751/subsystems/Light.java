package org.team751.subsystems;

import org.team751.commands.LightToggle;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Light extends Subsystem {
	private Relay lightSpike = new Relay(0, Direction.kReverse);
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LightToggle());
	}
	
	public void on(){
		System.out.println("on");
		lightSpike.setDirection(Direction.kForward);
	}
	
	public void off(){
		System.out.println("off");
		lightSpike.setDirection(Direction.kReverse);
	}
	
}
