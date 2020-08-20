package com.thefanta.fantachat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import Database.Connect;

import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class Register extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Register() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "REGISTER", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("Username");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("Password");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.setForeground(Color.RED);
		btnSignUp.setIcon(new ImageIcon("C:\\Users\\nk\\Pictures\\FAVPNG_share-icon-clip-art_H7aTKSqZ.png"));
		btnSignUp.setBackground(Color.WHITE);
		btnSignUp.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().isEmpty()||passwordField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Invalid username or password!", "Notification", 1);
				}else if(textField.getText().length()>10||passwordField.getText().length()>20) {
					JOptionPane.showMessageDialog(null, "Invalid username or password!", "Notification", 1);
				}else {
					try {
						PreparedStatement ps = Connect.getConnect().prepareStatement("insert into Accounts(UserName, Password) values (?,?)");
						ps.setString(1, textField.getText());
						ps.setString(2, passwordField.getText());
						ps.execute();
						JOptionPane.showMessageDialog(null, "Register successfully!", "Notification", 1);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Register failed!", "Notification", 1);
					}
				}
			}
		});
		btnSignUp.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnSignUp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JButton btnLogin = new JButton("Sign In");
		btnLogin.setForeground(Color.BLUE);
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setIcon(new ImageIcon("C:\\Users\\nk\\Pictures\\kisspng-computer-icons-login-icon-design-5b2c21206296c8.1035883515296187204038.png"));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login lg= new Login();
				lg.setVisible(true);
				lg.setTitle("LIBRARY MANAGEMENT");
				dispose();
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLogin.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(34)
					.addComponent(btnSignUp, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
					.addGap(39))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
							.addGap(23)
							.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(91, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(39)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(3)
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
					.addGap(55)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSignUp)
						.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(31, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
