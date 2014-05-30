package floje.u3u4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import listener.*;

public class ViewShop extends JFrame implements Observer {

	private static final long serialVersionUID = 7392956425233883188L;
	private JList<Product> productList;
	private DefaultListModel<Product> productModel;
	private JScrollPane scrollPane;
	private JPanel addPanel, headingPanel, addNamePanel, addPricePanel, addQuantityPanel, 
	addButtonPanel, fieldPanel, addAreaPanel, deleteButtonPanel;
	private JTextField addNameField, addPriceField, addQuantityField;
	private JLabel addLabel, addNameLabel, addPriceLabel, addQuantityLabel;
	private JButton addButton, deleteButton;
	private JRadioButtonMenuItem noneRadio,binRadio,beanRadio,xStreamRadio, jdbcRadio, openJPARadio;
	private ButtonGroup buttonGroup;
	private JMenuBar menubar;
	private JMenu strategyMenu, loadSaveMenu;
	private JMenuItem loadItem, saveItem;
	private static final int width = 800, height = 400;

	public ViewShop () {
		super("PC-Hardware Shop Administration");
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(650, 275));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		int screenWidth, screenHeight;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		this.setLocation((screenWidth - width) / 2, (screenHeight - height) / 2);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

		productList = new JList<Product>();
		productModel = new DefaultListModel<Product>();
		productList.setCellRenderer(new ListProductRenderer());
		productList.setModel(productModel);
		productList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				updateDeleteButton();
			}
		});

		//Setzen der radioButtonItems und Hinzufügen der Komponenten zur Menubar
		//Die Menubar wird dann gesetzt
		noneRadio = new JRadioButtonMenuItem("None");
		binRadio = new JRadioButtonMenuItem("Binary");
		beanRadio = new JRadioButtonMenuItem("Beans");
		xStreamRadio = new JRadioButtonMenuItem("XStream");
		jdbcRadio = new JRadioButtonMenuItem("JDBC Serialization");
		openJPARadio = new JRadioButtonMenuItem("OpenJPA Serialization");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(noneRadio);
		buttonGroup.add(binRadio);
		buttonGroup.add(beanRadio);
		buttonGroup.add(xStreamRadio);
		buttonGroup.add(jdbcRadio);
		buttonGroup.add(openJPARadio);
		buttonGroup.setSelected(noneRadio.getModel(), true);
		loadItem = new JMenuItem("Load");
		saveItem = new JMenuItem("Save");
		strategyMenu = new JMenu("Strategy");
		strategyMenu.add(noneRadio);
		strategyMenu.add(binRadio);
		strategyMenu.add(beanRadio);
		strategyMenu.add(xStreamRadio);
		strategyMenu.add(jdbcRadio);
		strategyMenu.add(openJPARadio);
		loadSaveMenu = new JMenu("Load/Save");
		loadSaveMenu.add(loadItem);
		loadSaveMenu.add(saveItem);
		loadSaveMenu.setEnabled(false);
		menubar = new JMenuBar();
		menubar.add(strategyMenu);
		menubar.add(loadSaveMenu);
		this.setJMenuBar(menubar);

		//ScrollPane mit der Productlist wird erstellt und hinzugefügt
		scrollPane = new JScrollPane(productList);
		scrollPane.setPreferredSize(new Dimension((int)(width * 0.6), height));
		this.add(scrollPane);

		//Listener für den Cursor damit Veränderungen erkannt werden können
		CaretListener caretListener = new CaretListener() {

			//Anonyme innere Klasse, welche das caretUpdate überschreibt mit passenden
			//Funktionen
			@Override
			public void caretUpdate(CaretEvent e) {
				JTextField textField = ((JTextField)e.getSource());
				if (textField != addNameField) {
					textField.getInputVerifier().verify(textField);	
				}
				updateAddButton(false);
			}
		};

		addNameField = new JTextField();
		addNameField.addCaretListener(caretListener);
		addNameLabel = new JLabel("  Name:");
		Font font = addNameLabel.getFont();
		font = font.deriveFont(Font.BOLD, font.getSize() * 1.2f);
		addNameLabel.setFont(font);
		addNamePanel = new JPanel(new GridLayout(2, 1));
		addNamePanel.add(addNameLabel);
		addNamePanel.add(addNameField);
		addNamePanel.setBorder(BorderFactory.createEmptyBorder());

		addPriceField = new JTextField();
		//Anonyme innere Klasse, die die Verify Methode überschreibt
		//und die Konvertierung auf Double prüft
		addPriceField.setInputVerifier(new InputVerifier() {

			@Override
			public boolean verify(JComponent input) {
				JTextField addPrice = (JTextField) input;
				addPrice.setBackground(Color.WHITE);
				if ("".equals(addPrice.getText())) {
					updateAddButton(true);
					return true;
				}
				try {
					Double.parseDouble(addPrice.getText());
				} catch (Exception e) {
					addPrice.setBackground(Color.RED);
					return false;
				}
				updateAddButton(false);
				return true;
			}
		});
		addPriceField.addCaretListener(caretListener);
		addPriceLabel = new JLabel("  Preis:");
		addPriceLabel.setFont(font);
		addPricePanel = new JPanel(new GridLayout(2, 1));
		addPricePanel.add(addPriceLabel);
		addPricePanel.add(addPriceField);
		addPricePanel.setBorder(BorderFactory.createEmptyBorder());

		addQuantityField = new JTextField();
		//Anonyme innere Klasse, die die Verify Methode überschreibt
		//und die Konvertierung auf Integer prüft
		addQuantityField.setInputVerifier(new InputVerifier() {

			@Override
			public boolean verify(JComponent input) {
				JTextField addQuantity = (JTextField) input;
				addQuantity.setBackground(Color.WHITE);
				if ("".equals(addQuantity.getText())) {
					updateAddButton(true);
					return true;
				}
				try {
					Integer.parseInt(addQuantity.getText());
				} catch (Exception e) {
					addQuantity.setBackground(Color.RED);
					return false;
				}
				updateAddButton(false);
				return true;
			}
		});
		addQuantityField.addCaretListener(caretListener);
		addQuantityLabel = new JLabel("  Anzahl:");
		addQuantityLabel.setFont(font);
		addQuantityPanel = new JPanel(new GridLayout(2, 1));
		addQuantityPanel.add(addQuantityLabel);
		addQuantityPanel.add(addQuantityField);
		addQuantityPanel.setBorder(BorderFactory.createEmptyBorder());

		addButton = new JButton("hinzufügen");
		addButton.setFont(font);
		addButton.setActionCommand("add");
		addButton.setEnabled(false);

		addButtonPanel = new JPanel();
		addButtonPanel.setLayout(new FlowLayout());
		addButtonPanel.add(addButton);

		fieldPanel = new JPanel(new GridLayout(3, 1));
		fieldPanel.add(addNamePanel);
		fieldPanel.add(addPricePanel);
		fieldPanel.add(addQuantityPanel);

		addAreaPanel = new JPanel();
		addAreaPanel.setLayout(new BoxLayout(addAreaPanel, BoxLayout.Y_AXIS));
		addAreaPanel.add(fieldPanel);
		addAreaPanel.add(addButtonPanel);

		deleteButton = new JButton("ausgewähltes Produkt löschen");
		deleteButton.setFont(font);
		deleteButton.setEnabled(false);
		deleteButton.setActionCommand("delete");

		deleteButtonPanel = new JPanel(new FlowLayout());
		deleteButtonPanel.add(deleteButton);

		addLabel = new JLabel(" Produkt hinzufügen");
		font = addLabel.getFont();
		font = font.deriveFont(Font.BOLD, font.getSize() * 2);
		addLabel.setFont(font);

		headingPanel = new JPanel();
		headingPanel.setLayout(new GridLayout(1, 1));
		headingPanel.add(addLabel);

		addPanel = new JPanel();
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		addPanel.add(Box.createVerticalGlue());
		addPanel.add(headingPanel);
		addPanel.add(Box.createVerticalStrut(5));
		addPanel.add(addAreaPanel);
		addPanel.add(Box.createVerticalGlue());
		addPanel.add(deleteButtonPanel);
		this.add(addPanel);

		this.pack();
		this.setVisible(true);
		addNameField.requestFocus();
	}

	@Override
	public void update(Observable o, Object arg) {
		//Wenn das Model eine Änderung mitgeteilt hat, werden nicht vorhande
		//Produkte der Liste hinzugefügt und gelöschte Objekte gegebenenfalls
		//entfernt
		if(arg.getClass().equals(AddEvent.class))
		{
			productModel.addElement(((AddEvent)arg).getProduct());
		}
		else
		{
			if(arg.getClass().equals(DeleteEvent.class))
			{
				productModel.removeElement(((DeleteEvent)arg).getProduct());
			}
		}
	}

	//Hinzufügen der Listener damit wir die Strategien wechseln können
	public void addActionListener (ActionListener a) {
		noneRadio.addActionListener(a);
		binRadio.addActionListener(a);
		beanRadio.addActionListener(a);
		xStreamRadio.addActionListener(a);
		jdbcRadio.addActionListener(a);
		openJPARadio.addActionListener(a);
		loadItem.addActionListener(a);
		saveItem.addActionListener(a);
	}

	//Methode für den AddListener
	public void addAddListener(final AddListener a) {
		//Anonyme innere Klasse zur Erstellung eines spezielleren ActionListeners
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				a.addPerfomed((Product)getNewProduct());
			}
		});
	}


	//Methode für den DeleteListener
	public void addDeleteListener(final DeleteListener a) {
		//Anonyme innere Klasse zur Erstellung eines spezielleren ActionListeners
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				a.deletePerfomed(productList.getSelectedValuesList().toArray(new Product[0]));
			}
		});
	}

	//Methode zum anzeigen von geworfenen Exceptions
	public void showError(String message)
	{
		JOptionPane.showConfirmDialog(this, message 
				,"Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}

	//Methoden zum aktivieren und deaktivieren des loadsavemenus
	public void deactivateLoadSaveMenu()
	{
		loadSaveMenu.setEnabled(false);		
	}
	public void activateLoadSaveMenu()
	{
		loadSaveMenu.setEnabled(true);
	}
	//Das gleiche nur fuer den Save-Button
	public void deactivateSaveMenu()
	{
		saveItem.setEnabled(false);		
	}
	public void activateSaveMenu()
	{
		saveItem.setEnabled(true);
	}

	//Methode zur Prüfung ob der deleteButton ausgeblendet werden soll
	private void updateDeleteButton () {
		deleteButton.setEnabled(!productList.getSelectedValuesList().isEmpty());
	}

	//Methode zur Prüfung ob der addButton ausgeblendet werden soll
	private void updateAddButton (boolean empty) {
		if (empty) {
			addButton.setEnabled(false);
			return;
		}
		if (textFieldsNotEmpty()) {
			addButton.setEnabled(true);
			return;
		}
		addButton.setEnabled(false);
	}

	//Methode zur Rückgabe des ausgewählten Produktes
	public List<Product> getSelected () {
		return productList.getSelectedValuesList();
	}

	public Product getNewProduct () {
		if (textFieldsNotEmpty()) {
			Product product = new Product();
			product.setName(addNameField.getText());
			product.setPrice(Double.parseDouble(addPriceField.getText()));
			product.setQuantity(Integer.parseInt(addQuantityField.getText()));
			addNameField.setText("");
			addPriceField.setText("");
			addQuantityField.setText("");
			return product;
		}
		return null;
	}

	//Methode zur Prüfung ob die Textfiels leer sind weil das mehrmals geprüft wird
	public Boolean textFieldsNotEmpty()
	{
		if (!"".equals(addNameField.getText()) && !"".equals(addPriceField.getText())
				&& !"".equals(addQuantityField.getText()))
		{
			return true;
		}
		return false;
	}
	//Paint Methode
	public void paint (Graphics g) {
		scrollPane.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		addPanel.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		super.paint(g);
	}
}