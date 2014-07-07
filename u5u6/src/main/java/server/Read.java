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
	private Socket soc;
	private boolean stop, initialized;
	
	public Read(Socket soc, InputStream in, Warehouse wh, Send s){
		//t2=t;
		this.in=in;
		this.wh=wh;
		this.s=s;
		stop = false;
		initialized = false;
		this.soc=soc;
	}
	
	@Override
	public void run() {
		ObjectInputStream oin = null;
		while(!soc.isClosed() && !stop){
			String input = "";
			synchronized (soc) {
				
				if (!initialized) {
					try {
						oin = new ObjectInputStream(in);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					initialized = true;
				}
				try{
					Object indata;
					indata = oin.readObject();
					if(indata instanceof String){
						input = (String)indata;
						if(authentifizierung(input)){
							indata = oin.readObject();
							if(indata instanceof Order){
								s.setOut(true);					
								
								Order inorder = (Order)indata;
								System.out.println(inorder.getQuantity());
								s.setOut(inorder);
								wh.addOrder(inorder);
								
							}
						}else{
							System.out.println(input);
							s.setOut(false);
							
						}
					}
					
				}catch(IOException e){
					e.printStackTrace();
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
					stop = true;
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
