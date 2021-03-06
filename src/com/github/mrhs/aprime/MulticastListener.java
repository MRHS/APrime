package com.github.mrhs.aprime;

import java.net.InetAddress;

interface MulticastListener
{
	void nodeJoined(InetAddress address);
	//void nodeLeft(InetAddress address);
	
	void newTask(String id, InetAddress address, int port);
	void taskStarted(String id, InetAddress address);
	void taskAborted(String id, InetAddress address);
	void taskFinished(String id, InetAddress address, int port);
	
	void ping(InetAddress address);
	void pong(InetAddress address);
}
