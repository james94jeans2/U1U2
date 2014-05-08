package floje.u1u2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;

import javax.swing.JMenuItem;

import listener.*;

import com.thoughtworks.xstream.io.StreamException;

import strategies.BaseStrategy;
import strategies.BinaryStrategy;
import strategies.XMLStrategy;
import strategies.XStreamStrategy;

public class ControllerShop implements ActionListener, AddListener, DeleteListener{
	private ModelShop mShop;
	private ViewShop vShop;
	private BaseStrategy baseStrat = new BaseStrategy();
	private BinaryStrategy binstrat = new BinaryStrategy();
	private XMLStrategy xmlstrat = new XMLStrategy();
	private XStreamStrategy xstreamstrat = new XStreamStrategy();
	//Nur um den ViewCustomer zu testen
	//ViewCustomer vCust;

	//Hier werden Model und View verkn�pft und den Buttons werden
	//Add- und DeleteListener hinzugef�gt
	public void link(ModelShop mShop, ViewShop vShop){
		this.mShop = mShop;
		this.mShop.addObserver(vShop);

		this.vShop = vShop;
		this.vShop.addAddListener(this);
		this.vShop.addDeleteListener(this);
		this.vShop.addActionListener(this);
	}
	//Nur Testweise um den ViewCustomer auszuprobieren
	//	public void link(ModelShop mShop, ViewCustomer vCust){
	//		this.mShop = mShop;
	//		this.mShop.addObserver(vCust);
	//
	//		this.vCust = vCust;
	//	}

	//Methode zum Testen der Eingabe, f�ngt die jeweiligen Exceptions
	//ab und liefert einen Fehler an die View
	public Boolean testProductData(String price, String quantity)
	{
		try 
		{
			Double.parseDouble(price);
		} catch (NumberFormatException e) {
			vShop.showError(e.getMessage());
			System.out.println("Invalid Input could not convert to Double");
			return false;
		}
		try
		{
			Integer.parseInt(quantity);
		}
		catch (NumberFormatException e)
		{
			vShop.showError(e.getMessage());
			System.out.println("Invalid Input could not convert to Integer");
			return false;
		}
		return true;
	}

	//Hier wird die deletePerformed Methode implementiert welche das Interface 
	//DeleteListener fordert
	@Override
	public void deletePerfomed(Product product) {
		if(product!=null)
		{
			mShop.delete(product);
		}
		else 
		{
			vShop.showError("Product not valid or nothing selected");	
		}
	}

	//Hier wird die addPerformed Methode implementiert welche das Interface
	//AddListener fordert
	@Override
	public void addPerfomed(Product product) {
		try
		{
			IDGenerator.generateIDForProduct(mShop, product);
		}
		catch(Exception e)
		{
			vShop.showError(e.getMessage());
		}
		mShop.add(product);
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		switch (((JMenuItem)event.getSource()).getText()) {
		case "None":
			baseStrat.setStrat(null);
			vShop.deactivateLoadSaveMenu();
			System.out.println("None");
			break;

		case "Binary":
			baseStrat.setStrat(binstrat);
			vShop.activateLoadSaveMenu();
			System.out.println("Binary");
			break;

		case "Beans":
			baseStrat.setStrat(xmlstrat);
			vShop.activateLoadSaveMenu();
			System.out.println("Beans");
			break;

		case "XStream":
			baseStrat.setStrat(xstreamstrat);
			vShop.activateLoadSaveMenu();
			System.out.println("XStream");
			break;

		case "Load":
			Product loadProduct;
			do {
				loadProduct=null;
				try
				{
					loadProduct = baseStrat.readObject();
					if(loadProduct != null)
					{
						mShop.add(loadProduct);
					}
				}
				catch(IOException | ArrayIndexOutOfBoundsException | StreamException ex)
				{
					if(ex.getClass().equals(EOFException.class)||
							ex.getClass().equals(ArrayIndexOutOfBoundsException.class)||
							ex.getClass().equals(StreamException.class))
					{
						break;
					}
					else
					{
						vShop.showError(ex.getMessage());
					}
				}				
			} while (loadProduct != null);
			try
			{
				baseStrat.close();
			}
			catch(IOException ex)
			{
				vShop.showError(ex.getMessage());
			}
			break;

		case "Save":
			for(fpt.com.Product saveProduct : mShop)
			{
				try
				{
					baseStrat.writeObject(saveProduct);

				}
				catch(IOException ex)
				{
					vShop.showError(ex.getMessage());
				}
			}
			try
			{
				baseStrat.close();
			}
			catch(IOException ex)
			{
				vShop.showError(ex.getMessage());
			}
			break;

		default:
			break;
		}
	}
}