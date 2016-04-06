package com.VTC.mcserver.graphics;

/**
 *
 * @author vtcakavsmoace
 */
public class MainFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = -7209522791480359107L;

	/**
	 * Creates new form MainFrame
	 */
	public MainFrame() {
		initComponents();
	}

	private void initComponents() {
		
		this.setTitle("MC SSH Setup");

		startupPanel1 = new com.VTC.mcserver.graphics.StartupPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(startupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(startupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
		setLocationRelativeTo(null);
	}

	private com.VTC.mcserver.graphics.StartupPanel startupPanel1;
}
