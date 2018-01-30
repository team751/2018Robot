==== BASE ====
package org.team751.arduino;

import java.util.Arrays;
==== BASE ====

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
==== BASE ====
	private final float WHEELRADIUS = 6.0f;
	
	
	private double distance, velocity, heading;
==== BASE ====
	private double orientation;
	private long requestNumber = 0;
	private long leftPulses, rightPulses;
	private SerialPort port;

	public ArduinoDataListener() {
		distanceFeet = 0.0;
		distanceInches = 0.0;
		
		velocity = 0.0;
		heading = 0.0;

		orientation = 0.0;
		leftPulses = 0;
		rightPulses = 0;

		port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(1024);
		port.setTimeout(0.001);
	}
==== BASE ====

	
	
	public double getOrientation(){
==== BASE ====
		return orientation;
	}

	public long getLeftPulses() throws InterruptedException {
		return leftPulses;
	}

	public long getRightPulses() throws InterruptedException {
		return rightPulses;
	}

	public double getVelocity() {
		return velocity;
	}
==== BASE ====
	
	public double getDistance(){
		return distance;
==== BASE ====
	}

	public double getHeading() throws InterruptedException {
		return heading;
	}
==== BASE ====
	
	private void refreshDistance(){
		//
==== BASE ====
	}

	@Override
	public void run() {
		try {
			fetchData();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
	}

	private void fetchData() throws InterruptedException {
		boolean dataReceived = false;
		String message;
		String[] data = null;
		long receivedNumber = 0;
		int counter = 10;
		while (!dataReceived && counter > 0) {
			port.writeString("Q-" + requestNumber);
			short waitLoop = 3;

			do {
				Thread.sleep((5 - waitLoop) * 5);
				waitLoop--;
				message = port.readString();
				if (message.startsWith(Long.toString(requestNumber))) {
					System.out.println("message:" + message);
					data = message.split("-");
					receivedNumber = Long.parseLong(data[0]);
					this.orientation = Double.parseDouble(data[1]);
					this.leftPulses = Long.parseLong(data[2]);
					this.rightPulses = Long.parseLong(data[3]);
					port.writeString("OK-" + requestNumber);
					dataReceived = true;
				}
			} while (waitLoop >= 0 && !dataReceived);
			requestNumber++;
			counter--;
		}
	}
}
