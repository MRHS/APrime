package com.github.mrhs.aprime;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.github.mrhs.aprime.tasks.Task;

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
			}
		});
		receiver.start();
		
		Thread.sleep(500);
		
		sender.start();
		
		sender.sendData("JOINED");
		
		TaskSender taskSender = new TaskSender(new TestTask());
		taskSender.start();
		
		TaskReceiver taskReceiver = new TaskReceiver(InetAddress.getLocalHost(), 1337, "com.github.mrhs.aprime", "TestTask");
		taskReceiver.addListener(new TaskListener() {
			@Override
			public void taskReceiveFinished(Task task) {
				tasks.add(task);
				
				System.out.println("Added task to task queue");
			}});
		taskReceiver.start();
		
		while (true)
		{
			if (tasks.isEmpty())
			{
				Thread.sleep(1000);
				continue;
			}
			
			Task task = tasks.poll();
			
			System.out.println(task);
		}
	}
}
