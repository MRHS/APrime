package com.github.mrhs.aprime.tasks;

import java.io.Serializable;

public interface Task extends Serializable
{
	public int getId();
	public TaskResult run(String[] args);
}
