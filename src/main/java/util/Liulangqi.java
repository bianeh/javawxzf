package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Liulangqi {

	
	 public static void main(String[] args) {  
	        String html = new Liulangqi().htmlContent(new HtmlPage("baidu.com", ""));  
	        System.out.println(html);  
	        
	        
	        
	    }  
	      
	    /** 
	     * ץȡ��ҳԭ���� 
	     * <br><b>��������</b>��2011-1-20 
	     * @param hp 
	     * @return  ָ����ҳ��Դ���룬���ץȡʧ�ܣ��򷵻�һ������Ϊ 0 �Ŀ��ַ��� 
	     * @author <a href="mailto:hemingwang0902@126.com" mce_href="mailto:hemingwang0902@126.com">������</a> 
	     */  
	    public String htmlContent(HtmlPage hp){  
	        StringBuffer html = new StringBuffer();  
	        Socket socket = null;  
	        BufferedWriter writer = null;  
	        BufferedReader reader = null;  
	        try {  
	            // ����һ��Socket  
	            socket = new Socket(InetAddress.getByName(hp.getServer()), hp.getPort());  
	              
	              
	            StringBuffer command = new StringBuffer()  
	                .append("GET " + hp.getPath() + " HTTP/1.0/r/n")  
	                .append("HOST:" + hp.getServer() + "/r/n")  
	                .append("/r/n");  
	            // ��������  
	            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), hp.getCharset()));  
	            writer.write(command.toString());  
	            writer.flush();  
	            // ���շ��صĽ��  
	            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), hp.getCharset()));  
	            String line;  
	            while ((line = reader.readLine()) != null) {  
	                html.append(line).append("/r/n");  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                if (reader != null)  
	                    reader.close();  
	                if (writer != null)  
	                    writer.close();  
	                if (socket != null)  
	                    socket.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	      
	        return html.toString();  
	    }  
	      
	    static class HtmlPage {  
	        private String server;  
	        private int port = 80;  
	        private String path;  
	        private String charset = "UTF-8";  
	        public HtmlPage() {  
	            super();  
	        }  
	        public HtmlPage(String server, String path) {  
	            super();  
	            this.server = server;  
	            this.path = path;  
	        }  
	        public HtmlPage(String server, String path, String charset) {  
	            super();  
	            this.server = server;  
	            this.path = path;  
	            this.charset = charset;  
	        }  
	        public HtmlPage(String server, int port, String path, String charset) {  
	            super();  
	            this.server = server;  
	            this.port = port;  
	            this.path = path;  
	            this.charset = charset;  
	        }  
	        public String getServer() {  
	            return server;  
	        }  
	        public void setServer(String server) {  
	            this.server = server;  
	        }  
	        public int getPort() {  
	            return port;  
	        }  
	        public void setPort(int port) {  
	            this.port = port;  
	        }  
	        public String getPath() {  
	            return path;  
	        }  
	        public void setPath(String path) {  
	            this.path = path;  
	        }  
	        public String getCharset() {  
	            return charset;  
	        }  
	        public void setCharset(String charset) {  
	            this.charset = charset;  
	        }  
	    }  
	}  
	 