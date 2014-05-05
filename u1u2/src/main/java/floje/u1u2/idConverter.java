package floje.u1u2;

import com.thoughtworks.xstream.converters.SingleValueConverter;



public class idConverter implements SingleValueConverter {

	
	
	@Override
	public boolean canConvert(Class arg0) {
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
		
		String idString = String.valueOf(arg0);
		int l = idString.length();
		while(l<6){
			idString="0"+idString;
			l = idString.length();
			
		}
		
		return idString;
	}

}
