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
			// CREATE THE MULTICAST SOCKET
			
			this.socket = new MulticastSocket();
			
			while (true)
			{
				// CHECK IF THERE ARE ANY MESSAGES TO SEND
				
				if (this.queue.isEmpty())
				{
					// IF THERE ARE NO MESSAGES, LIMIT THE CHECK TO 1 SECOND
					
					Thread.sleep(1000);
					
					continue;
				}
				
				// POP THE FIRST MESSAGE AND SEND IT
				
				DatagramPacket packet = this.queue.poll();
				
				this.socket.send(packet);
			}
		}
		catch (IOException | InterruptedException e)
		{
			// TODO HANDLE SOCKET AND SENDING ERRORS
			
			e.printStackTrace();
		}
	}
	
	public void sendData(String data)
	{
		// CREATE A PACKET CONTAINING THE STRING DATA THAT IS AIMED FOR THE MULTICAST SOCKET
		
		DatagramPacket packet = new DatagramPacket(data.getBytes(), data.getBytes().length, this.address, 8820);
		
		// QUEUE THE MESSAGE
		
		this.queue.add(packet);
	}
}
