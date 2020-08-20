package com.thefanta.fantachat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Database.Connect;

public class Client{
	
	private Thread send;
	private String name, address;
	private int port;
	private InetAddress ip;
	private int ID = -1;
	
	private DatagramSocket socket;
	
	public Client(String _name, String _address, int _port) {
		this.name = _name;
		this.address = _address;
		this.port = _port;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	/**
	 *  Check login
	 */
	public static Client CheckAccount(String name, String password, String address, int port) {
		Client c=null;
		try {
			PreparedStatement ps;
			ps=Connect.getConnect().prepareStatement("select * from Accounts where UserName=? and Password=?");
			ps.setString(1,name);
			ps.setString(2,password);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				c=new Client(name, address, port);
			}		
		} catch (Exception e) {
			return c=null;
		}
		return c;
	}
	
	public boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String receive() {
		byte[] data = new byte[60000];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
		socket.receive(packet);
		}catch(IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());		
		return message;
	}
	
	public void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	public void close() {
		new Thread() {
			public void run() {
				synchronized (socket) {
					socket.close();
				}
			}
		}.start();
	}
	public void setID(int ID) {
		// TODO Auto-generated method stub
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
