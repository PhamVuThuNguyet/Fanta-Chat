package com.thefanta.fantachat.server;

import java.net.InetAddress;

public class ServerClient {
	
	public String name;
	public InetAddress address;
	public int port;
	private final int ID;
	public int attempt = 0;
	
	public ServerClient(String _name, InetAddress _address, int _port, final int _ID) {
		this.name = _name;
		this.address = _address;
		this.port = _port;
		this.ID = _ID;
	}

	public int getID() {
		return ID;
	}
	
}
