package org.team751.ros;

import java.io.IOException;
import java.net.*;

public class MotorControlUDP implements Runnable {
	private boolean isRunning = true;
	private MotorPacketResponder responder;
	private final int port;
	
	public static interface MotorPacketResponder {
		public void setMotorSpeed(int idx, double speed);
	}
	
	public MotorControlUDP(MotorPacketResponder responder) {
		this(responder, 9999);
	}
	
	public MotorControlUDP(MotorPacketResponder motorPacketResponder, int port) {
		this.port = port;
		this.responder = motorPacketResponder;
	}
	
	@Override
	public void run() {
        DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

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
            
            if (modifiedSentence.length() <= 0 || modifiedSentence.charAt(0) != '[' || modifiedSentence.charAt(modifiedSentence.length() - 1) != ']') {
                System.err.println("Malformed data packet: " + modifiedSentence);
                continue;
            }

            String dividedString[] = modifiedSentence.replaceAll("\\[", "").replaceAll("\\]","").split(",");

            for (int i = 0; i < dividedString.length; i++) {
                String speedString = dividedString[i];

                if (speedString.equals("null")) setMotorSpeed(i, 0);
                else setMotorSpeed(i, Double.parseDouble(speedString));
            }
        }

        clientSocket.close();	
	}
	
	public void stop() {
		this.isRunning = false;
	}
	
	private void setMotorSpeed(int id, double speed) {
		responder.setMotorSpeed(id, speed);
	}

}
