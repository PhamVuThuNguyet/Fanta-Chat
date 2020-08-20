package com.thefanta.fantachat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;

import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtAddress;
	private JLabel lblIPAddress;
	private JTextField txtPort;
	private JLabel lblPort;
	private JLabel lblAddressDesc;
	private JLabel lblPortDesc;
	private JPasswordField passwordField;
	
	/**
	 * Create the frame.
	 */
	public Login() {
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
		
		setResizable(false);
		setFont(new Font("SansSerif", Font.PLAIN, 18));
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(311, 473);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtUsername.setBounds(38, 46, 220, 30);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblName = new JLabel("Username:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblName.setBounds(38, 10, 220, 26);
		contentPane.add(lblName);
		
		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtAddress.setBounds(38, 212, 220, 30);
		contentPane.add(txtAddress);
		
		lblIPAddress = new JLabel("Server IP Address:");
		lblIPAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblIPAddress.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblIPAddress.setBounds(38, 176, 220, 26);
		contentPane.add(lblIPAddress);
		
		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtPort.setBounds(38, 313, 220, 30);
		contentPane.add(txtPort);
		
		lblPort = new JLabel("Server Port:");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblPort.setBounds(64, 277, 167, 26);
		contentPane.add(lblPort);
		
		lblAddressDesc = new JLabel("(eg. 192.168.1.1)");
		lblAddressDesc.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddressDesc.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblAddressDesc.setBounds(38, 241, 220, 26);
		contentPane.add(lblAddressDesc);
		
		lblPortDesc = new JLabel("(eg. 7000)");
		lblPortDesc.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortDesc.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblPortDesc.setBounds(38, 341, 220, 26);
		contentPane.add(lblPortDesc);
		
		JButton btnLogin = new JButton("Sign In");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtUsername.getText();
				@SuppressWarnings("deprecation")
				String password = passwordField.getText();
				String address = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				if(name.isEmpty()||password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Invalid account or password!", "Notification", 1);
				}else {
					Client c= Client.CheckAccount(name, password, address, port);
					try {
						if(c!=null) {
							login(name, address, port);
						}else {
							JOptionPane.showMessageDialog(null, "Account not found!", "Notification",1);
						}
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, e2);
					}
				}
			}
		});
		btnLogin.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnLogin.setHorizontalTextPosition(SwingConstants.CENTER);
		btnLogin.setFont(new Font("SansSerif", Font.PLAIN, 15));
		btnLogin.setBounds(38, 395, 85, 26);
		contentPane.add(btnLogin);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblPassword.setBounds(38, 94, 220, 26);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		passwordField.setBounds(38, 136, 220, 30);
		contentPane.add(passwordField);
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Register reg = new Register();
				reg.setVisible(true);
				reg.setTitle("REGISTER FORM");
				dispose();
			}
		});
		btnSignUp.setHorizontalTextPosition(SwingConstants.CENTER);
		btnSignUp.setFont(new Font("SansSerif", Font.PLAIN, 15));
		btnSignUp.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnSignUp.setBounds(170, 395, 85, 26);
		contentPane.add(btnSignUp);
	}
	
	/**
	 * Login method
	 */
	private void login(String name, String address, int port) {
		dispose();
		new ClientWindow(name, address, port);		
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
