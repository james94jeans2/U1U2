package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Send implements Runnable {

	private OutputStream out;
	private Object output;
	private boolean changed, initialized;
	private Socket soc;
	
	public Send(OutputStream out,Socket soc){
		this.out=out;
		changed=true;
		this.soc=soc;
		initialized = false;
		output = new Object();
	}
	
	public void run() {
		ObjectOutputStream oout = null;
		while(!soc.isClosed()){
		   if(changed){
			synchronized (soc) {
				
				if (!initialized) {
					try {
						oout = new ObjectOutputStream(out);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			try{
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
