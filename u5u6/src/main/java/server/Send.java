package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

public class Send implements Runnable {

	private OutputStream out;
	private Object output;
	private boolean changed;
	private ServerSocket soc;
	
	public Send(OutputStream out,ServerSocket soc){
		this.out=out;
		changed=true;
		this.soc=soc;
	}
	
	public void run() {
		while(!soc.isClosed()){
		   if(changed){
			synchronized (soc) {
				
			
			try(ObjectOutputStream oout = new ObjectOutputStream(out)){
					synchronized (output) {
						oout.writeObject(output);
						oout.flush();
					}
			
				
				
				}catch(IOException e){
					e.printStackTrace();
				}	
			}
		 }
	}		
	}
	
	public void setOut(Object temp){
		synchronized (output) {
			output=temp;
		}
		

	}

}
