package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Sign {
	
	 
public  static String sign(){
	
	String appid="wxd930ea5d5a258f4f";
	String mch_id="10000100";
	String device_info="1000";
	String body="test"; 
	String nonce_str="ibuaiVcKdpRxkhJA"; 
	String key="192006250b4c09247ec02edce69f6a2d";
	SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();	
	// Map<Object, Object> parameters = new HashMap<Object, Object>();	

    parameters.put("appid",appid);
	 parameters.put("mch_id",mch_id);
	 parameters.put("device_info",device_info);
	 parameters.put("body",body);
	 parameters.put("nonce_str",nonce_str);
	String sign=creatSign( parameters, key);
	return sign;
	
	
}	
	
public static  String creatSign(SortedMap<Object, Object> parameters,String Key){
	
	  StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");

			}

		}

		System.out.println( sb.toString());
		sb.append(Key);
		System.out.println(sb);
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8");

		
return sign;

	
	
}

public static void main(String[] args) {
	
	
	String sign=sign();
	
	System.out.println(sign);
	
	
}


}
