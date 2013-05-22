package com.github.mrhs.aprime.tasks;

import java.io.Serializable;

public interface Task extends Serializable
{
	//Returns the id of the task
	public int getId();
	
	//The task run method
	public TaskResult run();
}
