package com.thefanta.fantachat;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.io.FileUtils;

import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Base64;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;

public class PrivateClientWindow extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	
	private Client client;
	private String client2Name;

	private boolean running = false;
	private Thread listen, run;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNewMenuItem_2;
	

	/**
	 * Create the frame.
	 */
	public PrivateClientWindow(Client client1, String name) {
		this.client = client1;
		this.client2Name = name;
		createWindow();
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	public void createWindow() {
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
		
		setTitle("Fanta Chat Private Client: " + client.getName() + " - " + client2Name);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300, 100, 880, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{28, 815, 30, 7};
		gbl_contentPane.rowHeights = new int[]{25, 485, 40};
		contentPane.setLayout(gbl_contentPane);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("MENU");
		mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		menuBar.add(mnFile);
		
		mntmNewMenuItem_2 = new JMenuItem("File Sharing");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fc = new JFileChooser();					
					int returnVal=fc.showOpenDialog(null);
					if(returnVal==JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String encodedString = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));		               
		                encodedString = "/pf/" + encodedString + "/from/" + client.getName() + "/to/" + client2Name + "/e/";
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
		
		history = new JTextArea();
		history.setEditable(false);
		JScrollPane scroll = new JScrollPane(history);
		GridBagConstraints gbc_history = new GridBagConstraints();
		gbc_history.insets = new Insets(0, 0, 5, 5);
		gbc_history.fill = GridBagConstraints.BOTH;
		gbc_history.gridx = 0;
		gbc_history.gridy = 0;
		gbc_history.gridwidth = 3;
		gbc_history.gridheight = 2;
		gbc_history.weightx = 1;
		gbc_history.weighty = 1;
		gbc_history.insets = new Insets(0, 5, 0, 0);
		contentPane.add(scroll, gbc_history);
		
		txtMessage = new JTextField();
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
		
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(txtMessage.getText()=="") {
						return;
					}
					else {
						String mes = client.getName() + ": " + txtMessage.getText() + "/to/" + client2Name;
						mes = "/pm/" + mes + "/e/";
						client.send(mes.getBytes()); // /pm/ namesend: message /to/ namereceive /e/
						mes = mes.substring(4); // namesend: message /to/ namereceive /e/
						mes = mes.split("/to/")[0];// namesend: message 
						txtMessage.setText("");
					}
				}
			}
		});
		
		JButton btnSend = new JButton("Send");
		btnSend.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtMessage.getText()=="") {
					return;
				}
				else {
					String mes = client.getName() + ": " + txtMessage.getText() + "/to/" + client2Name;
					mes = "/pm/" + mes + "/e/";
					client.send(mes.getBytes()); // /pm/ namesend: message /to/ namereceive /e/
					mes = mes.substring(4); // namesend: message /to/ namereceive /e/
					mes = mes.split("/to/")[0];// namesend: message 
					txtMessage.setText("");
				}
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		btnSend.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		btnSend.setFont(new Font("SansSerif", Font.PLAIN, 18));
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		gbc_btnSend.weightx = 0;
		gbc_btnSend.weighty = 0;
		contentPane.add(btnSend, gbc_btnSend);
		
		setVisible(true);
		txtMessage.requestFocusInWindow();
	}
	
	public void console(String message) {
		history.append(message+"\n\r");
		history.setCaretPosition(history.getDocument().getLength());
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getClient2Name() {
		return client2Name;
	}

	public void setClient2Name(String client2Name) {
		this.client2Name = client2Name;
	}

	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while(running) {
					String message = client.receive();
					if(message.startsWith("/pm/")) { // /pm/ namesend: message /to/ namereceive /time/ time /e/
						String text = message.substring(4); //namesend: message /to/ namereceive /time/ time /e/
						String time = text.split("/time/")[1].split("/e/")[0];
						text = text.split("/to/")[0];//name: message
						setVisible(true);
						console("\t" + time + "\n" + text);
					}else if(message.startsWith("/pf/")) { //"/pf/" + encodedString + "/from/" + client.getName() + "/to/" + client2Name + "/e/"
						String subString = message.substring(4);
						String fileString = subString.split("/from/")[0];
						String nameString = message.split("/from/")[1].split("/to/")[0];
						console(nameString + " has sent you a file!");
						int result =JOptionPane.showConfirmDialog(history, "Do you want to download this file", "Download File", JOptionPane.YES_NO_OPTION);
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
					}
				}
			}
		};
		listen.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		listen();
	}
	
}
