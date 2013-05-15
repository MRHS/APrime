package com.github.mrhs.aprime;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.github.mrhs.aprime.tasks.Task;

public class TaskSender extends Thread
{
	private ServerSocket socket;
	private Task task;
	
	public TaskSender(Task task)
	{
		this.task = task;
		
		try {
			this.socket = new ServerSocket(1337);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try
		{
			// GET THE BYTE DATA OF THE FILE
			
			byte[] sourceFile = Files.readAllBytes(Paths.get(this.task.getTaskLocation()));
			
			// KEEP SENDING UNTIL THE SOCKET CLOSES
			
			while (!this.socket.isClosed())
			{
				System.out.println("Waiting for socket: " + sourceFile.length);
				
				Socket receiveSocket = this.socket.accept();
				
				// CREATE THE OUTPUT STREAM THAT WILL BE USED TO TRANSFER THE FILE
				
				DataOutputStream outputStream = new DataOutputStream(receiveSocket.getOutputStream());
				
				int i = 0;
				
				// TRANSFER THE FILE IN 512-BYTE "CHUNKS"
				// TO ENSURE THAT THEY FIT IN INDIVIDUAL PACKETS
				
				for (i = 0; (i + 512) < sourceFile.length; i += 512)
				{
					outputStream.write(Arrays.copyOfRange(sourceFile, i, i + 512));
				}
				
				// SEND THE LAST "CHUNK" OF THE FILE
				// IT MAY NOT NECESSARILY BE 512 BYTES
				
				outputStream.write(Arrays.copyOfRange(sourceFile, i, sourceFile.length));
				
				// WAIT FIVE SECONDS BEFORE CLOSING THE SOCKET
				// PREVENTS RACE CONDITION WHEN RECEIVING THE FILE
				
				Thread.sleep(5000);
				
				receiveSocket.close();
				this.socket.close();
				
				System.out.println("Finished sending");
			}
		}
		catch (IOException | InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
