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
			
			try{
				ObjectOutputStream oout = new ObjectOutputStream(out);
				synchronized (oout) {
					oout.writeObject(output);
					oout.flush();
				}
			
				
				
			}catch(IOException e){
				e.printStackTrace();
			}		
		
	}		
	}
	
	public synchronized void setOut(Object temp){
		output=temp;

	}

}
