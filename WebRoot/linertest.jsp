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
	<!-- 为使用方便，直接使用jquery.js库，如您代码中不需要，可以去掉 -->
	<script src="http://code.jquery.com/jquery-1.12.3.min.js"></script>
	<!-- 引入封装了failback的接口--initGeetest -->
	<script src="http://static.geetest.com/static/tools/gt.js"></script>
	<script type="text/javascript">
	    // 接口配置
        var apis = [{}];
	    
	    // 网页版接口
	    var apis1 = apis[1] = {};
	    //apis1["yunba.liner.v1.site.user.login"] = { name: "网点用户登录接口", params: ["account", "password", "imgcode"] };
        apis1["yunba.liner.v1.site.user.login"] = { name: "网点用户登录接口", params: ["account", "password", "geetest"] };
	    apis1["yunba.liner.v1.site.user.menu.query"] = {name: "网点用户的菜单列表", params: []};
	    apis1["yunba.liner.v1.site.user.password.reset"] = { name: "网点用户重置密码(忘记密码)", params:["mobile_no", "varify_code", "new_password", "re_new_password"] };
	    apis1["yunba.liner.v1.site.user.password.reset.smscode.send"] = { name: "网点用户重置密码(忘记密码) 发送验证码", params:["mobile_no"] };
	    apis1["yunba.liner.v1.site.role.list"] = {name: "网点用户角色列表查询", params: []};
	    apis1["yunba.liner.v1.site.role.add"] = {name: "网点用户角色新增", params: ["role_name"]};
	    apis1["yunba.liner.v1.site.role.edit"] = {name: "网点用户角色修改", params: ["role_id", "role_name"]};
	    apis1["yunba.liner.v1.site.role.delete"] = {name: "网点用户角色删除", params: ["role_id"]};
	    apis1["yunba.liner.v1.site.role.menu.query"] = {name: "网点用户角色的菜单查询", params:["role_id"]};
	    apis1["yunba.liner.v1.site.role.menu.edit"] = {name: "网点用户角色的菜单修改", params:["role_id", "menu_ids"]};
	    
	    /* 员工模块 */
	    apis1["yunba.liner.v1.site.staff.list.get"] = { name: "网点员工管理列表", params:["page", "page_num"] };
	    apis1["yunba.liner.v1.site.staff.add"] = { name: "网点员工管理新增员工", params:["role_id", "user_name", "user_mobile", "user_pwd"] };
	    apis1["yunba.liner.v1.site.staff.update"] = { name: "网点员工管理修改员工", params:["user_id", "role_id", "user_name"] };
	    apis1["yunba.liner.v1.site.staff.del"] = { name: "网点员工管理删除员工", params:["user_id"] };
	    apis1["yunba.liner.v1.site.staff.password.reset"] = { name: "网点员工管理重置密码", params:["user_id", "user_pwd"] };
	    apis1["yunba.liner.v1.site.role.selection.list"] = { name: "网点角色下拉框列表", params:[] };
	    /* 公司资料模块 */
	    apis1["yunba.liner.v1.site.company.get"] = { name: "网点公司资料获取", params:[] };
	    apis1["yunba.liner.v1.site.company.update"] = { name: "网点公司资料修改", params:["site_logo_url", "site_name", "site_no", "site_company", "site_area", "site_area_code", "site_address", "site_lng", "site_lat", "site_linkman", "site_linktel"] };
	    
	    /* 合作网点模块 */
	    apis1["yunba.liner.v1.site.partener.invite"] = { name: "合作网点-邀请网点", params:["tel","site_name", "site_area_code", "site_address", "partener_type"] };
	    apis1["yunba.liner.v1.site.partener.list.mine"] = { name: "合作网点-我的网点", params:["page", "page_num"] };
	    apis1["yunba.liner.v1.site.partener.list.byme"] = { name: "合作网点-发出的邀请", params:["page", "page_num"] };
	    apis1["yunba.liner.v1.site.partener.list.tome"] = { name: "合作网点-收到的邀请", params:["page", "page_num"] };
	    apis1["yunba.liner.v1.site.partener.invite.agree"] = { name: "合作网点-同意邀请", params:["invite_site_id"] };
	    apis1["yunba.liner.v1.site.partener.invite.refuse"] = { name: "合作网点-拒绝邀请", params:["invite_site_id"] };
	    apis1["yunba.liner.v1.site.partener.info"] = { name: "合作网点-获取合作网点信息(获取编辑网点信息)", params:["partner_site_id"] };
	    apis1["yunba.liner.v1.site.partener.edit"] = { name: "合作网点-编辑我的网点", params:["partner_site_id","tel","site_name", "site_area_code", "site_address", "partener_type"] };
	    apis1["yunba.liner.v1.site.partener.delete"] = { name: "合作网点-我的网点-删除", params:["partner_site_id"] };
	    
        /* 收货人模块 */
        apis1["yunba.liner.v1.site.customer.listReceiver"] = { name: "收货人列表接口", params:["page", "page_num","customer_company"] };
        apis1["yunba.liner.v1.site.customer.addReceiver"] = { name: "收货人新增接口", params:["customer_company", "customer_linkman", "customer_linktel", "customer_area", "customer_area_code", "customer_address", "customer_lng", "customer_lat"] };
        apis1["yunba.liner.v1.site.customer.updateReceiver"] = { name: "收货人修改接口", params:["customer_id", "customer_company", "customer_linkman", "customer_linktel", "customer_area", "customer_area_code", "customer_address", "customer_lng", "customer_lat"] };
        apis1["yunba.liner.v1.site.customer.deleteReceiver"] = { name: "收货人删除接口", params:["customer_ids"] };
        apis1["yunba.liner.v1.site.customer.getReceiver"] = { name: "收货人获取接口", params:["customer_id"] };

        /* 发货人模块 */
        apis1["yunba.liner.v1.site.customer.listSender"] = { name: "发货人列表接口", params:["page", "page_num","customer_company"] };
        apis1["yunba.liner.v1.site.customer.addSender"] = { name: "发货人新增接口", params:["customer_company", "customer_linkman", "customer_linktel", "customer_area", "customer_area_code", "customer_address", "customer_lng", "customer_lat"] };
        apis1["yunba.liner.v1.site.customer.updateSender"] = { name: "发货人修改接口", params:["customer_id", "customer_company", "customer_linkman", "customer_linktel", "customer_area", "customer_area_code", "customer_address", "customer_lng", "customer_lat"] };
        apis1["yunba.liner.v1.site.customer.deleteSender"] = { name: "发货人删除接口", params:["customer_ids"] };
        apis1["yunba.liner.v1.site.customer.getSender"] = { name: "发货人获取接口", params:["customer_id"] };
        
        apis1["yunba.liner.v1.site.customer.customerSelectList"] = { name: "客户下拉框列表", params:["customer_type"] };
        
        /* 自有车辆管理 */
        apis1["yunba.liner.v1.site.truck.listInteriorTruck"] = { name: "自有车辆列表接口", params:["page", "page_num"] };
        apis1["yunba.liner.v1.site.truck.addInteriorTruck"] = { name: "自有车辆新增接口", params:["truck_plate", "truck_frame", "truck_type", "truck_length", "truck_weight", "truck_cube", "truck_drive_lisence", "truck_operating_lisence", "truck_pics"] };
        apis1["yunba.liner.v1.site.truck.updateInteriorTruck"] = { name: "自有车辆修改接口", params:["site_truck_id", "truck_plate", "truck_frame", "truck_type", "truck_length", "truck_weight", "truck_cube", "truck_drive_lisence", "truck_operating_lisence", "truck_pics"] };
        apis1["yunba.liner.v1.site.truck.deleteInteriorTruck"] = { name: "自有车辆删除接口", params:["site_truck_ids"] };
        apis1["yunba.liner.v1.site.truck.getInteriorTruck"] = { name: "自有车辆获取接口", params:["site_truck_id"] };
        apis1["yunba.liner.v1.site.truck.changeTruckDriver"] = { name: "修改司机接口", params:["site_truck_id", "site_driver_id"] };
        apis1["yunba.liner.v1.site.truck.getTruckByPlate"] = { name: "根据车牌号获取运吧车辆信息", params:["truck_plate"] };

        /* 外协车辆管理 */
        apis1["yunba.liner.v1.site.truck.listExteriorTruck"] = { name: "外协车辆列表接口", params:["page", "page_num"] };
        apis1["yunba.liner.v1.site.truck.addExteriorTruck"] = { name: "外协车辆新增接口", 
        		params:["truck_plate", "truck_frame", "truck_type", "truck_length", 
        		        "truck_weight", "truck_cube", "truck_drive_lisence", "truck_operating_lisence", "truck_pics",
        		        "driver_name","driver_id_no","driver_mobile","driver_sex","id_front_pic","id_back_pic","driver_lisence_pic","driver_pic"] };
        apis1["yunba.liner.v1.site.truck.updateExteriorTruck"] = { name: "外协车辆修改接口", 
                params:["site_truck_id","truck_plate", "truck_frame", "truck_type", "truck_length", 
                        "truck_weight", "truck_cube", "truck_drive_lisence", "truck_operating_lisence", "truck_pics",
                        "driver_name","driver_id_no","driver_mobile","driver_sex","id_front_pic","id_back_pic","driver_lisence_pic","driver_pic"] };
        apis1["yunba.liner.v1.site.truck.deleteExteriorTruck"] = { name: "外协车辆删除接口", params:["site_truck_ids"] };
        apis1["yunba.liner.v1.site.truck.getExteriorTruck"] = { name: "外协车辆获取接口", params:["site_truck_id"] };
        
        
        /* 司机管理 */
        apis1["yunba.liner.v1.site.driver.listInteriorDriver"] = { name: "自有司机列表接口", params:["page", "page_num"] };
        apis1["yunba.liner.v1.site.driver.addInteriorDriver"] = { name: "自有司机新增接口", params:["driver_name","driver_id_no","driver_mobile","driver_sex","id_front_pic","id_back_pic","driver_lisence_pic","driver_pic","site_truck_id"] };
        apis1["yunba.liner.v1.site.driver.updateInteriorDriver"] = { name: "自有司机修改接口", params:["site_driver_id", "driver_name","driver_id_no","driver_mobile","driver_sex","id_front_pic","id_back_pic","driver_lisence_pic","driver_pic"] };
        apis1["yunba.liner.v1.site.driver.deleteInteriorDriver"] = { name: "自有司机删除接口", params:["site_driver_ids"] };
        apis1["yunba.liner.v1.site.driver.getInteriorDriver"] = { name: "自有司机获取接口", params:["site_driver_id"] };
        apis1["yunba.liner.v1.site.driver.getDriverByPhone"] = { name: "根据手机号获取运吧司机信息", params:["phone"] };
        apis1["yunba.liner.v1.site.driver.listExteriorDriver"] = { name: "外协司机列表接口", params:["page", "page_num"] };
        
        /* 开单 */
        apis1["yunba.liner.v1.site.order.creatOrder"] = { name: "运单管理-创建运单", params:["customer_order_no","order_send_type",
                                                                                "send_customer_company","send_customer_name","send_customer_tel","order_start_area","order_start_area_code","send_customer_addr","send_customer_lng","send_customer_lat","receive_customer_company","receive_customer_name","receive_customer_tel","receive_customer_addr","receive_customer_lng","receive_customer_lat",
                                                                                "order_end_area","order_end_area_code","sign_back_nums","sign_back_type_original","sign_back_type_electric","sign_back_remark","fee_transport",
                                                                                "fee_send_cargo","fee_get_cargo","fee_proxy","fee_proxy_process","fee_insurance","fee_other",
                                                                                "pay_now","pay_arrive","pay_sign_back","pay_month","pay_from_fee_proxy","pay_remark",
                                                                                "cargos"] };
        /* 修改运单 */
        apis1["yunba.liner.v1.site.order.updateOrder"] = { name: "运单管理-修改运单", params:["order_id","customer_order_no","order_send_type",
                                                                                      "send_customer_company","send_customer_name","send_customer_tel","order_start_area","order_start_area_code","send_customer_addr","send_customer_lng","send_customer_lat","receive_customer_company","receive_customer_name","receive_customer_tel","receive_customer_addr","receive_customer_lng","receive_customer_lat",
                                                                                      "order_end_area","order_end_area_code","sign_back_nums","sign_back_type_original","sign_back_type_electric","sign_back_remark","fee_transport",
                                                                                      "fee_send_cargo","fee_get_cargo","fee_proxy","fee_proxy_process","fee_insurance","fee_other",
                                                                                      "pay_now","pay_arrive","pay_sign_back","pay_month","pay_from_fee_proxy","pay_remark",
                                                                                      "cargos"] };

        apis1["yunba.liner.v1.site.order.getOrder"] = { name: "运单管理-运单明细", params:["order_id"] };
        apis1["yunba.liner.v1.site.order.deleteOrder"] = { name: "运单管理-删除运单", params:["order_id"] };
        apis1["yunba.liner.v1.site.order.logistics.tracking"] = { name: "运单管理-物流跟踪", params:["order_id"] };
        apis1["yunba.liner.v1.site.cargo.queryCargoName"] = { name: "根据货物名称查询货物列表", params:["cargo_name"] };
        apis1["yunba.liner.v1.site.cargo.addCargo"] = { name: "运单管理-新增货物信息", params:["order_id","seq","cargo_name","cargo_unit","cargo_amount","cargo_weight","cargo_volume"] };
        apis1["yunba.liner.v1.site.cargo.updateCargo"] = { name: "运单管理-修改货物信息", params:["cargo_id","order_id","seq","cargo_name","cargo_unit","cargo_amount","cargo_weight","cargo_volume"] };
        apis1["yunba.liner.v1.site.cargo.deleteCargo"] = { name: "运单管理-删除货物信息", params:["cargo_id","order_id"] };
        /* 运单配载  */
        apis1["yunba.liner.v1.site.trip.createTrip"] = { name: "运单管理-运单配载", params:["order_ids","site_truck_id","site_driver_id","end_site_id","trip_order_dispatch","trip_order_fee","trip_pay_now","trip_pay_arrive","trip_pay_month"] };
        apis1["yunba.liner.v1.site.trip.cancelTrip"] = { name: "运单管理-取消配载", params:["trip_id","order_id"] };
        /* 我的运单 */
        apis1["yunba.liner.v1.site.order.searchAllOrders"] = { name: "我的运单-全部列表", params:["page", "page_num", "order_no","send_customer_name","send_customer_tel","order_start_area_code","order_end_area_code","startDate","endDate","receive_customer_name","receive_customer_tel"] };
        apis1["yunba.liner.v1.site.order.searchWaitLoadOrders"] = { name: "我的运单-待配载列表", params:["page", "page_num", "order_no","send_customer_name","send_customer_tel","order_start_area_code","order_end_area_code","startDate","endDate","receive_customer_name","receive_customer_tel","seq_create_time","seq_end_area_code"] };
        apis1["yunba.liner.v1.site.order.searchWaitSendOrders"] = { name: "我的运单-待发车列表", params:["page", "page_num", "order_no","send_customer_name","send_customer_tel","order_start_area_code","order_end_area_code","startDate","endDate","receive_customer_name","receive_customer_tel"] };
        apis1["yunba.liner.v1.site.order.searchTransportingOrders"] = { name: "我的运单-运输中列表", params:["page", "page_num", "order_no","send_customer_name","send_customer_tel","order_start_area_code","order_end_area_code","startDate","endDate","receive_customer_name","receive_customer_tel"] };
        apis1["yunba.liner.v1.site.order.searchWaitSignOrders"] = { name: "我的运单-待签收列表", params:["page", "page_num", "order_no","send_customer_name","send_customer_tel","order_start_area_code","order_end_area_code","startDate","endDate","receive_customer_name","receive_customer_tel"] };
        apis1["yunba.liner.v1.site.order.searchSignOrders"] = { name: "我的运单-已签收列表", params:["page", "page_num", "order_no","send_customer_name","send_customer_tel","order_start_area_code","order_end_area_code","startDate","endDate","receive_customer_name","receive_customer_tel"] };
        apis1["yunba.liner.v1.site.order.statisticsOrder"] = { name: "我的运单-统计运单数", params:["order_no","send_customer_name","send_customer_tel","order_start_area_code","order_end_area_code","startDate","endDate","receive_customer_name","receive_customer_tel"] };
        
        
        /* 首页 */
        apis1["yunba.liner.v1.site.homepage.password.update"] = { name: "首页-修改密码", params:["user_pwd", "old_user_pwd"] };
        apis1["yunba.liner.v1.site.homepage.message.list.get"] = { name: "首页-新消息列表", params:[] };
        apis1["yunba.liner.v1.site.homepage.message.read"] = { name: "首页-新消息阅读操作", params:["msg_id"] };
        apis1["yunba.liner.v1.site.order.seach"] = { name: "首页-查询运单", params:["key","page", "page_num"] };
        apis1["yunba.liner.v1.site.seach.next"] = { name: "下游网点", params:["partener_type"] };
        apis1["yunba.liner.v1.site.homepage.advert.list.get"] = { name: "首页-轮播广告", params:[] };
        apis1["yunba.liner.v1.site.homepage.post.list.get"] = { name: "首页-公告列表", params:["page", "page_num"] };
        apis1["yunba.liner.v1.site.homepage.post.get"] = { name: "首页-公告详情", params:["post_id"] };
	    apis1["yunba.liner.v1.site.order.seach"] = { name: "首页-查询运单", params:["key","page", "page_num"] };
	    apis1["yunba.liner.v1.site.truck.in.list"] = { name: "自有车辆", params:["page", "page_num","truck_id"] };
	    apis1["yunba.liner.v1.site.truck.out.list"] = { name: "外协车辆", params:["page", "page_num","truck_id"] };
	    
	    /** 发车 */
	    apis1["yunba.liner.v1.site.trip.status.num"] = {name: "发车-车次各个状态数量", params:["in_days"]};
	    apis1["yunba.liner.v1.site.trip.list"] = {name: "发车-车次列表", params:["trip_status", "in_days", "order_by", "order_by_type", "include_finished", "page", "page_num"]};
	    apis1["yunba.liner.v1.site.trip.detail"] = {name: "发车-车次详情", params:["trip_id"]};
	    apis1["yunba.liner.v1.site.trip.order.list"] = {name: "发车-车次配载运单", params:["trip_id"]};
	    apis1["yunba.liner.v1.site.trip.site.trace"] = {name: "发车-车次物流轨迹", params:["trip_id"]};
	    apis1["yunba.liner.v1.site.trip.start"] = {name: "发车-确认发车", params:["trip_id"]};
	    apis1["yunba.liner.v1.site.trip.finish"] = {name: "发车-确认到车", params:["trip_id"]};
	    apis1["yunba.liner.v1.site.trip.cancelAllTrip"] = { name: "发车-取消车次", params:["trip_ids"] };
        
	    /* 车辆跟踪  */
	    apis1["yunba.liner.v1.site.truck.location"] = {name : "车辆跟踪", params:["truck_status"]};
	    apis1["yunba.liner.v1.site.trip.trace"] = {name: "车辆跟踪-车次轨迹(根据车次号查询)", params:["trip_no"]};
	    apis1["yunba.liner.v1.site.trip.trace.by.orderno"] = {name: "车辆跟踪-车次轨迹(根据运单号查询)", params:["order_no"]};
	    apis1["yunba.liner.v1.site.trip.trace.by.tripid"] = {name: "车辆跟踪-车次轨迹(根据车次id查询)", params:["trip_id"]};
        
	    /* 回单模块 */
        apis1["yunba.liner.v1.site.sign.recycling.list"] = { name: "回单-待回收列表", params:["order_status", "customer_company","create_top_time", "create_bottom_time","page", "page_num"] };
        apis1["yunba.liner.v1.site.sign.recycled.list"] = { name: "回单-已回收列表", params:["order_status", "customer_company","create_top_time", "create_bottom_time","page", "page_num"] };
        apis1["yunba.liner.v1.site.sign.deliver.list"] = { name: "回单-已发放列表", params:["order_status", "customer_company","create_top_time", "create_bottom_time","page", "page_num"] };
        apis1["yunba.liner.v1.site.sign.pics.save"] = { name: "回单-上传回单图片保存操作", params:["sign_back_id","sign_back_pics"] };
        apis1["yunba.liner.v1.site.sign.recycle.confirm"] = { name: "回单-确认回收", params:["sign_back_ids"] };
        apis1["yunba.liner.v1.site.sign.deliver.confirm"] = { name: "回单-确认发放", params:["sign_back_ids", "deliver_receiver_name","deliver_receiver_mobile"] };
     	
        /* 到站运单模块 */
        apis1["yunba.liner.v1.site.order.arrive"] = { name: "到站运单-列表", params:["status","pre_site_id","partener_type","trip_no","order_send_type","sort_name","page", "page_num"] };
        apis1["yunba.liner.v1.site.order.arrive.confirm"] = { name: "到站运单-确认到站", params:["trip_order_id","problem","remark"] };
        apis1["yunba.liner.v1.site.order.arrive.sign.orders"] = { name: "到站运单-批量签收", params:["trip_order_ids","sign_name","card_no","remark","is_send"] };
        apis1["yunba.liner.v1.site.order.arrive.detail"] = { name: "到站运单-查看详情", params:["order_id"] };
        apis1["yunba.liner.v1.site.order.arrive.counts"] = { name: "到站运单-统计未到站、待签收、已签收状态运单数", params:[] };
        apis1["yunba.liner.v1.site.order.arrive.trip.orders"] = { name: "到站运单-配载运单列表", params:["trip_id"] };
        
        /* 操作记录 */
        apis1["yunba.liner.v1.site.operation.list"] = { name: "操作记录 ", params:["user_id","order_no","trip_no","op_top_time","op_bottom_time","page", "page_num"] };
        apis1["yunba.liner.v1.site.user.list"] = { name: "获取用户下拉框列表 ", params:[] };
        
        /* 统计分析 */
        apis1["yunba.liner.v1.site.statistics.orderAndTrip"] = { name: "运单/车次统计", params:["min_date","max_date"] };
        apis1["yunba.liner.v1.site.statistics.report"] = { name: "报表明细", params:["page","page_num","send_customer","trip_no","status","start_date","end_date"] };
        apis1["yunba.liner.v1.site.statistics.report.columns.get"] = { name: "报表明细获取自定义设置信息", params:[] };
        apis1["yunba.liner.v1.site.statistics.report.columns.edit"] = { name: "报表明细保存自定义设置信息", params:["columns"] };
        apis1["yunba.liner.v1.site.statistics.customer.send"] = { name: "发货方统计", params:["page","page_num"] };
        apis1["yunba.liner.v1.site.statistics.driver"] = { name: "司机运输统计", params:["page","page_num"] };
        
        // 公共接口
        var apis2 = apis[2] = {};
        apis2["yunba.liner.v1.common.dict"] = {name: "常用字典", params:["dict_name"]};
        apis2["yunba.liner.v1.common.area"] = {name: "省市县地区", params:["parent_code"]};
        
    </script>
  </head>
  
  <body>
    <table width="100%">
        <tr>
            <td width="20%" valign="top" style="font: 10px;">
                <input type="radio" name="rdCommand" value="1" checked="checked" onclick="showApp()">网页版
                 <input type="radio" name="rdCommand" value="2" onclick="showApp()">公共接口
                <br><a href="linertool.jsp" target="_blank">工具页</a>
                <ul id="selCommands1" class="commands"></ul>
                <ul id="selCommands2" class="commands" style="display: none"></ul>
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
			
			if(params[i] == 'geetest'){
			    $tdParams.append('<tr><td class="param-name" style="width:15%;" align="right"></td><td><div id="embed-captcha"></div></td></tr>');
			    // 验证开始需要向网站主后台获取id，challenge，success（是否启用failback）
	            $.ajax({
	                // 获取id，challenge，success（是否启用failback）
	                url: "liner/geetest/register?t=" + (new Date()).getTime(), // 加随机数防止缓存
	                type: "get",
	                dataType: "json",
	                success: function (data) {
	                    // 使用initGeetest接口
	                    // 参数1：配置参数
	                    // 参数2：回调，回调的第一个参数验证码对象，之后可以使用它做appendTo之类的事件
	                    initGeetest({
	                        gt: data.gt,
	                        challenge: data.challenge,
	                        product: "embed", // 产品形式，包括：float，embed，popup。注意只对PC版验证码有效
	                        offline: !data.success // 表示用户后台检测极验服务器是否宕机，一般不需要关注
	                        // 更多配置参数请参见：http://www.geetest.com/install/sections/idx-client-sdk.html#config
	                    }, function (captchaObj) {
	                        /* $("#embed-submit").click(function (e) {
	                            var validate = captchaObj.getValidate();
	                            if (!validate) {
	                                $("#notice")[0].className = "show";
	                                setTimeout(function () {
	                                    $("#notice")[0].className = "hide";
	                                }, 2000);
	                                e.preventDefault();
	                            }
	                        }); */
	                        // 将验证码加到id为captcha的元素里，同时会有三个input的值：geetest_challenge, geetest_validate, geetest_seccode
	                        captchaObj.appendTo("#embed-captcha");
	                        captchaObj.onReady(function () {
	                            //$("#wait")[0].className = "hide";
	                        });
	                    });
	                }
	            });
			}else{
			    $tdParams.append('<tr><td class="param-name" style="width:15%;" align="right">' + params[i] + 
				    '</td><td class="param-value"><input />' + 
				    (params[i] == 'imgcode' ? '<img src="liner/imgcode" class="imgcode"/>' : '') + 
				    '</td></tr>');
			}
		}
		
			
	    
	});
	
	/* var handlerPopup = function (captchaObj) {
		$("#embed-submit").click(function (e) {
            var validate = captchaObj.getValidate();
            if (!validate) {
                $("#notice")[0].className = "show";
                setTimeout(function () {
                    $("#notice")[0].className = "hide";
                }, 2000);
                e.preventDefault();
            }
        });
        // 将验证码加到id为captcha的元素里，同时会有三个input的值：geetest_challenge, geetest_validate, geetest_seccode
        captchaObj.appendTo("#embed-captcha");
        captchaObj.onReady(function () {
            $("#wait")[0].className = "hide";
        });
    }; */
	
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
		$('tr', $tdParams).each(function(){ // 其他请求参数
			var $tr = $(this);
			data[$('td.param-name', $tr).text()] = $('td.param-value input', $tr).val();
		});
		
		if(data.method=='yunba.liner.v1.site.user.login'){
			data.geetest_challenge = $('.geetest_challenge').val();
            data.geetest_validate = $('.geetest_validate').val();
            data.geetest_seccode = $('.geetest_seccode').val();
            console.log(data.geetest_challenge);
		}
		
		var url = "";  // 请求路径
		var checked = $('input[name=rdCommand]:checked').val();
		if (checked == "1") {
			url = "liner/webapp";
		} else if (checked == "2"){
			url = "liner/webapp";
		} else if (checked == "3") {
			url = "liner/webapp";
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
