package com.github.mrhs.aprime;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender extends Thread
{
	private InetAddress address;
	private MulticastSocket socket;
	
	public MulticastSender(InetAddress address)
	{
		this.address = address;
	}
	
	public void run()
	{
		try
		{
			this.socket = new MulticastSocket();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
