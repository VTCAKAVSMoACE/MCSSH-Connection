package com.VTC.mcserver.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;

/**
 *
 * @author vtcakavsmoace
 */
public class ServerConnection extends JFrame {

	private static final long serialVersionUID = 5274524154284957338L;

	private int comindex = 0;

	private ArrayList<String> commandHistory = new ArrayList<String>(0);

	private String currentCommand = "";

	private final PipedInputStream input;

	private final PrintStream inputPrinter;

	private PrintStream serverOutput;

	private final Updater outputUpdate;

	private final Thread outputUpdateThread;

	private final String privateKey;

	private final String publicKey;

	private final String user;

	private final String host;

	private final int port;

	private final String destination;

	private final Channel[] channels = new Channel[2];

	/**
	 * Creates new form ServerConnection
	 */
	public ServerConnection(String privateKey, String publicKey, String user, String host, int port, String destination,
			boolean startServer) throws JSchException, IOException {

		input = new PipedInputStream();
		PipedOutputStream inPrint = new PipedOutputStream(input);
		inputPrinter = new PrintStream(inPrint);

		this.privateKey = privateKey;

		this.publicKey = publicKey;

		this.user = user;

		this.host = host;

		this.port = port;

		this.destination = destination;

		if (!initComponents()) {
			outputUpdate = null;
			outputUpdateThread = null;
			return;
		}

		outputUpdate = new Updater(startServer);

		outputUpdateThread = new Thread(outputUpdate);

		outputUpdateThread.start();
	}

	private class Updater implements Runnable {
		private volatile boolean running = true;

		private final boolean startServer;

		PipedInputStream input = new PipedInputStream();
		PipedOutputStream inPrint;
		PrintStream inputPrinter;

		public Updater(boolean startServer) {
			this.startServer = startServer;
			try {
				inPrint = new PipedOutputStream(input);
				inputPrinter = new PrintStream(inPrint);
			} catch (IOException e) {
				inPrint = null;
				inputPrinter = null;
				System.exit(1);
			}
		}

		public void run() {
			PipedInputStream pushedOutput = new PipedInputStream();
			PipedOutputStream output;
			try {
				output = new PipedOutputStream(pushedOutput);
			} catch (IOException e2) {
				output = null;
				e2.printStackTrace();
				System.exit(1);
			}
			serverOutput = new PrintStream(output);
			BufferedReader reader = new BufferedReader(new InputStreamReader(pushedOutput));

			try {
				channels[0] = com.VTC.mcserver.Main.setupChannel(privateKey, publicKey, user, host, port, input,
						serverOutput);
				channels[0].connect(3000);
				inputPrinter.println("cd " + destination.substring(0, destination.lastIndexOf("/")));
				if (startServer)
					inputPrinter.println("./servstart " + destination.substring(destination.lastIndexOf("/") + 1));
				else
					inputPrinter.println("cat logs/latest.log");
				inputPrinter.println("./readMCOUT .");
			} catch (JSchException | IOException e1) {
				channels[0] = null;
				e1.printStackTrace();
				System.exit(1);
			}
			try {
				String line;
				while ((line = reader.readLine()) != null && running) {
					if (line.startsWith("[")) {
						outputTextArea.append(line + "\n");
						outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());
					}
				}
			} catch (IOException e) {
			}
		}

		public void kill() {
			running = false;
		}
	}

	private boolean initComponents() throws JSchException, IOException {

		try {
			channels[1] = com.VTC.mcserver.Main.setupChannel(privateKey, publicKey, user, host, port, input,
					new PrintStream(new PipedOutputStream(new PipedInputStream())));
		} catch (JSchException e) {
			new AccessDenied(e).setVisible(true);
			throw new JSchException();
		}

		channels[1].connect(3000);
		inputPrinter.println("cd " + destination.substring(0, destination.lastIndexOf("/")));
		inputPrinter.println("./writeMCIN .");

		this.setTitle("SSH Connection");

		outputTextAreaScroller = new JScrollPane();
		outputTextArea = new JTextArea();
		outputLabel = new JLabel();
		connectCloseButton = new JButton();
		commandField = new JTextField();
		runCommandButton = new JButton();
		commandLogScroller = new JScrollPane();
		commandLog = new JTextArea();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		outputTextArea.setEditable(false);
		outputTextAreaScroller.setViewportView(outputTextArea);

		outputLabel.setText("Output");

		connectCloseButton.setText("Close Connection");
		connectCloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				closeConnectionActionPerformed(evt);
			}
		});

		commandField.setText("Enter command here");
		commandField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				commandFieldActionPerformed(evt);
			}
		});
		commandField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// UNUSED
			}

			@Override
			public void keyPressed(KeyEvent e) {
				commandFieldKeyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// UNUSED
			}

		});

		runCommandButton.setText("Execute");
		runCommandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				runCommandButtonActionPerformed(evt);
			}
		});

		commandLog.setEditable(false);
		commandLogScroller.setViewportView(commandLog);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.commandField, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
								.addComponent(this.runCommandButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(this.commandLogScroller).addComponent(this.connectCloseButton,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(this.outputLabel, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGap(344, 344, 344))
								.addComponent(this.outputTextAreaScroller))
						.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(this.outputLabel, GroupLayout.PREFERRED_SIZE, 22,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(this.outputTextAreaScroller))
										.addGroup(layout.createSequentialGroup()
												.addComponent(this.commandField, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(this.runCommandButton)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(this.commandLogScroller, GroupLayout.DEFAULT_SIZE, 486,
														Short.MAX_VALUE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(this.connectCloseButton)))
								.addContainerGap()));

		WindowListener exitListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeConnectionActionPerformed(null);
				ServerConnection.this.setVisible(false);
				ServerConnection.this.dispose();
			}
		};
		this.addWindowListener(exitListener);

		pack();
		setLocationRelativeTo(null);
		return true;
	}

	protected void commandFieldKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (comindex > 0) {
				comindex--;
			}
			if (comindex == commandHistory.size())
				currentCommand = commandField.getText();
			commandField.setText(commandHistory.get(comindex));
			break;
		case KeyEvent.VK_DOWN:
			try {
				comindex++;
				commandField.setText(commandHistory.get(comindex));
			} catch (IndexOutOfBoundsException aoobe) {
				comindex = commandHistory.size();
				commandField.setText(currentCommand);
			}
			break;
		}
	}

	private void commandFieldActionPerformed(ActionEvent evt) {
		commandHistory.add(commandField.getText());
		currentCommand = "";
		comindex = commandHistory.size();
		String command = commandField.getText();
		inputPrinter.println(command);
		commandField.setText("");
		commandLog.append(command + "\n");
		commandLog.setCaretPosition(commandLog.getDocument().getLength());
	}

	private void runCommandButtonActionPerformed(ActionEvent evt) {
		commandFieldActionPerformed(evt);
	}

	private void closeConnectionActionPerformed(ActionEvent evt) {
		outputUpdate.kill();
		channels[0].disconnect();
		channels[1].disconnect();
		this.setVisible(false);
		this.dispose();
	}

	private JButton connectCloseButton;
	private JButton runCommandButton;
	private JLabel outputLabel;
	private JScrollPane commandLogScroller;
	private JScrollPane outputTextAreaScroller;
	private JTextField commandField;
	private JTextArea commandLog;
	private JTextArea outputTextArea;
}
