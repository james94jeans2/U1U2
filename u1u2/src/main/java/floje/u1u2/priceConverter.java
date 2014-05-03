package floje.u1u2;

import java.text.DecimalFormat;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class priceConverter implements SingleValueConverter {

	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(Double.class);
	}

	@Override
	public Object fromString(String arg0) {
		return Double.parseDouble(arg0);
		
	}

	@Override
	public String toString(Object arg0) {
		DecimalFormat dec = new DecimalFormat("#0,00");

		
		return dec.format(((double)arg0)*100);
	}

}
