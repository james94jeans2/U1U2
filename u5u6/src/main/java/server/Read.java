package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import floje.Order;

public class Read implements Runnable{

	private InputStream in;
	//private Thread t2;
	private Send s;
	private Warehouse wh;
	private Socket soc;
	private boolean stop;
	
	public Read(Socket soc, InputStream in, Warehouse wh, Send s){
		//t2=t;
		this.in=in;
		this.wh=wh;
		this.s=s;
		stop = false;
		this.soc=soc;
	}
	
	@Override
	public void run() {
		ObjectInputStream oin = null;
		synchronized (soc) {
			
				try {
					oin = new ObjectInputStream(in);
					System.out.println("ois initialized");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		while(!soc.isClosed() && !stop){
			String input = "";
			//synchronized (soc) {
				try{
					Object indata;
					indata = oin.readObject();
					if(indata instanceof String){
						input = (String)indata;
						if(authentifizierung(input)){
							s.setOut(true);	
							System.out.println("waiting for Data");
							indata = oin.readObject();
							if(indata instanceof Order){
								Order inorder = (Order)indata;
								s.setOut(inorder);
								System.out.println("hello");
								wh.addOrder(inorder);
							}
						}else{
							System.out.println(input);
							s.setOut(false);
							
						}
					}
					if (indata instanceof Integer) {
						if ((Integer)indata == -1) {
							stop = true;
						}
					}
				}catch(IOException e){
					e.printStackTrace();
					stop = true;
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
					stop = true;
				}
			}
		System.out.println("Shutting down ois!");
		try {
			oin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//}		
	}
	
	private boolean authentifizierung(String auth) {
		if(auth.contains("admin:admin")){
			return true;
		}
		return false;
	}

}
