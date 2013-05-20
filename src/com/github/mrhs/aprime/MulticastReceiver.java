package com.github.mrhs.aprime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class MulticastReceiver extends Thread
{
	private InetAddress address;
	private MulticastSocket socket;
	
	private List<MulticastListener> listeners = new ArrayList<MulticastListener>();
	
	public MulticastReceiver(InetAddress address)
	{
		this.address = address;
	}
	
	public void run()
	{
		try
		{
			// Create the multicast listener and join the group
			
			this.socket = new MulticastSocket(8820);
			this.socket.joinGroup(address);
			
			while (true)
			{
				// Only buffer 512 bytes, as it is the recommended packet size
				
				byte[] buffer = new byte[512];
				
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				this.socket.receive(packet);
				
				// Get the string of what was sent, trim off any null characters and spaces
				
				String data = new String(buffer).trim();
				
				// Split the message into parts
				
				String[] parts = data.split(" ");
				
				switch(parts[0])
				{
				case "JOINED":
					// Fired when nodes join the cluster
					
					for (MulticastListener listener :  this.listeners)
					{
						listener.nodeJoined(packet.getAddress());
					}
					
					break;
					
				case "PING":
					// Fired periodically by nodes looking for a node count
					
					for (MulticastListener listener :  this.listeners)
					{
						listener.ping(packet.getAddress());
					}
					
					break;
					
				case "PONG":
					// Fired in response to pings
					
					for (MulticastListener listener :  this.listeners)
					{
						listener.pong(packet.getAddress());
					}
					
					break;
					
				case "TASK":
					// All task-related  messages
					
					String taskId = parts[1];
					int status = Integer.parseInt(parts[2]);
					
					switch (status)
					{
					case 1:
						// Fired when a new task is created that needs to be handled
						
						int port = Integer.parseInt(parts[3]);

						for (MulticastListener listener :  this.listeners)
						{
							listener.newTask(taskId, packet.getAddress(), port);
						}
						
						break;
					case 2:
						// Fired when a node receives a task file successfully
						
						for (MulticastListener listener :  this.listeners)
						{
							listener.taskStarted(taskId, packet.getAddress());
						}
						
						break;
					case 3:
						// Fired when a task gives an exception while running it
						
						for (MulticastListener listener :  this.listeners)
						{
							listener.taskAborted(taskId, packet.getAddress());
						}
						
						break;
					case 4:
						// Fired when a task is successfully completed, without issues
						
						port = Integer.parseInt(parts[3]);

						for (MulticastListener listener :  this.listeners)
						{
							listener.taskFinished(taskId, packet.getAddress(), port);
						}
						
						break;
					default:
						// TODO Handle invalid task statuses
						
						System.out.println("Received unknown task status: " + status);
					}
					
					break;
					
				default:
					// TODO Handle invalid messages
					
					System.out.println("Received unknown message: " + parts[0]);
					
					break;
				}
			}
		}
		catch (IOException e)
		{
			// TODO Handle socket errors
			
			e.printStackTrace();
		}
	}
	
	public void addListener(MulticastListener listener)
	{
		this.listeners.add(listener);
	}
}
