package com.github.mrhs.aprime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.Queue;

public class MulticastSender extends Thread
{
	private InetAddress address;
	private MulticastSocket socket;
	
	private Queue<DatagramPacket> queue = new LinkedList<DatagramPacket>();
	
	public MulticastSender(InetAddress address)
	{
		this.address = address;
	}
	
	public void run()
	{
		try
		{
			this.socket = new MulticastSocket();
			
			while (true)
			{
				if (this.queue.isEmpty())
				{
					Thread.sleep(1000);
					
					continue;
				}
				
				DatagramPacket packet = this.queue.poll();
				
				this.socket.send(packet);
			}
		}
		catch (IOException | InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendData(String data)
	{
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, this.address, 8820);
		
		this.queue.add(packet);
	}
}
