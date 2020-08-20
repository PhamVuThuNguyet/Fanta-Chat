package com.thefanta.fantachat;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JList;
import java.awt.GridBagConstraints;

public class OnlineUsers extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<String> list;
	protected PrivateClientWindow privateclient;

	/**
	 * Create the frame.
	 */
	public OnlineUsers(Client client, List<PrivateClientWindow> privateclients) {
		setTitle("Online Users");
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(200, 320);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		list = new JList<String>();
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (e.getValueIsAdjusting()) {
					String sellectedChatUser = list.getSelectedValue();
					System.out.println(sellectedChatUser);				
					for(int i = 0; i < privateclients.size(); i++) {
						if(privateclients.get(i).getClient().equals(client) && privateclients.get(i).getClient2Name().equals(sellectedChatUser)) {
							privateclients.get(i).setVisible(true);
							return;
						}
					}
					privateclient = new PrivateClientWindow(client, sellectedChatUser);
					privateclients.add(privateclient);
					String connect = "/cp/" + client.getName() + "/to/" + sellectedChatUser + "/e/";
					client.send(connect.getBytes());
				}
			}
		});
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		JScrollPane p = new JScrollPane();
		p.setViewportView(list);
		contentPane.add(p, gbc_list);
		list.setFont(new Font("Verdana", 0, 24));
	}

	public void update(String[] users) {
		list.setListData(users);
	}
}
