package org.team751.arduino;

import java.util.Arrays;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
	private double distance, velocity, heading;
	private long requestNumber = 0;

	public ArduinoDataListener() {
		distance = 0.0;
		velocity = 0.0;
		heading = 0.0;
	}

	public double getDistance() {
		return distance;
	}

	public double getVelocity() {
		return velocity;
	}
	
	public double getHeading(){
		return heading;
	}

	@Override
	public void run() {
		SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(1024);
		port.setTimeout(0.001);
		
		
		while (true) {
			port.writeString("Q-" + requestNumber);
			final String message = port.readString();
			System.out.print(message);
			String[] data = message.split("-");
			long receivedNumber = Long.parseLong(data[0]);
			if(requestNumber == receivedNumber){
				this.velocity = Double.parseDouble(data[1]);
				this.distance = Double.parseDouble(data[2]);
				port.writeString("OK-" + requestNumber);
				requestNumber++;
			}
		}
		
	}

	public void stop() {
		
	}
}
