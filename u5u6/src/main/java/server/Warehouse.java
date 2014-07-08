package server;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;

import fpt.com.Order;
import fpt.com.Product;

public class Warehouse implements Runnable {

	private CopyOnWriteArrayList<Order> orders;
	private boolean changed;
	


	public void addOrder(Order order){
		//synchronized (orders) {
			System.out.println("add Order wh");
			orders.add(order);
			synchronized ((Object)changed) {
			changed=true;
			}
			System.out.println("add complete");
		//}
		
	}
	

	@Override
	public void run() {
		CopyOnWriteArrayList<Pair<Product, Integer>> list = new CopyOnWriteArrayList<>();
		orders = new CopyOnWriteArrayList<Order>();
		changed=false;
		int i = 0;
		while(true){
			synchronized((Object)changed) {
			if(changed){
				System.out.println("geändert");
				synchronized (orders) {
					System.out.println("Orders: "+orders);
					double ges=0;
			
					for(Order o: orders){
						for(Product p : o){
							ges+=p.getPrice();						
								if(!list.contains(p)){
									list.add(Pair.of(p,p.getQuantity()));
								}else{
									i=p.getQuantity();
									i+=list.get(list.indexOf(p)).getValue();
									list.get(list.indexOf(p)).setValue(i);
								}
						}	
						
					}
					String ordered="";
					for(int k = 0; k<list.size();k++){
						ordered+="Product: "+list.get(k).getLeft().getName()+" wurde "+list.get(k).getRight()+" mal bestellt.\n";
					}
					System.out.println("Gesammtpreis: "+ges);
					System.out.println(ordered);
				}
				changed=false;
			}
			}
		}
		
	}
	
	
	
}
