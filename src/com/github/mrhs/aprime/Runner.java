package com.github.mrhs.aprime;

import java.net.InetAddress;

import com.github.mrhs.aprime.tasks.Task;

public class Runner
{
	public static void main(String[] args) throws Exception
	{
		MulticastReceiver receiver = new MulticastReceiver(InetAddress.getByName("229.229.13.37"));
		receiver.start();
		
		MulticastSender sender = new MulticastSender(InetAddress.getByName("229.229.13.37"));
		sender.start();
		
		TaskSender taskSender = new TaskSender(new TestTask());
		taskSender.start();
		
		TaskReceiver taskReceiver = new TaskReceiver(InetAddress.getLocalHost(), 1337, "com.github.mrhs.aprime", "TestTask");
		taskReceiver.addListener(new TaskListener() {
			@Override
			public void taskReceiveFinished(Task task) {
				System.out.println(task);
			}});
		taskReceiver.start();
	}
}
