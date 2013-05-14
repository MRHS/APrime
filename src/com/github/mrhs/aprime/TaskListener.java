package com.github.mrhs.aprime;

import com.github.mrhs.aprime.tasks.Task;

interface TaskListener
{
	void taskReceiveFinished(Task task);
}
