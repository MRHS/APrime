package com.github.mrhs.aprime;

import com.github.mrhs.aprime.tasks.Task;
import com.github.mrhs.aprime.tasks.TaskResult;

public class TestTask implements Task {

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaskResult run() {
		// TODO Auto-generated method stub
		System.out.println("Running task");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(TestTask.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		
		return null;
	}

}
