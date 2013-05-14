package com.github.mrhs.aprime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver extends Thread
{
	private InetAddress address;
	private MulticastSocket socket;
	
	public MulticastReceiver(InetAddress address)
	{
		this.address = address;
	}
	
	public void run()
	{
		try
		{
			// CREATE THE MULTICAST SOCKET AND LISTEN ON THE GIVEN ADDRESS
			
			this.socket = new MulticastSocket(8820);
			this.socket.joinGroup(address);
			
			while (true)
			{
				// ONLY BUFFER 512 BYTES, AS IT IS THE RECOMMENDED PACKET SIZE
				
				byte[] buffer = new byte[512];
				
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				this.socket.receive(packet);
				
				// TODO PROCESS THE RECEIVED DATA AND FIRE ASSOCIATED EVENTS
			}
		}
		catch (IOException e)
		{
			// TODO HANDLE SOCKET ERRORS
			
			e.printStackTrace();
		}
	}
}
