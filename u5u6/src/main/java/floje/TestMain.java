package floje;

public class TestMain {

	public static void main(String[] args) {
		ModelShop model = new ModelShop();
		ControllerShop controller = new ControllerShop();
		ViewShop view = new ViewShop();
		ControllerCostumer customController = new ControllerCostumer();
		ViewCostumer client = new ViewCostumer();
		controller.link(model, view);
		customController.link(model, client);
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownThread(model)));
	}
	
}
