package org.team751.jetson;

import java.io.IOException;
import java.net.*;

public class JoystickInputUDP implements Runnable {
	private boolean isRunning = true;
	private final int port;
	private double x, y; // axes of operation
	
	public JoystickInputUDP(int port) {
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
            // form: [x, y]
            
            if (modifiedSentence.length() <= 0 || modifiedSentence.charAt(0) != '[' || modifiedSentence.charAt(modifiedSentence.length() - 1) != ']') {
                System.err.println("Malformed data packet: " + modifiedSentence);
                continue;
            }
            
            String dividedString[] = modifiedSentence.replaceAll("\\[", "").replaceAll("\\]","").split(","); // "[x, y]" -> "x", " y"
            
            // synchronization issue with getters?
            this.x = Double.parseDouble(dividedString[0]);
            this.y = Double.parseDouble(dividedString[1]);
        }
        clientSocket.close();	
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void stop() {
		this.isRunning = false;
	}
}
