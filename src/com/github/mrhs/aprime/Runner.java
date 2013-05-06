package com.github.mrhs.aprime;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Runner
{
	public static void main(String[] args) throws Exception
	{
		MulticastSocket socket = new MulticastSocket();
		/*
		socket.joinGroup(InetAddress.getByName("239.239.13.37"));
		
		while (true)
		{
			byte[] buffer = new byte[512];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(packet);
			
			System.out.println(packet.getAddress() + ": " + new String(packet.getData()));
		}
		
		*/
		
		InetAddress IPAddress = InetAddress.getByName("229.229.13.37");
		
		String message = "Testing this";
		
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, IPAddress, 8820);
		
		socket.send(packet);
		
		System.out.println(packet.getAddress());
		
	}
}
