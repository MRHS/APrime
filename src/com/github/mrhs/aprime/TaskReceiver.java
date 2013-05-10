package com.github.mrhs.aprime;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TaskReceiver extends Thread
{
	private InetAddress address;
	private int port;
	private Socket socket;
	
	public TaskReceiver(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}
	
	public void run()
	{
		try
		{
			this.socket = new Socket(this.address, this.port);
			
			InputStream reader = this.socket.getInputStream();
			PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
			
			System.out.println("Stream opened");
			
			while (!writer.checkError())
			{
				byte[] buffer = new byte[512];
				
				reader.read(buffer);
				
				writer.println("Test for close");
				System.out.println("Received buffer: " + writer.checkError());
			}
			
			System.out.println("Stream closed");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
