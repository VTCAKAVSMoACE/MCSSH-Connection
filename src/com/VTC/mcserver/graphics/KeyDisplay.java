package com.VTC.mcserver.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

/**
 *
 * @author vtcakavsmoace
 */
public class KeyDisplay extends JFrame {

	private static final long serialVersionUID = -5577657415802743731L;
	
	/**
	 * Creates new form KeyDisplay
	 * @throws IOException 
	 */
	public KeyDisplay() throws IOException {
		initComponents();
	}

	private void initComponents() throws IOException {

		keyHeader1 = new JLabel();
		separator = new JLabel();
		keyHeader2 = new JLabel();
		keyScrollPane = new JScrollPane();
		keyTextArea = new JTextArea();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		keyHeader1.setText("This is your public key. Distribute this to your serverhost so");

		keyHeader2.setText("that you can connect via ssh. DO NOT DISTRIBUTE id_rsa!");

		File file = new File("id_rsa.pub");
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		String key = new String(data, "UTF-8");
		
		keyTextArea.setColumns(20);
		keyTextArea.setRows(5);
		keyTextArea.setEditable(false);
		keyTextArea.setText(key);
		keyScrollPane.setViewportView(keyTextArea);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap().addGroup(layout
						.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(keyScrollPane)
						.addComponent(keyHeader1, GroupLayout.Alignment.LEADING,
								GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
						.addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(separator).addComponent(keyHeader2))
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(keyHeader1)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(keyHeader2)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(separator)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(keyScrollPane, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
		setLocationRelativeTo(null);
	}

	private JLabel keyHeader1;
	private JLabel separator;
	private JLabel keyHeader2;
	private JScrollPane keyScrollPane;
	private JTextArea keyTextArea;
}
