package server;

import java.util.concurrent.CopyOnWriteArrayList;

import floje.ProductList;
import floje.Order;
import fpt.com.Product;

public class Warehouse implements Runnable {

	private CopyOnWriteArrayList<Order> orders;
	private boolean changed;
	


	public void addOrder(Order order){
		synchronized (orders) {
			orders.add(order);
			synchronized ((Object)changed) {
				changed=true;
			}
		}
		
	}
	

	@Override
	public void run() {
		ProductList list = new ProductList();
		orders = new CopyOnWriteArrayList<Order>();
		changed=false;
		int i = 0;
		String temp,newestOrder,trenn,ordered;
		Double price, ges;
		ges = 0d;
		Product pro;
		trenn="";
		int anzahlP=0;
		for(int k = 0; k<80;k++){
			trenn+="=";
		}
		
		while(true){
			synchronized((Object)changed) {
			if(changed){
				synchronized (orders) {
					newestOrder="";
					for(Product p:orders.get(orders.size()-1)){
							newestOrder+=p.getName();
							newestOrder=fill(newestOrder,p.getName().length(),39);
							newestOrder+=p.getQuantity();
							price = p.getPrice()*p.getQuantity();
							temp= price.toString();
							newestOrder=fill(newestOrder,39+(""+p.getQuantity()).length(),80-temp.length());
							newestOrder+=temp+"\n";
							if(p.getId()==0){
								if(list.findProductByName(p.getName())==null){
									list.add(p);
								}else{
									i=p.getQuantity();
									i+=list.findProductByName(p.getName()).getQuantity();
									list.findProductByName(p.getName()).setQuantity(i);
								}
							}else{
								i=p.getQuantity();
								if(list.findProductById(p.getId())==null){
									list.add(p);
								}else{
									i+=list.findProductById(p.getId()).getQuantity();
									list.findProductById(p.getId()).setQuantity(i);
								}
								
							}
							
					}
					ges += orders.get(orders.size()-1).getSum();
					anzahlP += orders.get(orders.size() - 1).getQuantity();
					ordered="";
					for(int k = 0; k<list.size();k++){ 
						pro=list.get(k);
						ordered+="\n"+pro.getName();
						ordered=fill(ordered,pro.getName().length(),39);
						ordered+=pro.getQuantity();					
						price=pro.getPrice()*pro.getQuantity();
						temp= price.toString();
						ordered=fill(ordered,39+(""+pro.getQuantity()).length(),80-temp.length());
						ordered+=temp;						
					}
					synchronized (System.out) {
						
						System.out.println("... \nOrder eingegangen:");
						System.out.println(newestOrder);
						System.out.println("Orders gesammt:");
						System.out.print(trenn);
						System.out.println(ordered);
						System.out.println(trenn);
						System.out.println("Gesammtanzahl: "+anzahlP);
						System.out.println("Gesammtpreis: "+ges);
					}
				}
				changed=false;
			}
			}
		}
		
	}
	private String fill(String s,int start, int ende){
		for(int k = start;k<ende ;k++){
			s+=" ";
		}
		return s;
	}
	
	
	
}
