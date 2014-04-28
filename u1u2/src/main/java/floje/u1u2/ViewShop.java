package floje.u1u2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ViewShop extends JFrame implements Observer {

	private static final long serialVersionUID = 7392956425233883188L;
	private JList<fpt.com.Product> productList;
	private JScrollPane scrollPane;
	private JPanel addPanel, headingPanel, addNamePanel, addPricePanel, addQuantityPanel, addButtonPanel, fieldPanel, addAreaPanel, deleteButtonPanel;
	private JTextField addNameField, addPriceField, addQuantityField;
	private JLabel addLabel, addNameLabel, addPriceLabel, addQuantityLabel;
	private JButton addButton, deleteButton;
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
		
		productList = new JList<fpt.com.Product>();
		productList.setCellRenderer(new ListProductRenderer());
		productList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				updateDeleteButton();
			}
		});
//		productList.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focusLost(FocusEvent e) {
//				focusGained(e);
//			}
//			
//			@Override
//			public void focusGained(FocusEvent e) {
//				updateDeleteButton();
//			}
//		});
		scrollPane = new JScrollPane(productList);
		scrollPane.setPreferredSize(new Dimension((int)(width * 0.6), height));
		this.add(scrollPane);
		
		CaretListener caretListener = new CaretListener() {
			
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
	public void update(Observable arg0, Object arg1) {
		productList.setListData(((ModelShop)arg0).toArray());
	}
	
	private void updateDeleteButton () {
		deleteButton.setEnabled(!productList.getSelectedValuesList().isEmpty());
	}
	
	private void updateAddButton (boolean empty) {
		if (empty) {
			addButton.setEnabled(false);
			return;
		}
		if (!"".equals(addNameField.getText()) && !"".equals(addPriceField.getText())
				&& !"".equals(addQuantityField.getText())) {
			addButton.setEnabled(true);
			return;
		}
		addButton.setEnabled(false);
	}
	
	public void addActionListener (ActionListener a) {
		addButton.addActionListener(a);
		deleteButton.addActionListener(a);
	}
	
	public List<fpt.com.Product> getSelected () {
		return productList.getSelectedValuesList();
	}
	
	public fpt.com.Product getNewProduct () {
		if (!"".equals(addNameField.getText()) && !"".equals(addPriceField)
				&& !"".equals(addQuantityField)) {
			fpt.com.Product product = new Product();
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
	
	public void paint (Graphics g) {
		scrollPane.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		addPanel.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		super.paint(g);
	}
	
}
