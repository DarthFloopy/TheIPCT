package com.josephcagle.ipct.client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


class GetInfoFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPanel;
	private JTextField nameField;
	private JTextField ipField;
	
	private volatile boolean doneLoading = false;
	
	private final Image icon = new ImageIcon(this.getClass().getResource("IPCT_logo.png")).getImage();
	
	//	/**
	//	 * Test-launch the application.
	//	 */
	//	public static void main(String[] args) {
	//		GetInfoFrame f = new GetInfoFrame(); // to do: use event thread later
	//		System.out.println(Arrays.toString(f.getNameAndIP()));
	//	}

//	/**
//	 * Create the frame.
//	 */
	GetInfoFrame() {
		EventQueue.invokeLater(new Runnable() { 
			public void run() {
				// boilerplate generated by gui builder + add custom listeners
				
				setTitle("The IPCT");
				GetInfoFrame.this.setIconImage(icon);

				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				setBounds(100, 100, 450, 300);
				contentPanel = new JPanel();
				contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				contentPanel.setLayout(new BorderLayout(0, 0));
				setContentPane(contentPanel);

				JPanel mainPanel = new JPanel();
				contentPanel.add(mainPanel, BorderLayout.CENTER);
				mainPanel.setLayout(new BorderLayout(0, 0));

				JLabel welcomeLabel = new JLabel("<html>Welcome to the <b>Inter-Pi Connection Thingamajigger!</b>");
				mainPanel.add(welcomeLabel, BorderLayout.NORTH);

				JSeparator separator = new JSeparator();
				mainPanel.add(separator, BorderLayout.CENTER);

				JPanel infoPanel = new JPanel();
				mainPanel.add(infoPanel, BorderLayout.SOUTH);
				infoPanel.setLayout(new BorderLayout(0, 0));

				JPanel namePanel = new JPanel();
				infoPanel.add(namePanel, BorderLayout.NORTH);
				namePanel.setLayout(new BorderLayout(0, 0));

				JLabel nameLabel = new JLabel("<html><b>Name</b>");
				nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
				namePanel.add(nameLabel, BorderLayout.NORTH);

				nameField = new JTextField();
				nameField.setToolTipText("What is your name?");
				namePanel.add(nameField, BorderLayout.SOUTH);
				nameField.setColumns(10);

				JPanel ipPanel = new JPanel();
				infoPanel.add(ipPanel, BorderLayout.CENTER);
				ipPanel.setLayout(new BorderLayout(0, 0));

				JLabel ipLabel = new JLabel("<html><b>Server Address</b>");
				ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
				ipPanel.add(ipLabel, BorderLayout.NORTH);

				JPanel panel = new JPanel();
				ipPanel.add(panel, BorderLayout.SOUTH);
				panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

				ipField = new JTextField();
				panel.add(ipField);
				ipField.setColumns(20);

				JPanel buttonPanel = new JPanel();
				infoPanel.add(buttonPanel, BorderLayout.SOUTH);
				buttonPanel.setLayout(new BorderLayout(0, 0));

				JButton connectButton = new JButton("Connect");
				GetInfoFrame.this.getRootPane().setDefaultButton(connectButton);
				buttonPanel.add(connectButton, BorderLayout.EAST);
				connectButton.setEnabled(false);

				connectButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						nameField.setEnabled(false);
						ipField.setEnabled(false);
						connectButton.setEnabled(false);
						connectButton.setText("Connecting...");

						synchronized (GetInfoFrame.this) {
							GetInfoFrame.this.notifyAll();
						}

					}
				});
				
				KeyAdapter ka = new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						if ( nameField.getText().trim().isEmpty() ||
							 ipField.getText().trim().isEmpty()  ) {
							connectButton.setEnabled(false);
						} else {
							connectButton.setEnabled(true);
						}
					}
				};
				
				nameField.addKeyListener(ka);
				ipField.addKeyListener(ka);
				
				pack();
				setLocationRelativeTo(null);
				
				doneLoading = true;
			}
		});

	} // end GetInfoFrame()

	String[] getNameAndIP() {
		
		while (!doneLoading) {
			try { Thread.sleep(80); }   // wait for frame to load
			catch (InterruptedException ignore) {}
		}
		
		this.setVisible(true);
		try {
			synchronized (this) {
				this.wait();  // wait for button click
			}
		} catch (InterruptedException e) { e.printStackTrace(); }
		
		this.setVisible(false);
		
		return new String[] { nameField.getText().trim(),
							    ipField.getText().trim()  };
		
	}
}
