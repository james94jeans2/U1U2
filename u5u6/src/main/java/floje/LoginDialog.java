package floje;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField fieldUserName;
	private JPasswordField fieldPassword;
	private JLabel lUsername, lPassword;
	private JButton bLogin, bCancel;
	private boolean succeeded;
	private String username, password;
	
	public LoginDialog (final JFrame parent) {
		super(parent, "Login", true);
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		
		lUsername = new JLabel("Username: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lUsername, cs);
		
		fieldUserName = new JTextField(20);
		cs.gridx = 1;
		cs.gridwidth = 2;
		panel.add(fieldUserName, cs);
		
		lPassword = new JLabel("Password: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lPassword, cs);
		
		fieldPassword = new JPasswordField(20);
		cs.gridx = 1;
		cs.gridwidth = 2;
		panel.add(fieldPassword, cs);
		
		final LoginDialog dia = this;
		
		bLogin = new JButton("Login");
		bLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						((ViewCostumer)parent).performOrder(dia);
					}
				});
				dispose();
			}
		});
		
		bCancel = new JButton("Cancel");
		bCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(bLogin);
		buttonPanel.add(bCancel);
		
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	public String getUsername () {
		return fieldUserName.getText().trim();
	}
	
	public String getPassword () {
		return new String(fieldPassword.getPassword());
	}
	
	public boolean isSucceeded () {
		return succeeded;
	}
	
}
