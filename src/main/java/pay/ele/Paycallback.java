package pay.ele;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.hztywl.util.Util;
import com.hztywl.util.XMLParser;
/**
 * Servlet implementation class Paycallback
 */
@WebServlet("/Paycallback")
public class Paycallback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Paycallback() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String retStr = new String(Util.readInput(request.getInputStream()),"utf-8");
        Map<String, Object> cbMap = null;
		try {
			cbMap = XMLParser.getMapFromXML(retStr);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String out_trade_no = (String) cbMap.get("out_trade_no");
		
        if (cbMap.get("return_code").equals("SUCCESS") && cbMap.get("result_code").equals("SUCCESS")) {
        	
        	String driver="com.mysql.jdbc.Driver";//驱动路径
            String url="jdbc:mysql://localhost:3306/hlxtea";//数据库地址
            String user="root";//访问数据库的用户名
            String password="root";//用户密码      
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
    				
    				    String sql="select * from orderinfo where orderId ='"+out_trade_no+"' and state = 100";//查询user表的所有信息
    				//4、执行sql语句
    				ResultSet rs = st.executeQuery(sql);//查询之后返回结果集
    				rs.last();
    				int row = rs.getRow();
    				if(row == 1)
    				{
    			        String squ = "update orderinfo set state = 0 where orderId='"+out_trade_no+"'";
    			        st.execute(squ);
    				}
    			   }
    			} catch (SQLException e1) {
    				e1.printStackTrace();
    			}
        	
        }
        //返回的数据
        //支付回调处理订单 更改订单状态
        System.out.println(retStr);
        System.out.println("test");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
