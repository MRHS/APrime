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
			// TRY TO CONNECT TO THE NODE ON THE GIVEN PORT
			
			this.socket = new Socket(this.address, this.port);
			
			// GET THE STREAM USED FOR READING THE INCOMING FILE
			
			InputStream reader = this.socket.getInputStream();
			
			// CREATE A WRITER WHICH WILL BE USED TO TEST IF THE SOCKET IS STILL OPEN
			
			PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
			
			// CREATE THE FILE WITH THE PROVIDED CLASS NAME IN THE CURRENT DIRECTORY
			
			File file = new File(this.className + ".java");
			
			// INITIALIZE THE BUFFERS USED FOR WRITING THE DATA TO THE FILE
			
			FileOutputStream fileOutput = new FileOutputStream(file);
			BufferedOutputStream buffOut = new BufferedOutputStream(fileOutput);
			
			System.out.println("Stream opened");
			
			// CONTINUOUSLY LOOP UNTIL THE SOCKET IS CLOSED
			// THE WRITER WILL GIVE AN ERROR WHEN IT CAN NO LONGER WRITE TO THE SOCKET
			// THERE IS NO OTHER EFFICIENT WAY OF DETECTING WHEN IT HAS CLOSED
			
			while (!writer.checkError())
			{
				// CREATE A 512-BYTE BUFFER THAT WILL RECEIVE THE FILE "CHUNKS"
				
				byte[] buffer = new byte[512];
				
				reader.read(buffer);
				
				// CHECK ALL OF THE INCOMING BYTES AND WRITE THEM TO THE FILE
				// TODO MAKE THIS MORE EFFICIENT
				
				for (int i = 0; i < buffer.length; i++)
				{
					// CHECK EACH BYTE TO MAKE SURE IT IS NOT NULL
					// JAVA FILES SHOULD NOT CONTAIN NULL BYTES, AND THEY MARK THE END OF THE FILE
					
					if (buffer[i] != 0)
					{
						// WRITE THE SINGLE VALID BYTE TO THE FILE
						
						buffOut.write(buffer[i]);
					}
				}
				
				// WRITE TO THE SOCKET TO TEST IF IT HAS BEEN CLOSED OR NOT
				
				writer.println("Test for close");
				System.out.println("Received buffer: " + writer.checkError());
			}
			
			// CLOSE THE FILE STREAMS AFTER IT HAS BEEN RECEIVED
			
			buffOut.close();
			fileOutput.close();
			
			System.out.println("Stream closed");
			
			// INITIALIZE THE JAVA COMPILER AND COMPILE THE DOWNLOADED FILE
			
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			compiler.run(null, null, null, file.getPath());
			
			// GET THE DIRECTORY CONTAINING THE FILE
			
			File dir = file.getCanonicalFile().getParentFile();
			
			// CREATE A NEW CLASS LOADER FOR THE DIRECTORY THAT CONTAINS THE FILE
			
			URLClassLoader classLoader = new URLClassLoader(new URL[] { dir.toURI().toURL() });
			
			// GET THE NEW CLASS BASED ON THE FULL NAME OF THE CLASS
			// THIS MUST INCLUDE THE FULL PACKAGE NAME AND CLASS NAME
			// AND THIS ONLY GRABS CLASSES WHICH HAVE BEEN DOWNLOADED
			
			Class<?> cls = Class.forName(this.packageName + "." + this.className, true, classLoader);
			
			// CREATE THE NEW INSTANCE OF THE TASK
			
			Task task = (Task) cls.newInstance();
			
			// FIRE THE taskReceiveFinished HANDLERS WITH THE NEW TASK
			
			this.fireReceiveFinished(task);
		}
		catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			// TODO HANDLE ERRORS WHEN DOWNLOADING OR COMPILING
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a TaskListener that will be used when firing events.
	 * 
	 * @param listener
	 *        The TaskListener to be registered.
	 */
	
	public void addListener(TaskListener listener)
	{
		// ADD THE GIVEN LISTENER TO THE LIST THAT WILL BE USED FOR FIRING THEM
		
		this.listeners.add(listener);
	}
	
	/**
	 * Fires the taskReceiveFinished() method on all registered handlers.
	 * Provides the task that was successfully compiled and allows for it to
	 * be registered internally.
	 * 
	 * @param task
	 * 		  The task that was successfully downloaded and compiled.
	 */
	
	private void fireReceiveFinished(Task task)
	{
		// FIRE THE METHOD ON EACH LISTENER
		
		for (TaskListener listener : this.listeners)
		{
			listener.taskReceiveFinished(task);
		}
	}
}
