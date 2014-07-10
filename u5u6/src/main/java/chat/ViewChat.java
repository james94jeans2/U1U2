package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ViewChat extends JFrame {

	private static final long serialVersionUID = 1L;
	private String userName;
	private JTextArea users, chat;
	private ChatClient client;
	private boolean initialized;
	private JPanel left, right, inputPanel, chatPanel;
	private ChatService server;
	private Date date;
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	public ViewChat (ChatClient client) {
		super("chat : ");
		initialized = false;
		try
		{
			userName = client.getName();
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		this.setTitle("chat : " + userName);
		initialize();
		this.client = client;
		//Setze dem client dies hier als view
		client.setView(this);
		try 
		{
			//Es wird der server erittelt via naming.lookup
			server = (ChatService) Naming.lookup("//localhost/server");
		}
		catch (MalformedURLException | RemoteException | NotBoundException e) 
		{
			e.printStackTrace();
		}
	}

	private void initialize () {
		this.setPreferredSize(new Dimension(200, 200));
		this.setMinimumSize(new Dimension(200, 200));
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.setSize(200, 200);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				//Sollte das fenster geschlossen werden wird logout aufgerufen
				client.logout();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		left = new JPanel(new BorderLayout());
		users = new JTextArea();
		users.setEditable(false);
		left.add(new JScrollPane(users), BorderLayout.CENTER);
		left.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Users"));
		JButton send = new JButton("send");
		left.add(send, BorderLayout.SOUTH);

		right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		chat = new JTextArea();
		chat.setEditable(false);
		chatPanel = new JPanel(new BorderLayout());
		chatPanel.add(new JScrollPane(chat), BorderLayout.CENTER);
		chatPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Chat"));
		right.add(chatPanel);

		final JTextArea input = new JTextArea();
		input.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			//Hier das gewünschte posten per STRG-Enter
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					sendChatMessage(input.getText());
					input.setText("");
				}
			}
		});
		inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(new JScrollPane(input), BorderLayout.CENTER);
		inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Enter Message"));
		right.add(inputPanel);
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMessage(input.getText());
				input.setText("");
			}
		});

		this.add(left);
		this.add(right);
		initialized = true;
		this.setVisible(true);
		this.paint(getGraphics());
	}

	public void sendChatMessage (String message) {
		if (!message.trim().isEmpty()) 
		{
			String out = "";
			date = new Date();
			out += "[" + format.format(date.getTime()) + "]";
			out += userName + ": ";
			out += message.trim();
			try 
			{
				server.send(out);
			} 
			catch (RemoteException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public void addChatMessage (String message) {
		chat.append(message + "\n");
		chat.setCaretPosition(chat.getText().length()-1);
	}

	public void paint (Graphics g) {
		if (initialized) 
		{
			int width = (this.getWidth() / 10) * 3;
			if (width > 100) 
			{
				width = 100;
			}
			left.setPreferredSize(new Dimension(width, this.getHeight()));
			if (width == 100) 
			{
				width = this.getWidth() - 100;
			}
			right.setPreferredSize(new Dimension(width, this.getHeight()));
			chatPanel.setPreferredSize(new Dimension(right.getWidth(), right.getHeight()));
			inputPanel.setPreferredSize(new Dimension(right.getWidth(), right.getHeight() / 4));
		}
		super.paint(g);
	}

	public void updateUsers (List<String> list) {
		if (initialized) 
		{
			users.setText("");
			for (String string : list) 
			{
				if (userName.equals(string)) 
				{
					users.append("me\n");
					continue;
				}
				users.append(string + "\n");
			}
		}
	}

}
