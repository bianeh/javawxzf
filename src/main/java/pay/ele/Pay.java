package pay.ele;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.SAXException;
import com.alibaba.fastjson.JSONObject;
import com.hztywl.util.HttpRequest;
import com.hztywl.util.PayUtil;
import com.hztywl.util.Util;
import com.hztywl.util.WXPayUtil;
import com.hztywl.util.XMLParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import entity.Payr;
import util.Httpsrequest;
import util.Xstreamutil;
/**
 * Servlet implementation class Pay
 */
@WebServlet("/Pay")
public class Pay extends HttpServlet {
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
		String mch_id = "1500046272";
		String secret = "731e692e9a2eb630da66463987253bd4";
		String code = request.getParameter("code");
		String out_trade_no = request.getParameter("orderno");
		String driver="com.mysql.jdbc.Driver";//驱动路径
        String url="jdbc:mysql://localhost:3306/hlxtea";//数据库地址
        String user="root";//访问数据库的用户名
        String password="root";//用户密码      
        int total_fee = 1;
     
            //1、加载驱动
            try {
				Class.forName(driver);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            //2、链接数据库
            Connection con = null;
			try {
				con = DriverManager.getConnection(url, user, password);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
            try {
				if(!con.isClosed()){//判断数据库是否链接成功
				System.out.println("已成功链接数据库！");
				//3、创建Statement对象
				Statement st = con.createStatement();
				//4、执行sql语句
				String sql="select * from orderinfo where orderId ='"+out_trade_no+"'";//查询user表的所有信
				System.out.println(sql);
				ResultSet rs = st.executeQuery(sql);//查询之后返回结果集
				while(rs.next()){      //这里必须循环遍历
					String price = rs.getString("price");
					double price1 = Double.parseDouble(price.trim())*100;
					total_fee = (int)price1;
				}
				
				    //5、打印出结果
//                while(rs.next()){
//                   System.out.println(rs.getString("Id")+"\t"+rs.getString("name")+"\t"+rs.getString("password"));
//　　　　　　　　　　　}
				   }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

//	    int total_fee = 1
		String notify_url = "http://m.hailanxiang.cn/ele/Paycallback";
		String returnUrl = "http://m.hailanxiang.cn:8082";
		String body="fdsaf";
		System.out.println(out_trade_no);
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
        System.out.println(openid);
        HashMap<String, Object> map = new HashMap();
        map.put("appid", appid);
        map.put("mch_id", mch_id);
        map.put("device_info", "WEB");
        map.put("nonce_str", nonce_str);
        map.put("body", "支付购买茶叶");//订单标题
        map.put("out_trade_no", out_trade_no);//订单ID
        map.put("total_fee", total_fee);//订单需要支付的金额
        map.put("spbill_create_ip", spbill_create_ip);
        map.put("trade_type", trade_type);
        map.put("notify_url", notify_url);//notify_url 支付成功之后 微信会进行异步回调的地址
        map.put("openid", openid);
        String sign = PayUtil.getSign(map);//参数加密  该方法key的需要根据你当前公众号的key进行修改
        map.put("sign", sign);
        String xml = PayUtil.map2Xmlstring(map);
        System.out.println(xml);
        String requestUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String respxml=null;
		try {
			respxml = Httpsrequest.HttpsRequest(requestUrl, "POST",xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, Object> cbMap = null;
		try {
			cbMap = XMLParser.getMapFromXML(respxml);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(cbMap.get("return_code"));
        if (cbMap.get("return_code").equals("SUCCESS") && cbMap.get("result_code").equals("SUCCESS")) {
            prepay_id = cbMap.get("prepay_id") + "";//这就是预支付id
            Date date=new Date();	
			long a= date.getTime()/1000;
			String TempStamp=String.valueOf(a);	
            String timeStamp = TempStamp;
            map = new HashMap<String, Object>();
            map.put("appId", appid);
            map.put("nonceStr", nonce_str);
            map.put("package", "prepay_id=" + prepay_id);
            map.put("signType", "MD5");
            map.put("timeStamp", timeStamp);
            sign = PayUtil.getSign(map);//参数加密
            String jsStr = "";
            jsStr += "function onBridgeReady(){"
                    + "WeixinJSBridge.invoke("
                    + "'getBrandWCPayRequest', {"
                    +       "\"appId\" : \"" + appid + "\",   "    //公众号名称，由商户传入
                    +       "\"timeStamp\":\"" + timeStamp + "\",  "        //时间戳，自1970年以来的秒数
                    +       "\"nonceStr\" : \"" + nonce_str + "\"," //随机串
                    +       "\"package\" : \"prepay_id=" + prepay_id + "\",      "
                    +       "\"signType\" : \"MD5\",       "  //微信签名方式:
                    +       "\"paySign\" : \"" + sign + "\" " //微信签名
                    +    "},"
                    +    "function(res){"
                    +    "  if(res.err_msg == \"get_brand_wcpay_request:ok\" ) {"

                    + "}   "   // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                    +    "window.location.href='" + returnUrl + "';"
                    +    "}"
                    + ");"
                    + "}"
                    + "function paySubmit(){"
                    + "if (typeof WeixinJSBridge == \"undefined\"){"
                    +   "if( document.addEventListener ){"
                    +   "       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);"
                    +   "   }else if (document.attachEvent){"
                    +   "       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); "
                    +   "       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);"
                    +   "   }"
                    +   "}else{"
                    +   "   onBridgeReady();"
                    +   "}"
                    + "}";
            System.out.println(jsStr);
            request.setAttribute("jsStr",jsStr); 
            request.getRequestDispatcher( "/pay.jsp").forward(request,response); 
        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
