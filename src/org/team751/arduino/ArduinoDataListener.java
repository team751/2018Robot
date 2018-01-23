package org.team751.arduino;

import java.util.Arrays;
import java.lang.Math;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
	private final double WHEELRADIUS = 6.0;
	
	private double distanceFeet, distanceInches, velocity, heading;
	private double orientation;
	private long requestNumber = 0;
	private long leftPulses, rightPulses;
	private boolean stopSent = false;

	public ArduinoDataListener() {
		distanceFeet = 0.0;
		distanceInches = 0.0;
		
		velocity = 0.0;
		heading = 0.0;
		
		orientation = 0.0;
		leftPulses = 0;
		rightPulses = 0;
	}
	
	public double getOrientation(){
		return orientation;
	}
	
	public long getLeftPulses(){
		return leftPulses;
	}
	
	public long getRightPulses(){
		return rightPulses;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public double getDistanceInches(){
		return distanceInches;
	}
	
	public double getDistanceFeet(){
		return distanceFeet;
	}
	
	public double getHeading(){
		return heading;
	}
	
	private void refreshDistance(){
		distanceInches = (Math.sqrt(this.WHEELRADIUS)*Math.PI)*
								  this.leftPulses;
		
		this.distanceFeet = this.distanceInches/12;
	}

	@Override
	public void run() {
		SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(1024);
		port.setTimeout(0.001);
		
		
		while (!stopSent) {
			port.writeString("Q-" + requestNumber);
			final String message = port.readString();
			System.out.print(message);
			String[] data = message.split("-");
			long receivedNumber = Long.parseLong(data[0]);
			if(requestNumber == receivedNumber){
				/*this.velocity = Double.parseDouble(data[1]);
				this.distance = Double.parseDouble(data[2]);*/
				this.orientation = Double.parseDouble(data[1]);
				this.leftPulses = Long.parseLong(data[2]);
				this.rightPulses = Long.parseLong(data[3]);
				port.writeString("OK-" + requestNumber);
				requestNumber++;
				
				refreshDistance();
			}
		}
		
		stopSent = false;
	}

	public void stop() {
		stopSent = true;
	}
}
