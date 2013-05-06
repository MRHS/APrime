package com.github.mrhs.aprime;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Runner
{
	public static void main(String[] args) throws Exception
	{
		MulticastReceiver receiver = new MulticastReceiver(InetAddress.getByName("229.229.13.37"));
		receiver.start();
		
		MulticastSender sender = new MulticastSender(InetAddress.getByName("229.229.13.37"));
		sender.start();
	}
}
