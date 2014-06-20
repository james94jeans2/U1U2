package floje;

public class TestMain {

	public static void main(String[] args) {
		ModelShop model = new ModelShop();
		ControllerShop controller = new ControllerShop();
		ViewShop view = new ViewShop();
		ViewCostumer client = new ViewCostumer(model);
		controller.link(model, view);
		controller.link(model, client);
	}
	
}
