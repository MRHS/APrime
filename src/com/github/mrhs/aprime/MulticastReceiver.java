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
				
				String data = new String(buffer).trim();
				
				String[] parts = data.split(" ");
				
				switch(parts[0])
				{
				case "JOINED":
					for (MulticastListener listener :  this.listeners)
					{
						listener.nodeJoined(packet.getAddress());
					}
					
					break;
					
				case "PING":
					for (MulticastListener listener :  this.listeners)
					{
						listener.ping(packet.getAddress());
					}
					
					break;
					
				case "PONG":
					for (MulticastListener listener :  this.listeners)
					{
						listener.pong(packet.getAddress());
					}
					
					break;
					
				case "TASK":
					String taskId = parts[1];
					int status = Integer.parseInt(parts[2]);
					
					switch (status)
					{
					case 1:
						int port = Integer.parseInt(parts[3]);

						for (MulticastListener listener :  this.listeners)
						{
							listener.newTask(taskId, packet.getAddress(), port);
						}
						
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					default:
						System.out.println("Received unknown task status: " + status);
					}
					
					break;
					
				default:
					System.out.println("Received unknown message: " + parts[0]);
					
					break;
				}
			}
		}
		catch (IOException e)
		{
			// TODO HANDLE SOCKET ERRORS
			
			e.printStackTrace();
		}
	}
	
	public void addListener(MulticastListener listener)
	{
		this.listeners.add(listener);
	}
}
