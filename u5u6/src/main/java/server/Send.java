package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Send implements Runnable {

	private OutputStream out;
	private Object output;
	private boolean changed, stop;
	private Socket soc;
	
	public Send(OutputStream out,Socket soc){
		this.out=out;
		changed=false;
		this.soc=soc;
		output = new Object();
		stop = false;
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
		while(!soc.isClosed() && oout != null && !stop){
			//synchronized (soc) {
		   if(changed){
			try{
					synchronized (output) {
						System.out.println("writing");
						oout.writeObject(output);
						oout.flush();
						if (output instanceof Integer) {
							stop = true;
						}
					}
			
				
				
				}catch(IOException e){
					e.printStackTrace();
				}	
				changed = false;
			}
		 //}
	}
		try {
			oout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Outputconnection closed");
	}
	
	public void setOut(Object temp){
		synchronized (output) {
			output=temp;
			changed = true;
		}
		

	}

}
