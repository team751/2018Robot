package org.team751.arduino;

import java.util.Arrays;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
<<<<<<< HEAD
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
=======
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
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		System.out.println("Is going");

		if (clientSocket == null) return;
		
        byte[] receiveData = new byte[1024];

        while (clientSocket.isBound() && isRunning) {
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
>>>>>>> b211dd129c62d33b192db035c12da7f5b7e5f334
	}
	
	public double getHeading(){
		return heading;
	}
	
	public double getDistance() {
		return distance;
	}

	public double getVelocity() {
		return velocity;
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
