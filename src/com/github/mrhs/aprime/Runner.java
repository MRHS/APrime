package com.github.mrhs.aprime;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.github.mrhs.aprime.tasks.Task;
import com.github.mrhs.aprime.tasks.TaskResult;

public class Runner
{
	public static void main(String[] args) throws Exception
	{
		final List<InetAddress> nodes = new ArrayList<InetAddress>();
		final Queue<Task> tasks = new LinkedList<Task>();
		
		final MulticastSender sender = new MulticastSender(InetAddress.getByName("229.229.13.37"));
		
		MulticastReceiver receiver = new MulticastReceiver(InetAddress.getByName("229.229.13.37"));
		receiver.addListener(new MulticastListener() {
			@Override
			public void nodeJoined(InetAddress address) {
				System.out.println(address.getHostName() + " joined");
				
				nodes.add(address);
				
				sender.sendData("PING");
			}

			@Override
			public void ping(InetAddress address) {
				nodes.clear();
				
				sender.sendData("PONG");
			}

			@Override
			public void pong(InetAddress address) {
				nodes.add(address);
				
				System.out.println("Nodes in cluster: " + nodes.size());
			}

			@Override
			public void newTask(String id, InetAddress address, int port) {
				System.out.println("New task [" + id + "] started at " + address.getHostName() + ":" + port);
				
				if (tasks.size() < 3)
				{
					TaskReceiver taskReceiver = new TaskReceiver(address, port);
					taskReceiver.addListener(new TaskListener() {
						@Override
						public void taskReceiveFinished(Task task) {
							tasks.add(task);
							
							sender.sendData("TASK " + task.getId() + " 2");
							
							System.out.println("Added task to task queue");
						}});
					taskReceiver.start();
				}
			}

			@Override
			public void taskStarted(String id, InetAddress address) {
				System.out.println("Task [" + id + "] started at " + address.getHostName());
			}

			@Override
			public void taskAborted(String id, InetAddress address) {
				System.out.println("Task [" + id + "] aborted at " + address.getHostName());
			}

			@Override
			public void taskFinished(String id, InetAddress address, int port) {
				System.out.println("Task [" + id + "] finished at " + address.getHostName() + ":" + port);
			}
		});
		receiver.start();
		
		Thread.sleep(500);
		
		sender.start();
		
		sender.sendData("JOINED");
		
		Thread.sleep(500);
		
		Task testTask = new FindPrimesTask(new BigInteger("10"), new BigInteger("9001"));
		
		TaskSender taskSender = new TaskSender(testTask, 5876);
		taskSender.start();
		
		while (!taskSender.isTransferring)
		{
			sender.sendData("TASK " + testTask.getId() + " 1 5876");
			
			Thread.sleep(5000);
		}
		
		while (true)
		{
			if (tasks.isEmpty())
			{
				Thread.sleep(1000);
				continue;
			}
			
			Task task = tasks.poll();
			
			try
			{
				TaskResult result = task.run();
				
				sender.sendData("TASK " + task.getId() + " 4 1234");
			}
			catch (Exception e)
			{
				sender.sendData("TASK " + task.getId() + " 3");
			}
		}
	}
}
