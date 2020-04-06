<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>open test</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  
    <style type="text/css">
        ul { list-style-type: none;  margin: 2px; padding: 2px; }   
        li { border-bottom: 1px solid #ccc; font-size: 16px; cursor: pointer; } 
        .clicked { background-color: #ccc; }
        .imgcode { cursor: pointer; }
    </style>
  
	<script src="${resRoot }/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	    // 接口配置
        var apis = [{}];
	    
	    // APP接口
	    var apis1 = apis[1] = {};
	    apis1["yunba.liner.v1.app.user.login"] = { name: "网点用户登录接口(不需要token)", params: ["account", "password"] };
	    apis1["yunba.liner.v1.app.user.password.reset"] = { name: "网点用户重置密码(不需要token)", params:["mobile_no", "varify_code", "new_password", "re_new_password"] };
        apis1["yunba.liner.v1.app.user.password.reset.smscode.send"] = { name: "网点用户重置密码发送验证码(不需要token)", params:["mobile_no"] };
        apis1["yunba.liner.v1.app.report.main"] = {name: "app首页统计", params:[]};
        apis1["yunba.liner.v1.app.report.operation.day"] = {name: "运营日报", params:["page", "page_num"]};
        
        //我的车辆
        apis1["yunba.liner.v1.app.truck.status.num"] = { name: "app-车辆管理-获取各个状态车辆数量", params:[] };
        apis1["yunba.liner.v1.app.truck.list"] = { name: "app-车辆管理-车辆列表", params:["page","page_num","truck_status","search_value"] };
        apis1["yunba.liner.v1.app.truck.location.list"] = { name: "app-车辆管理-定位地图定位信息列表", params:["truck_status"] };
        apis1["yunba.liner.v1.app.truck.location.track.list"] = { name: "app-车辆管理-定位地图轨迹信息列表", params:["site_truck_id"] };
        /** 发车 */
	    apis1["yunba.liner.v1.app.trip.status.num"] = { name: "发车-获取各个状态车次数量", params:[] };
        apis1["yunba.liner.v1.app.trip.list"] = {name: "发车-车次列表", params:["trip_status", "search_value", "page", "page_num"]};
	    apis1["yunba.liner.v1.app.trip.detail"] = {name: "发车-车次详情", params:["trip_id"]};
	    apis1["yunba.liner.v1.app.trip.order.list"] = {name: "发车-车次详情下的配载运单", params:["trip_id"]};
	    apis1["yunba.liner.v1.app.trip.site.trace"] = {name: "发车-物流跟踪", params:["trip_id"]};
	    apis1["yunba.liner.v1.app.trip.location.trace"] = {name: "发车-车次轨迹", params:["trip_id"]};
	    
	    //到站运单
	    apis1["yunba.liner.v1.app.order.arrive"] = {name: "到站运单", params:["page","page_num","status","search_value"]};
	    apis1["yunba.liner.v1.app.order.detail"] = {name: "运单详情", params:["trip_order_id"]};
	    apis1["yunba.liner.v1.app.order.log"] = {name: "运单详情-操作记录", params:["order_no"]};
	    apis1["yunba.liner.v1.site.order.arrive.counts"] = {name: "到站运单-统计总数量", params:[]};
	    apis1["yunba.liner.v1.site.order.arrive.orderAllCounts"] = {name: "到站运单-统计各状态数量", params:[]};
	    
	    // 我的运单
        apis1["yunba.liner.v1.app.order.searchWaitSendOrders"] = { name: "我的运单-待发车列表", params:["page", "page_num", "keyword"] };
        apis1["yunba.liner.v1.app.order.searchTransportingOrders"] = { name: "我的运单-运输中列表", params:["page", "page_num", "keyword"] };
        apis1["yunba.liner.v1.app.order.searchWaitSignOrders"] = { name: "我的运单-待签收列表", params:["page", "page_num", "keyword"] };
        apis1["yunba.liner.v1.app.order.statisticsOrder"] = { name: "我的运单-统计运单数", params:["keyword"] };
        apis1["yunba.liner.v1.app.order.orderDetail"] = { name: "我的运单-运单明细", params:["order_id"] };
        
        // 发货客户列表
        apis1["yunba.liner.v1.app.customer.querySendCustomers"] = { name: "发货客户列表", params:["page", "page_num"] };
	    
	    //设置
	    apis1["yunba.liner.v1.app.config"] = {name: "设置", params:["order_truck_start_over_time","order_sign_over_time","truck_stop_over_time","truck_load_over_percent","cargo_send_over_time","version"]};
	    apis1["yunba.liner.v1.app.version.get"] = {name: "获取版本信息", params:["device_name"]};
	    apis1["yunba.liner.v1.app.channel.update"] = {name: "设置推送channelid", params:["channel_id","device_type"]};
	    
	    apis1["yunba.liner.v1.app.push.msg.list"] = {name: "消息列表", params:["page", "page_num"]};
	    apis1["yunba.liner.v1.app.push.msg.read"] = {name: "阅读消息", params:["msg_id"]};
	    
	    // 公共接口
        apis1["yunba.liner.v1.common.dict"] = {name: "常用字典", params:["dict_name"]};
        apis1["yunba.liner.v1.common.area"] = {name: "省市县地区", params:["parent_code"]};
	    
	</script>
  </head>
  
  <body>
    <table width="100%">
        <tr>
            <td width="20%" valign="top" style="font: 10px;">
                <input type="radio" name="rdCommand" value="1" checked="checked" onclick="showApp()">APP版
                <a href="linerapptool.jsp" target="_blank">工具页</a>
                <ul id="selCommands1" class="commands"></ul>
                <ul id="selCommands2" class="commands" style="display: none"></ul>
                <ul id="selCommands3" class="commands" style="display: none"></ul>
            </td>
            <td valign="top">
                <table width="100%" style="margin-top: 20px;" id="tblRight">
                  <tr>
                    <td valign="top">方法：</td>
                    <td id="tdMethodName" style="font-weight: bolder;"></td>
                  </tr>
                  <tr>
                      <td valign="top">参数：</td>
                      <td align="left" valign="top">
                          <table id="tdParams" width="100%">
                            
                          </table>
                      </td>
                  </tr>
                  <tr>
                      <td>
                          &nbsp;
                      </td>
                      <td colspan="">
                          <input id="btnReq" type="button" value="测试" style="width: 100px;">
                          TOKEN<input id="token" type="text" style="width: 100px;" placeholder="token">
                          time cost: <span id="timecost">-</span> ms.
                          <input type="checkbox" id="showdoc">显示接口文档
                      </td>
                  </tr>
                  <tr>
                      <td valign="top">返回：</td>
                      <td align="left" valign="top">
                          <textarea rows="20" cols="90%" id="result"></textarea>
                      </td>
                  </tr>
                  <tr>
                    <td colspan="2" id="tdShowdoc"></td>
                  </tr>
              </table>
                
            </td>
        </tr>
    </table>
  	
  </body>
</html>
<script type="text/javascript">

function showApp() {
	var checked = $('input[name=rdCommand]:checked').val();
	$('#selCommands' + checked).show().siblings('ul.commands').hide();
	$('ul.commands li.clicked').removeClass('clicked');
	$('#tdParams').empty();
}

$(function(){
	
	var $tdParams = $('#tdParams');
	var $btnReq = $('#btnReq');
	
	// 初始化页面左侧接口列表
	for (var k in apis) {
		for (var v in apis[k]) {
			$('#selCommands' + k).append('<li command="' + v + '">' + apis[k][v].name + '</option>');
		}
	}
	
	// 生成参数填写框
	$('li', '.commands').on('click', function(){
		var $li = $(this);
		$('li.clicked').removeClass('clicked');
		$li.addClass('clicked');
		var command = $li.attr('command');
		if (!command) {
			return;
		}
		$('#tdMethodName').html(command);
		
		var checked = $('input[name=rdCommand]:checked').val();
		var api = apis[checked][command];
		var params = api.params;
		$tdParams.empty();
		for (var i in params) {
			$tdParams.append('<tr><td class="param-name" style="width:15%;" align="right">' + params[i] + 
				    '</td><td class="param-value"><input />' + 
				    (params[i] == 'imgcode' ? '<img src="liner/imgcode" class="imgcode"/>' : '') + 
				    '</td></tr>');
		}
	});
	
	// 接口测试
	$btnReq.on('click', function(){
		
		$("#result").val('');
		$('#timecost').text('-');
		
		var start = new Date().getTime();
		
		var data = {};  // 请求参数
		data.method = $('li.clicked').attr('command');  // 接口方法名称。固定必传参数。
		if (!data.method) {
			alert('请先选择一个接口');
			return false;
		}
		data.token = $('#token').val();   // token.使用登录接口获取
		$('tr', $tdParams).each(function(){ // 其他请求参数
			var $tr = $(this);
			data[$('td.param-name', $tr).text()] = $('td.param-value input', $tr).val();
		});
		
		var url = "";  // 请求路径
		var checked = $('input[name=rdCommand]:checked').val();
		if (checked == "1") {
			url = "liner/android";
		} 
		
		if ($('#showdoc').prop('checked')) {
			data["__showdoc"] = '1';
		}
	    
		// 发起ajax请求
		$.ajax({ 
            url: url,
            type: 'POST',
            data: data,
            cache: false,
            dataType:"json",
            success:function(data, textStatus){
            	console.log(data);
            	if (data["__showdoc"]) {
            		$('#tdShowdoc').html(data["__showdoc"]);
            		delete data["__showdoc"];
            	}
          		var str = JSON.stringify(data, null, 2);
                $("#result").val(str);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
            	$("#result").val(XMLHttpRequest.responseText);
            },
            complete: function(){
            	var end = new Date().getTime();
            	$('#timecost').text(end - start);
            }
        });
	});
	
	$(document).scroll(function(){
		if ($('li.clicked')) {
			var docTop = $(document).scrollTop();
	        var liTop = $('li.clicked').offset().top - 200;
	        if (docTop < liTop) {
	            $('#tblRight').css('margin-top', 20 + docTop + 'px');
	        }
		}
	});
	
	$(document).on('click', '.imgcode', function() {
		var that = $(this);
		if (that.is('[src]')) {
			that.attr('src', that.attr('src') + '?r=' + new Date().getTime());
		}
	});
	
	$('#showdoc').on('click', function(){
		if (!$(this).prop('checked')) {
			$('#tdShowdoc').html('');
		}
	});
	
});
</script>
