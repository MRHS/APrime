package com.github.mrhs.aprime.tasks;

import java.io.Serializable;

public interface Task extends Serializable
{
	public int getId();
	
	public String getArgs();
	public void setArgs(String args);
	
	public TaskResult run();
}
