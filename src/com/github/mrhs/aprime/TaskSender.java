package com.github.mrhs.aprime;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

import com.github.mrhs.aprime.tasks.Task;

public class TaskSender extends Thread
{
	private ServerSocket socket;
	private Task task;
	private int port;
	
	public boolean isTransferring = false;
	
	public TaskSender(Task task, int port)
	{
		this.task = task;
		this.port = port;
		
		try {
			this.socket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Handle issues when opening socket
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try
		{
			// GET THE TASK CLASS LOCATION
			
			URL sourceFileUrl = this.task.getClass().getProtectionDomain().getCodeSource().getLocation();
			File actualSourceFile = new File(sourceFileUrl.getPath());
			
			// GET THE SOURCE DIRECTORY
			
			File javaSourceFileDir = actualSourceFile.getParentFile();
			
			// BREAK THE PACKAGE NAME TO MATCH THE DIRECTORY FORMAT
			
			String moduleName = this.task.getClass().getPackage().getName();
			String[] modules = moduleName.split("\\.");
			
			String moduleDir = "";
			
			// BUILD THE PACKAGE NAME INTO A DIRECTORY PATH
			
			for (String module : modules)
			{
				moduleDir += module + "/";
			}
			
			// GET THE EXACT SOURCE FILE
			
			File javaSourceFile = new File(javaSourceFileDir.getAbsoluteFile() + "/src/" + moduleDir + this.task.getClass().getSimpleName() + ".java");
			
			// GET THE BYTE DATA OF THE FILE
			
			byte[] sourceFile = Files.readAllBytes(javaSourceFile.toPath());
			
			// KEEP SENDING UNTIL THE SOCKET CLOSES
			
			while (!this.socket.isClosed())
			{
				System.out.println("Waiting for socket: " + sourceFile.length);
				
				Socket receiveSocket = this.socket.accept();
				
				this.isTransferring = true;
				
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
			// TODO Handle issues when sending source file
			e.printStackTrace();
		}
	}
}
