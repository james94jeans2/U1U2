package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import fpt.com.Order;

public class Read implements Runnable{

	private InputStream in;
	//private Thread t2;
	private Send s;
	private Warehouse wh;
	private ServerSocket soc;
	
	public Read(ServerSocket soc, InputStream in, Warehouse wh, Send s){
		//t2=t;
		this.in=in;
		this.wh=wh;
		this.s=s;
		this.soc=soc;
	}
	
	@Override
	public void run() {
		while(!soc.isClosed()){
			
			

			String input = "";
			synchronized (soc) {
				try(ObjectInputStream oin = new ObjectInputStream(in);){
					Object indata;
					indata = oin.readObject();
					if(indata instanceof String){
						input = (String)indata;
						if(authentifizierung(input)){
							if(indata instanceof Order){
								s.setOut(true);					
								
								Order inorder = (Order)indata;
								s.setOut(inorder);
								wh.addOrder(inorder);
								
							}
						}else{
							s.setOut(false);
							
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
	}
	
	private boolean authentifizierung(String auth) {
		if(auth.contains("admin:admin")){
			return true;
		}
		return false;
	}

}
