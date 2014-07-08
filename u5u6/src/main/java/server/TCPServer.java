package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	
	private static ServerSocket server;
	//private Socket client;
	private static InputStream in;
	private static OutputStream out;

	
	
	public static void main(String[] args){
		// ServerSocket erstellen
		Warehouse wh = new Warehouse();
		Thread wht = new Thread(wh);
		wht.start();
				try {
					server = new ServerSocket(6666);
					
					while (true) {
						Socket client = null;
						try{
							client = server.accept();
							in = client.getInputStream();
							out = client.getOutputStream();
						}catch(IOException e1) {
							e1.printStackTrace();
						}
						
//						final Runnable send = new Runnable(){
//							
//							
//							@Override
//							public synchronized void run() {
//								while(true){
//									
//									try{
//										out.write(getOut().getBytes());
//										out.flush();
//										
//									}catch(IOException e){
//										e.printStackTrace();
//									}		
//								
//							}
//							}s
//						}; 
						Send s = new Send(out,client);
						final Thread t2 = new Thread(s);
						
//						final Runnable read = new Runnable(){
//							
//							@Override
//							public synchronized void run() {
//								while(true){
//									try {
//										t2.wait();
//									} catch (InterruptedException e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//									String input = "";
//									try{
//										byte[] indata = new byte[in.available()];
//										in.read(indata);
//										input = new String(indata);
//										if(authentifizierung(input)){
//											setOut(input.replaceAll("admin:admin", ""));
//											t2.notify();
//										}else{
//											setOut("retry");
//											t2.notify();
//										}
//									}catch(IOException e){
//										e.printStackTrace();
//									}
//									
//								}								
//							}
//						};
						
						
						
						final Thread t1 = new Thread(new Read(client,in,wh,s));
						

						t2.start();
						t1.start();
						
						

					
						
						
					}		
				} catch (IOException e2) {
					e2.printStackTrace();
				}
	}


	
//	private static boolean authentifizierung(String auth) {
//		if(auth.contains("admin:admin")){
//			return true;
//		}
//		return false;
//	}
	


}
