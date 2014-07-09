package floje;

public class ShutdownThread implements Runnable {

	private ModelShop shop;
	
	public ShutdownThread (ModelShop shop) {
		this.shop = shop;
	}
	
	@Override
	public void run() {
		System.out.println("Closing connections!");
		shop.closeConnnections();
		System.out.println("Connections closed!");
	}

}
