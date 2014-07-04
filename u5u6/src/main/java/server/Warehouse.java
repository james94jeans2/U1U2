package server;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;

import fpt.com.Order;
import fpt.com.Product;

public class Warehouse implements Runnable {

	private CopyOnWriteArrayList<Order> orders;
	public boolean changed=false;
	
	public void addOrder(Order order){
		orders.add(order);
		changed=true;
	}
	

	@Override
	public void run() {
		CopyOnWriteArrayList<Pair<Product, Integer>> list = null;
		int i = 0;
		while(true){
			if(changed){
				System.out.println("Orders: "+orders);
				double ges=0;
				for(Order o: orders){
					for(Product p : o){
						ges+=p.getPrice();
						
						i = list.get(list.indexOf(p)).getRight();
						if(list.contains(Pair.of(p,i))){
							i++;
							Pair.of(p, i-1).setValue(i);
							
						}
					}
				}
				String ordered="";
				for(int k = 0; k<list.size();k++){
					ordered+="Product: "+list.get(k).getLeft()+" wurde "+list.get(k).getRight()+" mal bestellt./n";
				}
				System.out.println("Gesammtpreis: "+ges);
				System.out.println(ordered);
			}
		}
		
	}
	
	
	
}
