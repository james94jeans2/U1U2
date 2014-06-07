package problem4;

public class Problem {

	public static void main(String[] args) {
		Thread thread = new Thread(new Acquisition());
		thread.setName("Kundenaquise");
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
