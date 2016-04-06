package com.VTC.mcserver.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import com.jcraft.jsch.JSchException;

/**
 *
 * @author vtcakavsmoace
 */
public class AccessDenied extends JFrame {
	
	private static final long serialVersionUID = -8408238690177814663L;
	
	public AccessDenied(JSchException e) {
		initComponents(e);
	}

	private void initComponents(JSchException jsche) {

		jLabel1 = new JLabel();
		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();
		jButton1 = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		jLabel1.setText("Access Denied: this probably means that you haven't given your public key to the server.");

		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		jsche.printStackTrace(pw);
		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jTextArea1.setText(sw.toString());
		jScrollPane1.setViewportView(jTextArea1);

		jButton1.setText("Cancel");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING,
								GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
						.addComponent(jLabel1, GroupLayout.Alignment.TRAILING,
								GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(jButton1)))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1)
						.addContainerGap()));

		pack();
		setLocationRelativeTo(null);
	}

	private void jButton1ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private JButton jButton1;
	private JLabel jLabel1;
	private JScrollPane jScrollPane1;
	private JTextArea jTextArea1;

}
