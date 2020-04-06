<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>open tool</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  
    <style type="text/css">
        ul { list-style-type: none;  margin: 2px; padding: 2px; }   
        li { border-bottom: 1px solid #ccc; font-size: 16px; cursor: pointer; } 
        .clicked { background-color: #ccc; }
        p {border-bottom:  1px solid #ccc;}
    </style>
  
	<script src="${resRoot }/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
	
  </head>
  
  <body>
  
   <form action="liner/upload/img/file" id="form1" name="form1" encType="multipart/form-data"  method="post" target="hidden_frame" >   
              图片:<input type="file" id="img" name="img">
      <INPUT id="uploadImg" value="上传图片" type="button">
      <br/><span id="imgmsg"></span>   
      <iframe name="hidden_frame" id="hidden_frame" style="display:none"></iframe>   
  </form>   
  
  </body>
  
</html>
<script type="text/javascript">
$(function(){
	
	$('#uploadImg').on('click', function() {
        var $form = $('#form1');
        var $img = $('#img');
        var path = $img.val();
        if (!path) {
        	alert('图片不能为空');
            return;
        }
        $('#imgname').val(path.substr(path.lastIndexOf('\\') + 1));
        $form.submit();
    });
	
	var $imgmsg = $('#imgmsg');
	$('iframe[name=hidden_frame]').on('load', function(){
		$imgmsg.html($(this).contents().find('body').html());
	});
	
});

</script>
