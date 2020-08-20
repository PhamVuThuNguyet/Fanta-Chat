package com.thefanta.fantachat.server;

public class ServerMain {
	
	private int port;
	
	public ServerMain(int _port) {
		this.port= _port;
		new Server(port);
	}
	
	public static void main(String[] args) {
		int port;
		if(args.length!=1) {
			return;
		}
		port = Integer.parseInt(args[0]);
		new ServerMain(port);
	}
	
}
