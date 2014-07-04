package floje;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField fieldUserName;
	private JPasswordField fieldPassword;
	private JLabel lUsername, lPassword;
	private JButton bLogin, bCancel;
	private boolean succeeded;
	
	public LoginDialog (JFrame parent) {
		super(parent, "Login", true);
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		
		cs.fill = GridBagConstraints.HORIZONTAL;
		
		lUsername = new JLabel("Username: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lUsername, cs);
		
		fieldUserName = new JTextField();
		cs.gridx = 1;
		cs.gridwidth = 2;
		panel.add(fieldUserName, cs);
		
		lPassword = new JLabel("Password: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		
	}

}
