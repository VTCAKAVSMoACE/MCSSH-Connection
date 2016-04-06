package com.VTC.mcserver.graphics;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;

import com.VTC.mcserver.Main;
import com.jcraft.jsch.JSchException;

/**
 *
 * @author vtcakavsmoace
 */
public class StartupPanel extends JPanel {

	private static final long serialVersionUID = -3592703114011554457L;

	public StartupPanel() {
		initComponents();
	}

	private void initComponents() {

		BufferedReader configReader = null;

		try {
			configReader = new BufferedReader(new FileReader("MCSSH.config"));
		} catch (FileNotFoundException e) {
		}

		usernameLabel = new JLabel();
		username = new JTextField();
		hostLabel = new JLabel();
		hostIP = new JTextField();
		portNumber = new JTextField();
		hostPortLabel = new JLabel();
		startServer = new JCheckBox();
		mcfolderLabel = new JLabel();
		mcdest = new JTextField();
		connect = new JButton();
		rememberSettings = new JCheckBox();

		usernameLabel.setText("Username");

		try {
			username.setText(configReader.readLine());
		} catch (Exception e) {
			username.setText("Enter username here");
		}

		hostLabel.setText("Host");

		try {
			hostIP.setText(configReader.readLine());
		} catch (Exception e) {
			hostIP.setText("Enter host IP address here");
		}

		try {
			portNumber.setText(configReader.readLine());
		} catch (Exception e) {
			portNumber.setText("Enter host ssh port number");
		}

		hostPortLabel.setText("Host Port");

		startServer.setText("Start the Server (leave unchecked if the server is running");

		mcfolderLabel.setText("Target Jar");

		try {
			mcdest.setText(configReader.readLine());
		} catch (Exception e) {
			mcdest.setText("Enter path to minecraft server jar on host");
		}

		connect.setText("Connect");
		connect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectActionPerformed(evt);
			}
		});

		rememberSettings.setSelected(true);
		rememberSettings.setText("Remember this configuration?");

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
								.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
										.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(usernameLabel)
												.addComponent(hostLabel).addComponent(hostPortLabel))
										.addGap(24, 24, 24)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(username).addComponent(hostIP).addComponent(portNumber)))
								.addGroup(layout.createSequentialGroup().addComponent(mcfolderLabel).addGap(23, 23, 23)
										.addComponent(mcdest))
								.addComponent(connect, GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(rememberSettings).addComponent(startServer))
										.addGap(0, 0, Short.MAX_VALUE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(usernameLabel)
								.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(hostLabel)
								.addComponent(hostIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(portNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(hostPortLabel))
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(mcfolderLabel)
								.addComponent(mcdest, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18).addComponent(startServer)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(rememberSettings)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(connect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
	}

	private void connectActionPerformed(ActionEvent evt) {
		try {
			if (rememberSettings.isSelected()) {
				PrintWriter out = new PrintWriter("MCSSH.config");
				out.println(username.getText());
				out.println(hostIP.getText());
				out.println(portNumber.getText());
				out.println(mcdest.getText());
				out.close();
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("Cannot stat file MCSSH.config.");
		}
		if (!new File("id_rsa").exists()) {
			Main.generateKeypair();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						new KeyDisplay().setVisible(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new ServerConnection("id_rsa", "id_rsa.pub", username.getText(), hostIP.getText(),
							Integer.parseInt(portNumber.getText()), mcdest.getText(), startServer.isSelected())
									.setVisible(true);
				} catch (NumberFormatException | JSchException | IOException e) {
				}
			}
		});
	}

	private JButton connect;
	private JTextField hostIP;
	private JLabel hostLabel;
	private JLabel hostPortLabel;
	private JTextField mcdest;
	private JLabel mcfolderLabel;
	private JTextField portNumber;
	private JCheckBox rememberSettings;
	private JCheckBox startServer;
	private JTextField username;
	private JLabel usernameLabel;

}
