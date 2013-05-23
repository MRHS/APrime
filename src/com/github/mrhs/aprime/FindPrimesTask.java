package com.github.mrhs.aprime;

import com.github.mrhs.aprime.tasks.Task;
import com.github.mrhs.aprime.tasks.TaskResult;
import java.math.BigInteger;
import java.util.ArrayList;

public class FindPrimesTask implements Task {

	private BigInteger start;
	private BigInteger end;
	private ArrayList<BigInteger> results;

	public FindPrimesTask()
	{
		results = new ArrayList<BigInteger>();
	}
	
	public FindPrimesTask(BigInteger start, BigInteger end) {
		this.start = start;
		this.end = end;
		
		results = new ArrayList<BigInteger>();
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaskResult run() {
		for (BigInteger current = start.nextProbablePrime();
			 current.compareTo(end) < 1;
			 current = current.nextProbablePrime()) {
			results.add(current);
		}
		System.out.println(results.toString());
		return null;
	}

	@Override
	public String getArgs() {
		return this.start.toString() + " " + this.end.toString();
	}

	@Override
	public void setArgs(String args) {
		String[] parts = args.split(" ");
		
		this.start = new BigInteger(parts[0]);
		this.end = new BigInteger(parts[1]);
	}
}
