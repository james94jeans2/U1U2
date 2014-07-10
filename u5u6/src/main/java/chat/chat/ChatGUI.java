package chat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

public class ChatGUI implements ActionListener {
	
	JFrame frame, clientFrame;
	JTextField textField, clientName;
	JPanel mainPanel, actionPanel, clientPanel;
	JTextArea textPanel;
	JButton send, login, logout;
	KeyStroke keyStroke;
	String keyStrokeString;

	ChatService server;
	String userName;
	ChatClient client;
	//ClientLink link;
	private static ChatGUI instance;
	

	public static ChatGUI getInstance () {
		return instance;
	}

	public ChatGUI() throws RemoteException, MalformedURLException, NotBoundException {
	
		server = (ChatService) Naming.lookup("//localhost/server");
		instance = this;
		//link = new ClientLinkImpl(this);
		
		frame = new JFrame("Workshop Chat");
		clientFrame = new JFrame("Login");
		
		frame.setSize(400, 250);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		clientFrame.setSize(250, 100);
		clientFrame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		
		textPanel = new JTextArea();
		actionPanel = new JPanel();
		clientPanel = new JPanel();
		textField = new JTextField();
		clientName = new JTextField();
		
		send = new JButton("send");
		login = new JButton("login");
		logout = new JButton("logout");
		
		textPanel.setLayout(new BorderLayout() );
		actionPanel.setLayout(new BorderLayout() );
		clientPanel.setLayout(new GridLayout(3,1));
		
		send.addActionListener(this);
		login.addActionListener(this);
		logout.addActionListener(this);
		
		textField.addKeyListener(new KeyAdapter() {  
			 public void keyPressed(KeyEvent ke )  
			 {  
			 //System.out.println("key pressed fired");  
				 int code = ke.getKeyCode();  
				 int modifiers = ke.getModifiers();  
				 if(code == KeyEvent.VK_ENTER && modifiers == KeyEvent.CTRL_MASK)  
				 {  
					try {
						server.send(userName + ":" + textField.getText());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					textField.setText("");
				 }  
				}  
			}); 
		
		mainPanel.add(new JScrollPane (textPanel, 
	            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
	            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));
		
		actionPanel.add(textField);
		actionPanel.add(send, BorderLayout.EAST);
		
		textPanel.add(actionPanel, BorderLayout.SOUTH);

		
		clientPanel.add(clientName);
		clientPanel.add(login, BorderLayout.SOUTH);
		clientPanel.add(logout, BorderLayout.SOUTH);


		frame.add(mainPanel);
		//frame.pack();
		frame.setVisible(true);
		clientFrame.add(clientPanel);
		//clientFrame.pack();
		clientFrame.setVisible(true);
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send) {
			try {
				server.send(userName + ":" + textField.getText());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			textField.setText("");
		}
		if (e.getSource() == login) {
			userName = clientName.getText();
			try {
				this.client = new ChatClient(userName);
				//client.setLink(link);
			} catch (RemoteException | MalformedURLException
					| NotBoundException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == logout) {
			try {
				client.logout();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
	
	public void receiveMessage(String message) {
	    textPanel.append(message+"\n");
	    textPanel.setCaretPosition(textPanel.getText().length()-1);
	  }
	
	public static void main (String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		new ChatGUI();
	}
	





	
}
