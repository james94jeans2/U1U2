package floje;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import chat.ChatClient;
import chat.ChatService;
import chat.ViewChat;
import listener.AddEvent;
import listener.DeleteEvent;
import listener.OrderListener;

public class ViewCostumer extends JFrame implements Observer{

	private static final long serialVersionUID = -5399845903830908290L;
	private static final int width = 800, height = 400;
	private JPanel rightSide = new JPanel(), leftSide;
	private JTable productTable;
	private Object[][] data;
	private  JScrollPane scrollPane;
	private JPanel buttonPanel;
	private JList<Order> orderList; 
	DefaultTableModel modelt;
	private String[] columnNames = {"Name",
			"Preis",
			"MaxCount",
	"OrderCount"};
	private DatagramSocket datagramm;
	private final JLabel date;
	private JButton ok, chat;
	private ProductList products;
	private OrderListener listener;
	private static ViewCostumer instance;

	public ViewCostumer(){
		super("PC-Hardware Shop Costumer");
		instance = this;
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.X_AXIS));
		int screenWidth, screenHeight;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		this.setLocation(((screenWidth - width) / 2), ((screenHeight - height) / 2)+height);
		products = new ProductList();

		orderList = new JList<Order>();

		orderList.setCellRenderer(new ListOrderRenderer());
		leftSide = new JPanel(new BorderLayout());		
		leftSide.add(new JScrollPane(orderList), BorderLayout.CENTER);
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		rightSide.setLayout(new BorderLayout());;



		data=new Object[0][4];


		productTable=new JTable(consModel());



		scrollPane = new JScrollPane(productTable);

		productTable.setFillsViewportHeight(true);


		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		ok = new JButton("ok");
		ok.setPreferredSize(new Dimension(100,70));


		buttonPanel.add(ok, BorderLayout.WEST);
		date=new JLabel("");
		buttonPanel.add(date, BorderLayout.CENTER);
		chat = new JButton("open chat");
		chat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = JOptionPane.showInputDialog(instance, "Enter your desired username, please!");
				//Falls einfach abbrechen geklickt wurde
				if (username == null)
				{
					System.out.println("User aborted login");
					return;
				}
				//Falls sich jemand den Spaß mach und einfach nix eingibt
				else if(username.equals(""))
				{
					System.out.println("User did not enter a name");
					return;
				}
				if (username.contains(" ")) {
					username = username.trim();
					username = username.replaceAll(" ", "_");
					int option = JOptionPane.showOptionDialog(instance, "The username can not contain any spaces! Want to chat as \"" + username + "\"?", "Illegal Username", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					if (option == JOptionPane.NO_OPTION) {
						actionPerformed(e);
						return;
					}
					if (option == JOptionPane.CLOSED_OPTION) {
						return;
					}
				}
				try
				{
					//Hier wird der Chat integriert und das Fenster gestartet
					ChatClient client = new ChatClient(username);
					new ViewChat(client);
				} 
				catch (IllegalArgumentException ex)
				{
					JOptionPane.showMessageDialog(instance, ex.getMessage(), "Login failed", JOptionPane.ERROR_MESSAGE);
				} 
				catch (RemoteException | MalformedURLException  | NotBoundException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(chat, BorderLayout.EAST);




		rightSide.add(scrollPane, BorderLayout.CENTER);
		rightSide.add(buttonPanel, BorderLayout.SOUTH);

		this.add(leftSide);
		this.add(rightSide);

		this.pack();

		this.setVisible(true);
		//Hier kommt das Netzwerkgedöns für das datum per udp
		try 
		{
			datagramm = new DatagramSocket();
		} 
		catch (SocketException e)
		{
			datagramm = null;
			e.printStackTrace();
		}
		try
		{
			abfrageDatum();
			abrufenDatum();
		} 
		catch (SocketException e) {
			e.printStackTrace();
		}




	}

	@Override
	public void update(Observable o, Object arg) {
		//Wenn das Model eine Änderung mitgeteilt hat, werden nicht vorhande
		//Produkte der Liste hinzugefügt und gelöschte Objekte gegebenenfalls
		//entfernt
		if (arg != null) {
			if(arg.getClass().equals(AddEvent.class))
			{
				products.add(((AddEvent)arg).getProduct());
			}
			else
			{
				if(arg.getClass().equals(DeleteEvent.class))
				{
					products.remove(((DeleteEvent)arg).getProduct());
				}
			}

			data=new Object[products.size()][4];

			Product[] product = products.toArray(new Product[0]);

			for(int i=0;i<product.length;i++){


				data[i][0]=product[i].getName();
				data[i][1]=product[i].getPrice();
				data[i][2]=product[i].getQuantity();


			}		
			productTable.setModel(consModel());	
		} else {
			orderList.setListData(((ModelShop) o).getOrders().toArray(new Order[0]));
		}
	}

	public void paint (Graphics g) {
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		super.paint(g);
	}

	private DefaultTableModel consModel(){
		modelt = new DefaultTableModel(data,columnNames){
			private static final long serialVersionUID = -4503641078531367197L;

			public boolean isCellEditable(int x, int y) {
				if(y==3){
					return true;
				}
				return false;
			}

			public Class<?> getColumnClass(int columnCount){
				if (columnCount==3){
					return Integer.class;
				}
				return String.class;

			}


		};
		return modelt;
	}

	//datum abfragen per udp
	private void abfrageDatum() throws SocketException{
		InetAddress address  = null;
		try 
		{
			//Internetaddresse von localhost wird gesucht
			address = InetAddress.getByName("localhost");
		}
		catch (UnknownHostException e2) 
		{
			e2.printStackTrace();
		}
		System.out.println("Anfrage");
		final String command = "DATE:";
		final byte buffer[];
		//Command wird in bytes gewandelt
		buffer = command.getBytes();
		//DatagramPacket wird gebaut
		final DatagramPacket packet = new DatagramPacket(
				buffer,buffer.length, address, 6667);
		//Timer wird gebaut und nachher gestartet damit uns die gui durch 
		//die vielen zeitabfragen nicht einfriert
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try 
				{
					//Das vorher vorbereitete Packet wird gesendet
					datagramm.send(packet);
				} 
				catch (IOException e1) {
					e1.printStackTrace();

				}
			}
		}, 0, 1000);
	}

	//Hier der Abruf per receive()
	private void abrufenDatum() throws SocketException{
		System.out.println("Abfrage");
		System.out.println(""+datagramm.getPort());
		//Wir bauen auch hier einen Timer damit da getrennt von der Anzeige läuft
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				//das Packet wird vorbereitet
				byte answer[] = new byte[1024];
				DatagramPacket inpack = new DatagramPacket(answer, answer.length);
				try 
				{
					//Und wird per reveive empfangen
					datagramm.receive(inpack);
				} 
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				//Hier wird dann der Text gesetzt via invokeLater, weil wir
				//die Änderung in den GUI-Thread kriegen müssen
				final String ding = new String(inpack.getData()).trim();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						date.setText(ding);
					}
				});

			}
		}, 0, 1000);
	}

	public JTable getTable(){
		return productTable;
	}

	public void addActionListener (ActionListener listener) {
		ok.addActionListener(listener);
	}

	public void addOrderListener (OrderListener listener) {
		this.listener = listener;
	}

	//Die Methode zur Ausführung der Order
	public void performOrder (LoginDialog dialog) {
		//Die Nutzerdaten werden abgefragt
		String login = "";
		login += dialog.getUsername();
		login += ":";
		login += dialog.getPassword();
		//Es wird eine order gebildet
		Order order = new Order();
		
		@SuppressWarnings("unchecked")
		Vector<Vector<Object>> data = modelt.getDataVector();
		int j;
		//Hier wird das Objekt welches wir aus der Tabelle geholt haben 
		//zerlegt und daraus ein Product gebastelt das in die order hinzugefügt wird
		for (int i = 0; i < data.size(); ++i) {
			if (((Integer)data.get(i).get(3)) != null) {
				j = (Integer) data.get(i).get(3);
				if (j > 0) {
					Product product = (Product) products.get(i);
					product.setQuantity(j);
					order.add(product);
				}
			}
		}
		//Dann wird die orderperformed methode aufgerufen mit dem login und der 
		//gebauten order
		listener.orderPerformend(login, order);
	}

	public static ViewCostumer getInstance () {
		//Gibt die instanz der ViewCustomer zurück
		return instance;
	}

	//Error for wrong Login
	public void showLoginError () {
		JOptionPane.showMessageDialog(this, "Wrong Login Credentials, please try again!", "Authentification Error", JOptionPane.ERROR_MESSAGE);
	}

}
