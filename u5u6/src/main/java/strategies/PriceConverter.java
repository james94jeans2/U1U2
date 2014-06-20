package strategies;

import java.util.Locale;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class PriceConverter implements SingleValueConverter {

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(Double.class);
	}

	@Override
	public Object fromString(String arg0) {
		return Double.parseDouble(arg0);

	}

	@Override
	public String toString(Object arg0) {
		return String.format(Locale.ENGLISH, "%.2f", arg0);
	}

}
