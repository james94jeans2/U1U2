package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Send implements Runnable {

	private OutputStream out;
	private Object output;
	
	
	public Send(OutputStream out){
		this.out=out;
	}
	
	public void run() {
		while(true){
			synchronized (out) {
				
			
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
	
	public void setOut(Object temp){
		synchronized (output) {
			output=temp;
		}
		

	}

}
