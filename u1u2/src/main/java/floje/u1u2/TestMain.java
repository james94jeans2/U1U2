package floje.u1u2;

import fpt.com.ProductList;

public class TestMain {

	public static void main(String[] args) {
		ModelShop model = new ModelShop();
		ControllerShop controller = new ControllerShop();
		ViewShop view = new ViewShop();
		controller.link(model, view);
	}
	
//	public static void main (String[] args) {
//		ProductList products = new floje.u1u2.ProductList();
//		for (int i = 0; i < 10; ++i) {
//			fpt.com.Product product = new Product();
//			product.setName("" + i);
//			product.setPrice(i * 3.99);
//			product.setQuantity(i * 5);
//			products.add(product);
//		}
//		ViewCostumer kunde = new ViewCostumer(((floje.u1u2.ProductList)products).toArray(new Product[0]));
//	}
	
}
