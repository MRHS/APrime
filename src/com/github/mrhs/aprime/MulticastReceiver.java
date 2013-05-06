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
			this.socket = new MulticastSocket();
			this.socket.joinGroup(address);
			
			while (true)
			{
				byte[] buffer = new byte[512];
				
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				this.socket.receive(packet);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
