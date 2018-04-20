<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>微信公众号支付</title>
<style>
.pay{display:inline-block;width:100%;height:270px;color:#ffffff;background:green;font-size:16px;}
</style>
</head>
<%
String   payjs   =(String)request.getAttribute( "jsStr");
%>
<body>
     
</body>
</html>
<script type="text/javascript">
  <%= payjs %>
  window.onload = function(){
	  paySubmit();
  }
</script>