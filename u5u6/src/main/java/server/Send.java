package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Send implements Runnable {

	private OutputStream out;
	private Object output;
	private boolean changed;
	private Socket soc;
	
	public Send(OutputStream out,Socket soc){
		this.out=out;
		changed=false;
		this.soc=soc;
		output = new Object();
	}
	
	public void run() {
		ObjectOutputStream oout = null;
		synchronized (soc) {
				try {
					oout = new ObjectOutputStream(out);
					System.out.println("oos initialized");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		while(!soc.isClosed() && oout != null){
			//synchronized (soc) {
		   if(changed){
			try{
					synchronized (output) {
						System.out.println("writing");
						oout.writeObject(output);
						oout.flush();
					}
			
				
				
				}catch(IOException e){
					e.printStackTrace();
				}	
				changed = false;
			}
		 //}
	}		
		System.out.println("Outputconnection closed");
	}
	
	public void setOut(Object temp){
		synchronized (output) {
			output=temp;
			changed = true;
			System.out.println("out set");
		}
		

	}

}
