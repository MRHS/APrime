package com.github.mrhs.aprime;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.github.mrhs.aprime.tasks.Task;

public class TaskReceiver extends Thread
{
	private InetAddress address;
	private int port;
	private Socket socket;
	
	private String packageName;
	private String className;
	
	private List<TaskListener> listeners = new ArrayList<TaskListener>();
	
	public TaskReceiver(InetAddress address, int port, String packageName, String className)
	{
		this.address = address;
		this.port = port;
		this.packageName = packageName;
		this.className = className;
	}
	
	public void run()
	{
		try
		{
			this.socket = new Socket(this.address, this.port);
			
			InputStream reader = this.socket.getInputStream();
			PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
			
			File file = new File(this.className + ".java");
			FileOutputStream fileOutput = new FileOutputStream(file);
			BufferedOutputStream buffOut = new BufferedOutputStream(fileOutput);
			
			System.out.println("Stream opened");
			
			while (!writer.checkError())
			{
				byte[] buffer = new byte[512];
				
				reader.read(buffer);
				
				for (int i = 0; i < buffer.length; i++)
				{
					// Only write non-null bytes
					
					if (buffer[i] != 0)
					{
						buffOut.write(buffer[i]);
					}
				}
				
				writer.println("Test for close");
				System.out.println("Received buffer: " + writer.checkError());
			}
			
			buffOut.close();
			fileOutput.close();
			
			System.out.println("Stream closed");
			
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			compiler.run(null, null, null, file.getPath());
			
			File dir = file.getCanonicalFile().getParentFile();
			
			URLClassLoader classLoader = new URLClassLoader(new URL[] { dir.toURI().toURL() });
			
			Class<?> cls = Class.forName(this.packageName + "." + this.className, true, classLoader);
			
			Task task = (Task) cls.newInstance();
			
			this.fireReceiveFinished(task);
		}
		catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addListener(TaskListener listener)
	{
		this.listeners.add(listener);
	}
	
	private void fireReceiveFinished(Task task)
	{
		for (TaskListener listener : this.listeners)
		{
			listener.taskReceiveFinished(task);
		}
	}
}
