package strategies;

import com.thoughtworks.xstream.converters.SingleValueConverter;


public class IDConverter implements SingleValueConverter {



	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class arg0) {
		return arg0.equals(Long.class);
	}

	@Override
	public Object fromString(String arg0) {
		long ret = 0l;

		try{

			ret= Long.parseLong(arg0);

		}catch(Exception e){
			e.printStackTrace();

		}
		return ret;
	}

	@Override
	public String toString(Object arg0) {
		return String.format("%06d", arg0);
	}
}
