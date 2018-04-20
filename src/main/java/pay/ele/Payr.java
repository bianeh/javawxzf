package pay.ele;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.alibaba.fastjson.JSONObject;
import com.hztywl.util.HttpRequest;
import com.hztywl.util.PayUtil;
import com.hztywl.util.Util;
import com.hztywl.util.XMLParser;
import entity.Pay;

import util.Xstreamutil;

/**
 * Servlet implementation class Pay
 */
@WebServlet("/Payr")
public class Payr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
//    public Pay() {
//       // super();
//        // TODO Auto-generated constructor stub
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nonce_str = PayUtil.getRandomStringByLength(16);//生成随机数，可直接用系统提供的方法
        String spbill_create_ip = Util.getIpAdd();//用户端ip,这里随意输入的
        String trade_type = "JSAPI";
        String openid = "";
        String prepay_id = "";
        String nonceStr = PayUtil.getRandomStringByLength(16);//生成随机数，可直接用系统提供的方法
		String appid = "wxa28f032b95868e92";
		String mch_id = "10000100";
		String secret = "731e692e9a2eb630da66463987253bd4";
		String code = request.getParameter("code");
		String out_trade_no = "201801122512";
		String notify_url = "http://m.hailanxiang.cn/ele";
		int total_fee = 12;
		String returnUrl = "http://m.hailanxiang.cn/ele";
		String body="fdsaf";
			/*
			 * 1、静默登录
			 * 2、获取openid
			 * 3、调用统一下单接口
			 * */
        //静默登录获取openid
		String openIdInfo = new HttpRequest().sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=" + appid + "&secret=" + secret +"&code=" + code + "&grant_type=authorization_code");
	
        //openIdInfo json字符串
        JSONObject json = JSONObject.parseObject(openIdInfo);
        openid = json.getString("openid");
        HashMap<String, Object> map = new HashMap();
        map.put("appid", appid);
        map.put("mch_id", mch_id);
        map.put("device_info", "WEB");
        map.put("nonce_str", nonce_str);
        map.put("body", "购买金币");//订单标题
        map.put("out_trade_no", out_trade_no);//订单ID
        map.put("total_fee", total_fee);//订单需要支付的金额
        map.put("spbill_create_ip", spbill_create_ip);
        map.put("trade_type", trade_type);
        map.put("notify_url", notify_url);//notify_url 支付成功之后 微信会进行异步回调的地址
        map.put("openid", openid);
        String sign = PayUtil.getSign(map);//参数加密  该方法key的需要根据你当前公众号的key进行修改
        map.put("sign", sign);
        
        
        
        Pay pay = new Pay();
		pay.setAppid(appid);
		pay.setBody(body);
		pay.setMch_id(mch_id);
		pay.setNonce_str(nonce_str);
		pay.setNotify_url(notify_url);
		pay.setOpenid(openid);
		pay.setSign(sign);
		pay.setOut_trade_no(out_trade_no);
		pay.setSpbill_create_ip(spbill_create_ip);
		pay.setTrade_type(trade_type);
		pay.setTotal_fee(total_fee);
		
		
		Xstreamutil.xstream.alias("xml", Pay.class);
		 
		String xml=Xstreamutil.xstream.toXML(pay).replaceAll("__", "_");
	    System.out.println(xml);
		String requestUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String respxml=null;
	
		
		
		
        
        
        
        
        
        System.out.println(sign);
        
        
        
        
        
        
        //String content = XMLParser.getXMLFromMap(map);
//        try {
//			http = new HttpRequest();
//		} catch (UnrecoverableKeyException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        //调用统一下单接口
//        String PostResult = null;
//		try {
//			PostResult = http.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", map);
//		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        Map<String, Object> cbMap = null;
//		try {
//			cbMap = XMLParser.getMapFromXML(PostResult);
//		} catch (ParserConfigurationException | SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        if (cbMap.get("return_code").equals("SUCCESS") && cbMap.get("result_code").equals("SUCCESS")) {
//            prepay_id = cbMap.get("prepay_id") + "";//这就是预支付id
//            
//            Date date=new Date();	
//			long a= date.getTime()/1000;
//			String TempStamp=String.valueOf(a);
//			
//            String timeStamp = TempStamp;
//            map = new HashMap<String, Object>();
//            map.put("appId", appid);
//            map.put("nonceStr", nonce_str);
//            map.put("package", "prepay_id=" + prepay_id);
//            map.put("signType", "MD5");
//            map.put("timeStamp", timeStamp);
//            sign = PayUtil.getSign(map);//参数加密
//            String jsStr = "";
//            jsStr += "function onBridgeReady(){"
//                    + "WeixinJSBridge.invoke("
//                    + "'getBrandWCPayRequest', {"
//                    +       "\"appId\" : \"" + appid + "\",   "    //公众号名称，由商户传入
//                    +       "\"timeStamp\":\"" + timeStamp + "\",  "        //时间戳，自1970年以来的秒数
//                    +       "\"nonceStr\" : \"" + nonce_str + "\"," //随机串
//                    +       "\"package\" : \"prepay_id=" + prepay_id + "\",      "
//                    +       "\"signType\" : \"MD5\",       "  //微信签名方式:
//                    +       "\"paySign\" : \"" + sign + "\" " //微信签名
//                    +    "},"
//                    +    "function(res){"
//                    +    "  if(res.err_msg == \"get_brand_wcpay_request:ok\" ) {"
//
//                    + "}   "   // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
//                    +    "window.location.href='" + returnUrl + "';"
//                    +    "}"
//                    + ");"
//                    + "}"
//                    + "function paySubmit(){"
//                    + "if (typeof WeixinJSBridge == \"undefined\"){"
//                    +   "if( document.addEventListener ){"
//                    +   "       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);"
//                    +   "   }else if (document.attachEvent){"
//                    +   "       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); "
//                    +   "       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);"
//                    +   "   }"
//                    +   "}else{"
//                    +   "   onBridgeReady();"
//                    +   "}"
//                    + "}";
      //String content = XMLParser.getXMLFromMap(map);
        //调用统一下单接口
//        String par = "appid=" + appid + "&mch_id=" + mch_id +"&device_info=WEB&grant_type=authorization_code&nonce_str="+nonce_str+"&body=rtrtr&out_trade_no="+out_trade_no+"&total_fee="+total_fee+"&spbill_create_ip="+spbill_create_ip+"&notify_url="+notify_url+"&trade_type="+trade_type+"&openid="+openid+"&sign="+sign;
//        System.out.println(par);
//        String PostResult = new HttpRequest().sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", par);
//        
        //调用统一下单接口
//        String PostResult = null;
//		try {
//			PostResult = new HttpRequest().sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", map);
//		} catch (UnrecoverableKeyException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (KeyManagementException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (KeyStoreException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NoSuchAlgorithmException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//        Map<String, Object> cbMap = null;
//		try {
//			cbMap = XMLParser.getMapFromXML(PostResult);
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(PostResult);
//        if (cbMap.get("return_code").equals("SUCCESS") && cbMap.get("result_code").equals("SUCCESS")) {
//            prepay_id = cbMap.get("prepay_id") + "";//这就是预支付id
//            Date date=new Date();	
//			long a= date.getTime()/1000;
//			String TempStamp=String.valueOf(a);	
//            String timeStamp = TempStamp;
//            map = new HashMap<String, Object>();
//            map.put("appId", appid);
//            map.put("nonceStr", nonce_str);
//            map.put("package", "prepay_id=" + prepay_id);
//            map.put("signType", "MD5");
//            map.put("timeStamp", timeStamp);
//            sign = PayUtil.getSign(map);//参数加密
//            String jsStr = "";
//            jsStr += "function onBridgeReady(){"
//                    + "WeixinJSBridge.invoke("
//                    + "'getBrandWCPayRequest', {"
//                    +       "\"appId\" : \"" + appid + "\",   "    //公众号名称，由商户传入
//                    +       "\"timeStamp\":\"" + timeStamp + "\",  "        //时间戳，自1970年以来的秒数
//                    +       "\"nonceStr\" : \"" + nonce_str + "\"," //随机串
//                    +       "\"package\" : \"prepay_id=" + prepay_id + "\",      "
//                    +       "\"signType\" : \"MD5\",       "  //微信签名方式:
//                    +       "\"paySign\" : \"" + sign + "\" " //微信签名
//                    +    "},"
//                    +    "function(res){"
//                    +    "  if(res.err_msg == \"get_brand_wcpay_request:ok\" ) {"
//
//                    + "}   "   // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
//                    +    "window.location.href='" + returnUrl + "';"
//                    +    "}"
//                    + ");"
//                    + "}"
//                    + "function paySubmit(){"
//                    + "if (typeof WeixinJSBridge == \"undefined\"){"
//                    +   "if( document.addEventListener ){"
//                    +   "       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);"
//                    +   "   }else if (document.attachEvent){"
//                    +   "       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); "
//                    +   "       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);"
//                    +   "   }"
//                    +   "}else{"
//                    +   "   onBridgeReady();"
//                    +   "}"
//                    + "}";
//            //最后将js直接返回给前台 进行调用 给按钮增加paySubmit方法
//            System.out.println(jsStr);
//            System.out.println("1111");
//        }
            System.out.println(sign);
            System.out.println(openid);
            System.out.println(openIdInfo);
            System.out.println("22");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
