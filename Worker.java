package com.github.mrhs.aprime;

/*
 * Spencer Kocot
 * Last modified: 5/2/13
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Worker {

	//Inet address of group
	private InetAddress addr;

	//Port
	private int port;

	//Multicast socket
	private MulticastSocket socket = null;

	//Constructor
	public Worker(InetAddress addr, int port) throws IOException{
		this.addr = addr;
		this.port = port;
		socket = new MulticastSocket(port);
		socket.joinGroup(addr);
	}

	//Sends a packet
	public void sendPacket(DatagramPacket packet) throws IOException{
		if(socket == null)return;
		socket.send(packet);
	}
	
	public DatagramPacket readPacket() throws IOException{
		byte[] buf = new byte[1000];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		return packet;
	}

	//Leave multicast group
	public void leave() throws IOException{socket.leaveGroup(addr);}
}
