package com.github.mrhs.aprime;

import com.github.mrhs.aprime.tasks.Task;
import com.github.mrhs.aprime.tasks.TaskResult;
import java.math.BigInteger;
import java.util.ArrayList;

public class FindPrimesTask implements Task {

	private BigInteger start;
	private BigInteger end;
	private ArrayList<BigInteger> results;

	public FindPrimesTask() {
		this.start = BigInteger.valueOf(1000000);
		this.end = BigInteger.valueOf(10000000);
		results = new ArrayList<BigInteger>();
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaskResult run(String[] args) {
		for (BigInteger current = start.nextProbablePrime();
			 current.compareTo(end) < 1;
			 current = current.nextProbablePrime()) {
			results.add(current);
			System.out.println("Hello!");
		}
		System.out.println(results.toString());
		return null;
	}
}
