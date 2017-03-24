package org.team751.arduino;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
	private boolean isRunning = true;
	private final int port;
	private double heading; // axes of operation
	private double distance;
	private double velocity;
	
	public ArduinoDataListener(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(32);
		port.setTimeout(0.01);
		while(true){
			final String message = port.readString();
			//final String[] parts = message.split("[");
			System.out.print(message);
			
			//String[] data = parts[].split(",");
			//System.out.println(Arrays.toString(data));
			//this.heading = Double.parseDouble(data[0]);
            //this.velocity = Double.parseDouble(data[1]);
            //this.distance = Double.parseDouble(data[2]);
		}
	}
	
	
	/*
	@Override
	public void run() {
		byte[] buffer = new byte[4];
		I2C serialPort = new I2C(I2C.Port.kOnboard, 0b0101011_1);
		while(true){
			serialPort.read(0b01010110, 4, buffer);
			System.out.println((char) buffer[0] + (char) buffer[1] + (char) buffer[2]);
		}
		//serialPort.free();
	}
	*/
	
	/*
	@Override
	public void run() {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		System.out.println("Is going");

		if (clientSocket == null) return;
		System.out.println("clientSocket is not null");
		
        byte[] receiveData = new byte[1024];

        while (clientSocket.isBound() && isRunning) {
        	System.out.println("waiting for packet");
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

            String modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            System.out.println("Got " + modifiedSentence);
            // form: [heading,velocity,distance]
            modifiedSentence = modifiedSentence.replaceAll("\\[|\\]", ""); // remove the hard brackets on either side to prevent substrings because easier
            String[] stuff =  modifiedSentence.split(",");
            this.heading = Double.parseDouble(stuff[0]);
            this.velocity = Double.parseDouble(stuff[1]);
            this.distance = Double.parseDouble(stuff[2]);
        }
        clientSocket.close();	
	}
	*/
	public double getHeading() {
		return heading;
	}
	
	public double getDistance() {
		return distance;
	}

	public double getVelocity() {
		return velocity;
	}

	public void stop() {
		this.isRunning = false;
	}
}
