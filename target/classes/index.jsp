<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
<title>Insert title here</title>
<style>
   *{padding:0px;margin:0px;}
   .wechatpay{width:100%;}
   .wechatpaybutton{display:inline-block;border:0px;width:100%;background:blue;height:70px;color:#FFFFFF;font-weight:bold;}
</style>
</head>
<body>
  <div class="wechatpay">
       <button class="wechatpaybutton">wechatpay</button>
  </div>
</body>
</html>
<script>
   $(".wechatpaybutton").click(function(){
	    window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa28f032b95868e92&redirect_uri=http://m.hailanxiang.cn/ele/Pay?orderno=4446564&paymoney=12&response_type=code&scope=snsapi_base#wechat_redirect";
   })
</script>