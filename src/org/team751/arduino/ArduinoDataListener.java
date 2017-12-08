package org.team751.arduino;

import java.util.Arrays;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
	private double x, y, heading;

	public ArduinoDataListener() {
		this.x = 0.0;
		this.y = 0.0;
		this.heading = 0.0;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double getHeading(){
		return heading;
	}

	@Override
	public void run() {
		/*
		SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(1024);
		port.setTimeout(0.01);
		while (true) {
			final String message = port.readString();
			System.out.print(message);
			String[] lines = message.split("\\n");
			System.out.println(Arrays.toString(lines));
			
			final int lastIndex = message.lastIndexOf('\n');
			final int secondLastIndex = message.lastIndexOf('\n', lastIndex - 1);
			String dataLine = message.substring(secondLastIndex, lastIndex);
			String[] data = dataLine.split(",");
			
			this.x = Double.parseDouble(data[0]);
			this.y = Double.parseDouble(data[1]);
			this.heading = Double.parseDouble(data[2]);
			
		}
		*/
	}

	public void stop() {
		
	}
}
