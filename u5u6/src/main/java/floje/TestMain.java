package floje;

public class TestMain {

	public static void main(String[] args) {
//		new ViewCostumer();
		ModelShop model = new ModelShop();
		ControllerShop controller = new ControllerShop();
		ViewShop view = new ViewShop();
		controller.link(model, view);
		
	}
	
}
