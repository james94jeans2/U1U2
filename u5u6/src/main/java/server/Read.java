package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import fpt.com.Order;

public class Read implements Runnable{

	private InputStream in;
	private Thread t2;
	private Send s;
	private Warehouse wh;
	
	public Read(Thread t, InputStream in, Warehouse wh, Send s){
		t2=t;
		this.in=in;
		this.wh=wh;
		this.s=s;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				t2.wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String input = "";
				try{
					ObjectInputStream oin = new ObjectInputStream(in);
					Object indata;
					indata = oin.readObject();
					if(indata instanceof String){
						input = (String)indata;
						if(authentifizierung(input)){
							if(indata instanceof Order){
								s.setOut(true);
								t2.notify();
								Order inorder = (Order)indata;
								wh.addOrder(inorder);
								t2.notify();
							}
						}else{
							s.setOut(false);
							t2.notify();
						}
					}
					
				}catch(IOException e){
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();				
				}
			
		}		
	}
	
	private boolean authentifizierung(String auth) {
		if(auth.contains("admin:admin")){
			return true;
		}
		return false;
	}

}
