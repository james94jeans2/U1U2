package floje.u3u4;

import javax.persistence.*;

//Wichtige Elemente für OpenJPA, Entity sagt es kann serialisiert werden und 
//Table gibt den Namen an wo das Objekt hingespeichert wird
@Entity()
@Table(name = "products")
public class Product implements fpt.com.Product {

	private static final long serialVersionUID = -8282322185786801850L;
	//Gibt an dass die Id der generierteWert der Datenbank ist z.B. bei INSERT
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="products_SEQ") 	
	private long id;
	
	private double price;
	private int quantity;
	private String name;
	
	@Override
	public long getId() {

		return id;
	}
	
	public void setId(long id) {
		
		this.id = id;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}