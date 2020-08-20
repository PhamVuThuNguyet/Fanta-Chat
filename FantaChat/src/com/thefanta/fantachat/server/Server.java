package com.thefanta.fantachat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import Database.Connect;

public class Server implements Runnable{

	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private List<Integer> clientResponse = new ArrayList<Integer>();
	
	private int port;
	private DatagramSocket socket;
	private boolean running = false;
	private Thread run, manage, send, receive;
	
	private final int MAX_ATTEMPS = 50;
	private boolean raw = false;
	private Scanner scanner;
	
	public Server(int _port) {
		this.port=_port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run = new Thread(this, "Server");
		run.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		running = true;
		System.out.println("Server started on port "+port);
		manageClients();
		receive();
		scanner = new Scanner(System.in);
		while(running) {
			String text = scanner.nextLine();
			if(!text.startsWith("/")) {
				sendToAll("/m/Server: " + text + "/e/");
				continue;
			}
			text = text.substring(1);
			if(text.equals("raw")) {
				if(raw) {
					System.out.println("Raw mode off");
				}else {
					System.out.println("Raw mode on");
				}
				raw = !raw;
			}else if(text.equals("clients")) {
				System.out.println("Clients: ");
				System.out.println("==========");
				for(int i = 0; i < clients.size(); i++) {
					ServerClient c = clients.get(i);					
					System.out.println(c.name.trim()+"("+c.getID()+"):"+c.address.toString()+":"+c.port);
				}
				System.out.println("==========");
			}else if(text.startsWith("kick")) {
				String name = text.split(" ")[1];
				int id = -1;
				boolean number = false;
				try {
					id = Integer.parseInt(name);
					number = true;
				}catch(NumberFormatException e) {
					number = false;
				}
				if(number) {
					boolean exists = false;
					for(int i =0; i < clients.size(); i++) {
						if(clients.get(i).getID() == id) {
							exists = true;
							break;
						}
					}
					if(exists) {
						ServerClient c = null;
						for(int i =0; i < clients.size(); i++) {
							if(clients.get(i).getID() == id) {
								c = clients.get(i);
								disconnect(id, true);
								String messageKick = "/k/" + "You have been kicked by Server!" + "/e/";
								send(messageKick.getBytes(), c.address , c.port);
							}
						}					
					}else {
						System.out.println("Client "+ id + " doesn't exist! Check ID number.");
					}
				}else {
					for(int i = 0; i < clients.size(); i++) {
						ServerClient c = clients.get(i);
						if(name.equals(c.name)) {
							disconnect(c.getID(), true);
							String messageKick = "/k/" + "You have been kicked by Server!" + "/e/";
							send(messageKick.getBytes(), c.address , c.port);
						}
					}
				}
			}else if(text.equals("quit")) {
				quit();
			}else if(text.equals("help")) {
				printHelp();
			}
			else {
				System.out.println("Unknown command");
				printHelp();
			}
		}
		scanner.close();
	}
	
	private void printHelp() {
		System.out.println("Here is a list of all available commands: ");
		System.out.println("==========================================");
		System.out.println("/raw - enables raw mode");
		System.out.println("/clients - shows all connected clients");
		System.out.println("/kick [users ID or username] - kicks a user");
		System.out.println("/help - show this help message");
		System.out.println("/quit - shut down the server");
	}
	
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while(running) {
					//Managing
					sendToAll("/i/server");
					sendStatus();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(int i = 0; i < clients.size(); i++) {
						ServerClient c = clients.get(i);
						if(!clientResponse.contains(clients.get(i).getID())) {
							if(c.attempt >= MAX_ATTEMPS) {
								disconnect(c.getID(), false);
							}else {
								c.attempt++;
							}
						}else {
							clientResponse.remove(Integer.valueOf((c.getID())));
							c.attempt = 0;
						}
					}
				}
			}
		};
		manage.start();
	}
	
	private void sendStatus() {
		if(clients.size() <=0) {
			return;
		}
		String users = "/u/";
		for(int i = 0; i < clients.size() - 1; i++) {
			users += clients.get(i).name + "/n/";
		}
		users += clients.get(clients.size()-1).name + "/e/";
		sendToAll(users);
	}
	
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while(running) {
					//Receiving
					byte[] data = new byte[60000];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (SocketException e) {
						
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					process(packet);										
				}
			}
		};
		receive.start();
	}
	
	private void sendToAll(String message) {
		if(message.startsWith("/m/")) {// /m/ name: message "/time/" time "/e/"
			String text = message.substring(3); //name: message "/time/" time "/e/"
			text = text.split("/e/")[0]; //name: message "/time/" time
		}
		for(int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
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
	
	private void send(String message, InetAddress address, int port) {
		message+= "/e/";
		send(message.getBytes(), address, port);
	}
	
	private void process(DatagramPacket packet) {
		String string = new String (packet.getData());
		if(raw) {
			System.out.println(string);
		}
		if(string.startsWith("/c/")) {
			int id = UniqueIdentifier.getIdentifier();
			String name = string.split("/c/|/e/")[1];
			System.out.println(name + "(" + id + ") connected!");			
			clients.add(new ServerClient(name, packet.getAddress(), packet.getPort(), id));
			String ID = "/c/" + id;
			send(ID, packet.getAddress(), packet.getPort());
		}
		else if(string.startsWith("/m/")) { // /m/ name: message /e/	
			LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedDate = time.format(myFormatObj);
			//insert message into database
			try {
				String message = string.substring(3); //name: message /e/
				message = message.split("/e/")[0];//name: message
				PreparedStatement ps = Connect.getConnect().prepareStatement("insert into MessageHistory(UserName, Message, Time) "
						+ "values (?,?,?)");
				ps.setString(1, message.split(":")[0]);
				ps.setString(2, message.split(":")[1]);
				ps.setString(3, formattedDate);
				ps.execute();			
				message = "/m/" + message + "/time/" + formattedDate + "/e/" ; 
				sendToAll(message);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Send failed!", "Notification", 1);
				e1.printStackTrace();
			}			
		}
		else if(string.startsWith("/d/")){
			String id = string.split("/d/|/e/")[1];
			disconnect(Integer.parseInt(id), true);
		}
		else if(string.startsWith("/i/")){
			clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
		}else if(string.startsWith("/h/")) {
			try{
			    final String sql = "SELECT * FROM MessageHistory;";
			    PreparedStatement ps = Connect.getConnect().prepareStatement(sql);
			    ResultSet rs = ps.executeQuery();
			    if(rs != null){
			        while(rs.next()){
			            String UserName = rs.getString("UserName");
			            String Message = rs.getString("Message");		
			            String time = rs.getString("Time");	            
						String history = "/m/" + UserName + ": " + Message + "/time/" + time + "/e/";
			            send(history, packet.getAddress(), packet.getPort());			            
			        }
			        send("/h/", packet.getAddress(), packet.getPort());
			        rs.close();
			    }
			    ps.close();
			}catch(Exception e){
			    e.printStackTrace();
			}
		}else if(string.startsWith("/f/")) {
			sendFileToAll(string, packet);
		}else if(string.startsWith("/cp/")) {// "/cp/" + client.getName() + "/to/" + sellectedChatUser + "/e/"
			String name = string.split("/to/")[1];
			name = name.split("/e/")[0];
			String namesend = string.substring(4).split("/to/")[0];
			sendToClient(string, name);
			try{
			    final String sql = "SELECT * FROM PrivateMessageHistory WHERE (NameSend = ? AND NameReceive = ?) OR (NameReceive = ? AND NameSend = ?) ;";
			    PreparedStatement ps = Connect.getConnect().prepareStatement(sql);
			    ps.setString(1, name);
			    ps.setString(2, namesend);
			    ps.setString(3, name);
			    ps.setString(4, namesend);
			    ResultSet rs = ps.executeQuery();    
			    if(rs != null){
			        while(rs.next()){
			            String NameSend = rs.getString("NameSend");
			            String Message = rs.getString("Message");		
			            String NameReceive = rs.getString("NameReceive");
			            String time = rs.getString("Time");	            
						String history = "/pm/" + NameSend + ": " + Message + "/to/" + NameReceive + "/time/" + time + "/e/";
			            send(history, packet.getAddress(), packet.getPort());		
			            sendToClient(history, name);
			        }
			        rs.close();
			    }
			    ps.close();				   
			}catch(Exception e){
			    e.printStackTrace();
			}
		}else if(string.startsWith("/pm/")) { // /pm/ namesend: message /to/ namereceive /e/
			LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedDate = time.format(myFormatObj);
			String name = string.split("/to/")[1];
			name = name.split("/e/")[0];
			String namesend = string.substring(4);
			String message = namesend.split(":")[1].split("/to/")[0];
			namesend = namesend.split(":")[0];						
			//insert message into database
			try {				
				PreparedStatement ps = Connect.getConnect().prepareStatement("insert into PrivateMessageHistory(NameSend, Message, NameReceive, Time) "
						+ "values (?,?,?,?)");
				ps.setString(1, namesend );
				ps.setString(2, message);
				ps.setString(3, name);
				ps.setString(4, formattedDate);
				ps.execute();			
				string = string.split("/e/")[0] + "/time/" + formattedDate +"/e/";  // /pm/ namesend: message /to/ namereceive /time/ time /e/
				sendToClient(string, namesend);
				sendToClient(string, name); 				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Send failed!", "Notification", 1);
				e1.printStackTrace();
			}		
		}else if(string.startsWith("/pf/")) { //"/pf/" + encodedString + "/from/" + client.getName() + "/to/" + client2Name + "/e/"
			String name = string.split("/to/")[1].split("/e/")[0];
			sendToClient(string, name);
		}
	}
	
	private void sendToClient(String message, String name) {
		for(int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			if(client.name.equals(name)) {
				send(message.getBytes(), client.address, client.port);
			}	
		}
	}
	
	private void sendFileToAll(String file, DatagramPacket packet) {
		for(int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			if(client.port != packet.getPort()) {
				send(file.getBytes(), client.address, client.port);
			}	
		}
	}
	
	private void quit() {
		while(clients.size()!= 0){ 
			disconnect(clients.get(0).getID(), true);
		 }
		running = false;
		socket.close();
	}
	
	private void disconnect(int id, boolean status) {
		ServerClient c = null;
		boolean existed = false;
		for(int i = 0; i< clients.size(); i++) {
			if(clients.get(i).getID() == id) {
				c = clients.get(i);
				clients.remove(i);
				existed = true;
				break;
			}
		}
		if(!existed) {
			return;
		}
		String message = "";
		if(status) {
			message = "Client " + c.name.trim() + "(" +c.getID() + ") @" + c.address.toString() + ":" +c.port + " disconnected!\n";
		}else {
			message = "Client " + c.name.trim() + "(" +c.getID() + ") @" + c.address.toString() + ":" +c.port + " timed out!\n";
		}
		System.out.print(message);
	}
}
