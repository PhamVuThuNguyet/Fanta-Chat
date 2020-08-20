package com.thefanta.fantachat;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.io.FileUtils;

import java.awt.Component;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class ClientWindow extends JFrame implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private Thread listen, run;
	
	private Client client;
	private OnlineUsers users;
	
	private boolean running = false;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;
		
	private List<PrivateClientWindow> privateclients = new ArrayList<PrivateClientWindow>();
	
	public ClientWindow(String name, String address, int port) {
		setTitle("Fanta Chat Client");
		client = new Client(name, address, port);
		boolean connect = client.openConnection(address);
		if(!connect) {
			System.err.println("Connection failed!");
			console("Connection failed!");
		}
		createWindow();
		send("/h/", false);
		users = new OnlineUsers(client, privateclients);
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	
	/**
	 * Create the frame.
	 */
	
	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 880, 550);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("MENU");
		mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		menuBar.add(mnFile);
		
		mntmNewMenuItem = new JMenuItem("Online Users");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				users.setVisible(true);
			}
		});
		mntmNewMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 18));
		mnFile.add(mntmNewMenuItem);
		
		mntmNewMenuItem_1 = new JMenuItem("Exit");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect,false);
				running = false;
				client.close();			
				dispose();
			}
		});
		mntmNewMenuItem_1.setFont(new Font("SansSerif", Font.PLAIN, 18));
		mnFile.add(mntmNewMenuItem_1);
		
		mntmNewMenuItem_2 = new JMenuItem("File Sharing");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fc = new JFileChooser();					
					int returnVal=fc.showOpenDialog(null);
					if(returnVal==JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String encodedString = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));		               
		                encodedString = "/f/" + encodedString + "/e/" + client.getName();
		                client.send(encodedString.getBytes());
					}
				}catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		});
		mntmNewMenuItem_2.setFont(new Font("SansSerif", Font.PLAIN, 18));
		mnFile.add(mntmNewMenuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{28, 815, 30, 7};
		gbl_contentPane.rowHeights = new int[]{25, 485, 40};
		contentPane.setLayout(gbl_contentPane);
		
		history = new JTextArea();
		history.setEditable(false);
		JScrollPane scroll = new JScrollPane(history);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		scrollConstraints.weightx = 1;
		scrollConstraints.weighty = 1;
		scrollConstraints.insets = new Insets(0, 5, 0, 0);
		contentPane.add(scroll, scrollConstraints);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText(), true);
				}
			}
		});
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 2;
		gbc_txtMessage.gridwidth = 2;
		gbc_txtMessage.weightx = 1;
		gbc_txtMessage.weighty = 0;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText(), true);
			}
		});
		btnSend.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		btnSend.setFont(new Font("SansSerif", Font.PLAIN, 18));
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		gbc_btnSend.weightx = 0;
		gbc_btnSend.weighty = 0;
		contentPane.add(btnSend, gbc_btnSend);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect,false);
				running = false;
				client.close();				
			}
		});
		
		setVisible(true);
		
		txtMessage.requestFocusInWindow();
	}
	
	private void send(String message, boolean text) {
		if(message.equals("")) {
			return;
		}
		else if(message.startsWith("/i/")) {
			client.send( message.getBytes());
		}
		else if(message.equals("/h/")) {
			client.send( message.getBytes());
		}
		else if(text) {
			message = client.getName() + ": " + message;
			message = "/m/" + message +"/e/";// /m/ name: message /e/
			client.send( message.getBytes());
			txtMessage.setText("");
		}
		else {
			client.send( message.getBytes());
			txtMessage.setText("");
		}
	}
	
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while(running) {
					String message = client.receive();
					if(message.startsWith("/h/")){
						String connection = "/c/" +  client.getName() + "/e/";
						client.send(connection.getBytes());
					}
					else if(message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						console("Successfully connected to server! ID: "+ client.getID());
					}else if(message.startsWith("/m/")) {// /m/name: message /time/ time /e/
						String text = message.substring(3);//name: message /time/ time /e/
						text = text.split("/e/")[0];//name: message /time/ time 
						String mes = text.split("/time/")[0];
						String time = text.split("/time/")[1];
						console("\t" + time + "\n" + mes);
					}else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						send(text, false);
					}else if(message.startsWith("/k/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						console(text);
						txtMessage.setEditable(false);
					}else if(message.startsWith("/u/")) {
						String[] u = message.split("/u/|/n/|/e/");
						users.update(Arrays.copyOfRange(u, 1, u.length - 1));
					}else if(message.startsWith("/f/")) {
						String subString = message.substring(3);
						String fileString = subString.split("/e/")[0];
						String nameString = message.split("/e/")[1];
						console(nameString + " has sent a file!");
						int result =JOptionPane.showConfirmDialog(history, nameString + " has sent you a file! Do you want to download this file? ", "Download File", JOptionPane.YES_NO_OPTION);
						if(result == JOptionPane.YES_OPTION) {
							byte[] decodedBytes = Base64.getDecoder().decode(fileString);
							try {
								JFileChooser fc = new JFileChooser();
								int returnVal=fc.showSaveDialog(null);
								if(returnVal==JFileChooser.APPROVE_OPTION) {
									File file = fc.getSelectedFile();
									FileUtils.writeByteArrayToFile(file, decodedBytes);
								}
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}						
					}else if(message.startsWith("/cp/")) { //"/cp/" + client.getName() + "/to/" + sellectedChatUser + "/e/"						
						String name = message.split("/to/")[0];
						name = name.split("/cp/")[1];
						System.out.println("connected to "+ name);
						PrivateClientWindow privateClientWindow = new PrivateClientWindow(client, name);				
						privateclients.add(privateClientWindow);
						privateClientWindow.setVisible(false);
					}
				}
			}
		};
		listen.start();
	}
	
	
	public void console(String message) {
		history.append(message+"\n\r");
		history.setCaretPosition(history.getDocument().getLength());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		listen();
	}


	public List<PrivateClientWindow> getPrivateclients() {
		return privateclients;
	}

	public void setPrivateclients(List<PrivateClientWindow> privateclients) {
		this.privateclients = privateclients;
	}
	
}
